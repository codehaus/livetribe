/**
 *
 * Copyright 2005 (C) The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.totem.single;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ScheduledExecutorService;
import org.activeio.AsyncChannelListener;
import org.activeio.Packet;
import org.activeio.command.AsyncChannelToAsyncCommandChannel;
import org.activeio.command.CommandListener;
import org.activeio.command.WireFormat;
import org.activeio.net.DatagramSocketSyncChannelFactory;
import org.activeio.net.MulticastSocketSyncChannel;
import org.activeio.net.MulticastSocketSyncChannelFactory;

import org.livetribe.totem.FilePersistentStore;
import org.livetribe.totem.Identifier;
import org.livetribe.totem.PersistentStore;
import org.livetribe.totem.TotemException;


/**
 * @version $Revision: $ $Date: $
 */
public class SingleRingChannel extends SingleRingChannelBase
{
    private final static long DEFAULT_TIMEOUT = 10 * 1000;
    private final static int DEFAULT_WINDOW = 65536;
    private final static int DEFAULT_MAX_MESSAGES = 1024;

    private AsyncRinglListener ringListener;

    private final BusChannel busChannel;
    private final TokenRingChannel ringChannel;
    private final WireFormat wireFormat;
    private final PersistentStore store;
    private AsyncChannelToAsyncCommandChannel ring;
    private AsyncChannelToAsyncCommandChannel bus;
    private long timeout = DEFAULT_TIMEOUT;

    private Identifier myId;

    private Token lastToken;
    private long myTokenSequence;
    private long myARU;
    private long lastARU;
    private final ARUTracker tracker = new ARUTracker();
    private byte myARUCount;
    private byte failToReceiveMax;
    private BlockingQueue newMessageQueue = new LinkedBlockingQueue();
    private SortedSet receivedMessageQueue = new TreeSet();
    private Set retransRequestSet = new HashSet();
    private long highDelivered;

    private RingId myRingId;
    private Set myMembers = new HashSet();
    private Set myProcessorSet = new HashSet();
    private Set myFailedSet = new HashSet();
    private Map consensus = new HashMap();
    private int maxIncarnation;

    /**
     * Flow Control
     */
    private int windowSize = DEFAULT_WINDOW;
    private int maxMessages = DEFAULT_MAX_MESSAGES;
    private int myTrc;
    private int myPbl;
    private int myTbl;

    /**
     * @param busChannel
     * @param ringChannel
     * @param wireFormat
     * @param store
     */
    public SingleRingChannel(BusChannel busChannel, TokenRingChannel ringChannel, WireFormat wireFormat,
                             ScheduledExecutorService clockDaemon, PersistentStore store)
    {
        super(clockDaemon);
        this.busChannel = busChannel;
        this.wireFormat = wireFormat;
        this.ringChannel = ringChannel;
        this.store = store;
    }

    public void setAsyncChannelListener(AsyncChannelListener channelListener)
    {
        if (channelListener == null) throw new IllegalArgumentException("Channel Listener is null");
        if (!(channelListener instanceof AsyncRinglListener))
        {
            throw new IllegalArgumentException("Channel Listener not instance of AsyncRinglListener");
        }

        this.ringListener = (AsyncRinglListener) channelListener;
    }

    public AsyncChannelListener getAsyncChannelListener()
    {
        return ringListener;
    }

    public long getTimeout()
    {
        return timeout;
    }

    public void setTimeout(long timeout)
    {
        if (timeout < 0) timeout = DEFAULT_TIMEOUT;

        this.timeout = timeout;
    }

    public int getWindowSize()
    {
        return windowSize;
    }

    public void setWindowSize(int windowSize)
    {
        if (windowSize < 0) windowSize = DEFAULT_WINDOW;

        this.windowSize = windowSize;
    }

    public int getMaxMessages()
    {
        return maxMessages;
    }

    public void setMaxMessages(int maxMessages)
    {
        if (maxMessages < 0) maxMessages = DEFAULT_MAX_MESSAGES;

        this.maxMessages = maxMessages;
    }

    public byte getFailToReceiveMax()
    {
        return failToReceiveMax;
    }

    public void setFailToReceiveMax(byte failToReceiveMax)
    {
        this.failToReceiveMax = failToReceiveMax;
    }

    public Object getAdapter(Class target)
    {
        if (target.isAssignableFrom(getClass())) return this;

        return null;
    }

    public void dispose()
    {
        bus.dispose();
    }

    public void start() throws IOException
    {
        state = GATHER;

        MulticastSocketSyncChannel c = (MulticastSocketSyncChannel) busChannel.getAdapter(MulticastSocketSyncChannel.class);
        bus = new AsyncChannelToAsyncCommandChannel(busChannel, wireFormat);
        bus.setCommandListener(new CommandListener()
        {
            public void onCommand(Object command)
            {
                try
                {
                    synchronized (STATE_SEMAPHORE)
                    {
                        if (command instanceof RegularMessage)
                        {
                            RegularMessage regular = (RegularMessage) command;

                            if (myMembers.contains(regular.getSenderId()))
                            {
                                state = state.process(regular);
                            }
                            else
                            {
                                state = state.processForeign(regular);
                            }
                        }
                        else if (command instanceof JoinMessage)
                        {
                            state = state.process((JoinMessage) command);
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();  //TODO: change body of catch statement use File | Settings | File Templates.
                }
            }

            public void onError(Exception e)
            {
                //TODO: change body of implemented methods use File | Settings | File Templates.
            }
        });
        bus.start();

        ring = new AsyncChannelToAsyncCommandChannel(ringChannel, wireFormat);
        ring.setCommandListener(new CommandListener()
        {
            public void onCommand(Object command)
            {
                try
                {
                    synchronized (STATE_SEMAPHORE)
                    {
                        if (command instanceof RegularToken)
                        {
                            state = state.process((RegularToken) command);
                        }
                        else if (command instanceof CommitToken)
                        {
                            state = state.process((CommitToken) command);
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();  //TODO: change body of catch statement use File | Settings | File Templates.
                }
            }

            public void onError(Exception e)
            {
                //TODO: change body of implemented methods use File | Settings | File Templates.
            }
        });
        ring.start();

        myId = new DefaultIdentifier(ringChannel.getAddress(), ringChannel.getPort());
        try
        {
            myRingId = new RingId(store.getRingIncarnation(), myId);
        }
        catch (IOException e)
        {
            throw (TotemException) (new TotemException("Error retrieving ring incarnation number").initCause(e));
        }
        myMembers.add(myId);

        JoinMessage joinMessage = new JoinMessage();
        joinMessage.setIncarnation(myRingId.getIncarnation());
        joinMessage.setSenderId(myId);
        joinMessage.getProcessorSet().add(myId);

        bus.writeCommand(joinMessage);
    }

    public void stop(long timeout) throws IOException
    {
        bus.stop(timeout);
    }

    public void write(Packet packet) throws IOException
    {
        try
        {
            newMessageQueue.put(packet);
        }
        catch (InterruptedException e)
        {
            throw (TotemException) (new TotemException("Interrupted while inserting packet into queue").initCause(e));
        }
    }

    public void flush() throws IOException
    {
        //TODO: change body of implemented methods use File | Settings | File Templates.
    }

    protected int computeFlowControl(RegularToken token)
    {
        myPbl = myTbl;
        myTbl = newMessageQueue.size();

        int result = maxMessages;

        int max = windowSize - token.getFcc();
        if (result > max) result = max;

        max = windowSize * myTbl / (token.getBacklog() + myTbl - myPbl);
        if (result > max) result = max;

        return result;
    }

    protected State shiftToGather() throws IOException
    {
        JoinMessage joinMessage = new JoinMessage();
        joinMessage.setIncarnation(myRingId.getIncarnation());
        joinMessage.setSenderId(myId);
        joinMessage.getProcessorSet().addAll(myProcessorSet);
        joinMessage.getFailedSet().addAll(myFailedSet);

        bus.writeCommand(joinMessage);

        cancelTokenLossTimeout();
        cancelTokenRetransTimeout();
        resetJoinTimeout();
        resetConsensusTimeout();

        consensus.clear();
        consensus.put(myId, Boolean.TRUE);
        maxIncarnation = -1;

        return GATHER;
    }

    protected State shiftToCommit(CommitToken token) throws IOException
    {
        Member member = new Member();
        member.myId = myId;
        member.oldRingId = myRingId;
        member.oldMyARU = myARU;
        member.highDelivered = highDelivered;
        member.received = false;

        List members = token.getMembers();
        int index = members.indexOf(member);
        if (index < 0)
        {
            token.getMembers().add(member);
        }
        else
        {
            token.getMembers().set(members.indexOf(member), member);
        }

        myRingId = token.getRingId();

        ring.writeCommand(token);

        cancelJoinTimeout();
        cancelConsensusTimeout();
        resetTokenLossTimeout();
        resetTokenRetransTimeout();

        return COMMIT;
    }

    protected State shiftToRecovery()
    {
        return RECOVERY;
    }

    protected State shiftToOperational()
    {
        return OPERATIONAL;
    }

    private long computeMyARU(long ARU)
    {
        for (Iterator iter = receivedMessageQueue.iterator(); iter.hasNext();)
        {
            RegularMessage message = (RegularMessage) iter.next();

            if (ARU + 1 == message.getSequenceId())
            {
                ARU++;
            }
            else
            {
                break;
            }
        }
        return ARU;
    }

    private void deliverMessages()
    {
        SortedSet delivered = new TreeSet();
        for (Iterator iter = receivedMessageQueue.iterator(); iter.hasNext();)
        {
            RegularMessage message = (RegularMessage) iter.next();

            if (ringListener != null)
            {
                ringListener.onPacket(message.getPacket());
            }
            delivered.add(message);
            highDelivered = message.getSequenceId();

            if (tracker.getARU() == message.getSequenceId()) break;
        }
        receivedMessageQueue.removeAll(delivered);
    }

    private final State OPERATIONAL = new Operational();
    private final State GATHER = new Gather();
    private final State COMMIT = new Commit();
    private final State RECOVERY = new Recovery();

    class Operational extends State
    {
        State process(RegularToken regular) throws IOException
        {
            if (regular.getRingId().equals(myRingId) && regular.getTokenSequence() > myTokenSequence)
            {
                int maxMessages = computeFlowControl(regular);
                int myPrc = myTrc;
                myTrc = 0;

                Set retransmit = new HashSet();
                Set candidates = regular.getRtr();

                for (Iterator iter = receivedMessageQueue.iterator(); iter.hasNext() && maxMessages-- > 0;)
                {
                    RegularMessage message = (RegularMessage) iter.next();
                    Long messageId = new Long(message.getSequenceId());

                    if (candidates.contains(messageId))
                    {
                        retransmit.add(message);
                        candidates.remove(messageId);
                    }
                }

                for (Iterator iter = retransmit.iterator(); iter.hasNext();)
                {
                    bus.writeCommand(iter.next());

                    myTrc++;
                }

                long sequenceId = regular.getSequenceId();
                while (newMessageQueue.peek() != null && maxMessages-- > 0)
                {
                    Packet packet = null;
                    try
                    {
                        packet = (Packet) newMessageQueue.take();
                    }
                    catch (InterruptedException e)
                    {
                        throw (TotemException) (new TotemException("Interrupted while taking packet from queue").initCause(e));
                    }

                    RegularMessage message = new RegularMessage();

                    message.setSenderId(myId);
                    message.setRingId(myRingId);
                    message.setSequenceId(++sequenceId);
                    message.setPacket(packet);

                    bus.writeCommand(message);

                    myTrc++;
                }
                regular.setSequenceId(sequenceId);

                lastARU = regular.getAru();
                myARU = computeMyARU(myARU);

                if (myARU < regular.getAru() || regular.getAruId() == null || myId.equals(regular.getAruId()))
                {
                    regular.setAru(myARU);

                    if (regular.getAru() == regular.getSequenceId())
                    {
                        regular.setAruId(null);
                    }
                    else
                    {
                        regular.setAruId(myId);
                    }
                }

                if (regular.getAru() == lastARU && regular.getAruId() != null)
                {
                    myARUCount++;
                }
                else
                {
                    myARUCount = 0;
                }

                if (myARUCount > failToReceiveMax && !regular.getAruId().equals(myId))
                {
                    myFailedSet.clear();
                    myFailedSet.add(regular.getAruId());

                    return shiftToGather();
                }
                else
                {
                    regular.setBacklog(regular.getBacklog() + myTbl - myPbl);
                    regular.setFcc(regular.getFcc() + myTrc - myPrc);

                    ring.writeCommand(regular);

                    lastToken = regular;
                    tracker.setARU(regular.getAru());

                    resetTokenLossTimeout();
                    resetTokenRetransTimeout();

                    deliverMessages();
                }
            }
            return myState();
        }

        State process(RegularMessage message) throws IOException
        {
            cancelTokenLossTimeout();

            receivedMessageQueue.add(message);
            retransRequestSet.remove(new Long(message.getSequenceId()));

            myARU = computeMyARU(myARU);

            deliverMessages();

            return myState();
        }

        State tokenLossTimeout() throws IOException
        {
            return shiftToGather();
        }

        State tokenRetransmissionTimeout() throws IOException
        {
            ring.writeCommand(lastToken);

            startTokenRetransTimeout();

            return myState();
        }

        State processForeign(RegularMessage message) throws IOException
        {
            myProcessorSet.add(message.getSenderId());
            return shiftToGather();
        }

        State process(JoinMessage message) throws IOException
        {
            if (myProcessorSet.equals(message.getProcessorSet()) && myFailedSet.equals(message.getFailedSet()))
            {
                consensus.put(message.getSenderId(), Boolean.TRUE);
                if (maxIncarnation < message.getIncarnation()) maxIncarnation = message.getIncarnation();

                Set liveProcessors = new HashSet(myProcessorSet);
                liveProcessors.removeAll(myFailedSet);

                boolean haveConsensus = true;
                Identifier smallestId = null;
                for (Iterator iter = liveProcessors.iterator(); iter.hasNext();)
                {
                    Identifier id = (Identifier) iter.next();

                    if (consensus.get(id) != Boolean.TRUE) haveConsensus = false;
                    if (smallestId == null || smallestId.compareTo(id) > 0) smallestId = id;
                }

                if (haveConsensus && myId.equals(smallestId))
                {
                    CommitToken token = new CommitToken();
                    token.setRingId(new RingId(maxIncarnation + 4, myId));

                    return shiftToCommit(token);
                }
            }
            else if (!(myProcessorSet.containsAll(message.getProcessorSet())
                       && myFailedSet.containsAll(message.getFailedSet()))
                     && !myFailedSet.contains(message.getSenderId()))
            {
                myProcessorSet.addAll(message.getProcessorSet());

                if (message.getFailedSet().contains(myId))
                {
                    myFailedSet.add(message.getSenderId());
                }
                else
                {
                    myFailedSet.addAll(message.getFailedSet());
                }
                return shiftToGather();
            }
            return myShiftToGather();
        }

        State process(CommitToken token) throws IOException
        {
            return myState();
        }

        State joinTimeout() throws IOException
        {
            throw new RuntimeException("Join timeout not supported");
        }

        State consensusTimeout() throws IOException
        {
            throw new RuntimeException("Consensus timeout not supported");
        }

        protected State myShiftToGather() throws IOException
        {
            return shiftToGather();
        }
    }

    class Gather extends Operational
    {
        State processForeign(RegularMessage message) throws IOException
        {
            if (!myProcessorSet.contains(message.getSenderId()))
            {
                myProcessorSet.add(message.getSenderId());
                return shiftToGather();
            }
            return myState();
        }

        State tokenLossTimeout() throws IOException
        {
            Set liveProcessors = new HashSet(myProcessorSet);
            liveProcessors.removeAll(myFailedSet);
            boolean haveConsensus = true;

            for (Iterator iter = liveProcessors.iterator(); iter.hasNext();)
            {
                if (consensus.get(iter.next()) != Boolean.TRUE)
                {
                    haveConsensus = false;
                    break;
                }
            }

            if (!haveConsensus)
            {
                for (Iterator iter = liveProcessors.iterator(); iter.hasNext();)
                {
                    Identifier id = (Identifier) iter.next();

                    if (consensus.get(id) != Boolean.TRUE)
                    {
                        myFailedSet.add(id);
                    }
                }
            }
            else
            {
                consensus.clear();
                maxIncarnation = -1;

                resetTokenLossTimeout();
            }
            return shiftToGather();
        }

        State process(CommitToken token) throws IOException
        {
            Set liveProcessors = new HashSet(myProcessorSet);
            liveProcessors.removeAll(myFailedSet);

            if (liveProcessors.equals(token.getMembers()) && token.getRingId().getIncarnation() > myRingId.getIncarnation())
            {
                return shiftToCommit(token);
            }
            return myState();
        }


        State joinTimeout() throws IOException
        {
            JoinMessage message = new JoinMessage();

            message.setIncarnation(myRingId.getIncarnation());
            message.setSenderId(myId);
            message.getProcessorSet().clear();
            message.getProcessorSet().addAll(myProcessorSet);
            message.getFailedSet().clear();
            message.getFailedSet().addAll(myFailedSet);

            bus.writeCommand(message);

            return myState();
        }

        State consensusTimeout() throws IOException
        {
            Set liveProcessors = new HashSet(myProcessorSet);
            liveProcessors.removeAll(myFailedSet);
            boolean haveConsensus = true;

            for (Iterator iter = liveProcessors.iterator(); iter.hasNext();)
            {
                if (consensus.get(iter.next()) != Boolean.TRUE)
                {
                    haveConsensus = false;
                    break;
                }
            }

            if (!haveConsensus)
            {
                for (Iterator iter = liveProcessors.iterator(); iter.hasNext();)
                {
                    Identifier id = (Identifier) iter.next();

                    if (consensus.get(id) != Boolean.TRUE)
                    {
                        myFailedSet.add(id);
                    }
                }
                return shiftToGather();
            }
            else
            {
                consensus.clear();
                maxIncarnation = -1;

                resetTokenLossTimeout();

                return myState();
            }
        }

        protected State myShiftToGather()
        {
            return myState();
        }
    }

    class Commit extends Operational
    {
        State process(RegularToken regular) throws IOException
        {
            return myState();
        }

        State tokenLossTimeout() throws IOException
        {
            return shiftToGather();
        }

        State tokenRetransmissionTimeout() throws IOException
        {
            throw new RuntimeException("Token retransmit timeout not supported");
        }

        State processForeign(RegularMessage message) throws IOException
        {
            return myState();
        }

        State process(JoinMessage message) throws IOException
        {
            // TODO: paper says to use myNewMembers but this does not make sense
            if (myMembers.contains(message.getSenderId()) && message.getIncarnation() > myRingId.getIncarnation())
            {
                return super.process(message);
            }
            return myState();
        }

        State process(CommitToken token) throws IOException
        {
            if (token.getRingId().getIncarnation() == myRingId.getIncarnation())
            {
                return shiftToRecovery();
            }
            return myState();
        }

        State joinTimeout() throws IOException
        {
            JoinMessage message = new JoinMessage();

            message.setIncarnation(myRingId.getIncarnation());
            message.setSenderId(myId);
            message.getProcessorSet().clear();
            message.getProcessorSet().addAll(myProcessorSet);
            message.getFailedSet().clear();
            message.getFailedSet().addAll(myFailedSet);

            bus.writeCommand(message);

            return myState();
        }

        State consensusTimeout() throws IOException
        {
            throw new RuntimeException("Consensus timeout not supported");
        }

        protected State myShiftToGather() throws IOException
        {
            return shiftToGather();
        }
    }

    class Recovery extends State
    {
        /**
         * recovery variables
         */
        private Set myNewMembers = new HashSet();
        private Set myTransMembers = new HashSet();
        private Set myDeliveryMembers = new HashSet();
        private long lowRingARU;
        private long highRingDelivered;
        private long myInstallSequenceId;
        private int myInstallSequenceCount;
        private SortedSet retransMessageQueue = new TreeSet();
        private SortedSet receivedMessageOldQueue = new TreeSet();
        private int myRetransCount;
        private boolean myReceivedFlag;

        State process(RegularToken regular) throws IOException
        {
            if (regular.getRingId().equals(myRingId) && regular.getTokenSequence() > myTokenSequence)
            {
                int maxMessages = computeFlowControl(regular);
                int myPrc = myTrc;
                myTrc = 0;

                Set retransmit = new HashSet();
                Set candidates = regular.getRtr();

                for (Iterator iter = receivedMessageQueue.iterator(); iter.hasNext() && maxMessages-- > 0;)
                {
                    RegularMessage message = (RegularMessage) iter.next();
                    Long messageId = new Long(message.getSequenceId());

                    if (candidates.contains(messageId))
                    {
                        retransmit.add(message);
                        candidates.remove(messageId);
                    }
                }

                for (Iterator iter = retransmit.iterator(); iter.hasNext();)
                {
                    bus.writeCommand(iter.next());

                    myTrc++;
                }

                long sequenceId = regular.getSequenceId();
                while (retransMessageQueue.size() > 0 && maxMessages-- > 0)
                {
                    Packet packet = (Packet) retransMessageQueue.first();
                    retransMessageQueue.remove(packet);

                    RegularMessage message = new RegularMessage();

                    message.setSenderId(myId);
                    message.setRingId(myRingId);
                    message.setSequenceId(++sequenceId);
                    message.setPacket(packet);

                    bus.writeCommand(message);

                    myTrc++;
                }
                regular.setSequenceId(sequenceId);

                myARU = computeMyARU(myARU);

                if (myARU < regular.getAru() || regular.getAruId() == null || myId.equals(regular.getAruId()))
                {
                    regular.setAru(myARU);

                    if (regular.getAru() == regular.getSequenceId())
                    {
                        regular.setAruId(null);
                    }
                    else
                    {
                        regular.setAruId(myId);
                    }
                }

                if (regular.getAru() == lastARU && regular.getAruId() != null)
                {
                    myARUCount++;
                }
                else
                {
                    myARUCount = 0;
                }

                lastARU = regular.getAru();

                if (myARUCount > failToReceiveMax && !regular.getAruId().equals(myId))
                {
                    myFailedSet.clear();
                    myFailedSet.add(regular.getAruId());

                    return shiftToGather();
                }
                else
                {
                    regular.setBacklog(regular.getBacklog() + myTbl - myPbl);
                    regular.setFcc(regular.getFcc() + myTrc - myPrc);

                    if (retransMessageQueue.size() > 0)
                    {
                        if (regular.getRetransmitId() == null) regular.setRetransmitId(myId);
                    }
                    else if (regular.getRetransmitId().equals(myId))
                    {
                        regular.setRetransmitId(null);
                    }

                    if (regular.getRetransmitId() == null)
                    {
                        myRetransCount++;
                    }
                    else
                    {
                        myRetransCount = 0;
                    }

                    if (myRetransCount == 2) myInstallSequenceId = regular.getSequenceId();

                    if (myRetransCount >= 2 && myARU >= myInstallSequenceId && !myReceivedFlag)
                    {
                        myReceivedFlag = true;
                        myDeliveryMembers.clear();
                        myDeliveryMembers.addAll(myTransMembers);
                    }

                    if (regular.getAru() > myInstallSequenceId)
                    {
                        myInstallSequenceCount++;
                    }
                    else
                    {
                        myInstallSequenceCount = 0;
                    }

                    if (myRetransCount >= 3 && myInstallSequenceCount >= 2)
                    {
                        return shiftToOperational();
                    }

                    ring.writeCommand(regular);

                    lastToken = regular;

                    resetTokenLossTimeout();
                    resetTokenRetransTimeout();

                    deliverMessages();
                }
            }
            return myState();
        }

        State process(RegularMessage message) throws IOException
        {

            resetTokenRetransTimeout();

            receivedMessageQueue.add(message);

            myARU = computeMyARU(myARU);

            if (retransMessageQueue.contains(message))
            {
                receivedMessageOldQueue.add(message);
                retransMessageQueue.remove(message);
                return myState();
            }
            return myState();
        }

        State tokenLossTimeout() throws IOException
        {
            receivedMessageQueue.clear();
            retransMessageQueue.clear();
            myARU = computeMyARU(myARU);

            return shiftToGather();
        }

        State tokenRetransmissionTimeout() throws IOException
        {
            ring.writeCommand(lastToken);

            startTokenRetransTimeout();

            return myState();
        }

        State processForeign(RegularMessage message) throws IOException
        {
            return myState();
        }

        State process(JoinMessage message) throws IOException
        {
            if (myNewMembers.contains(message.getSenderId()) && message.getIncarnation() > myRingId.getIncarnation())
            {
                return myState();
            }
            return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
        }

        State process(CommitToken token) throws IOException
        {
            return null;  //TODO: change body of implemented methods use File | Settings | File Templates.
        }

        State joinTimeout() throws IOException
        {
            throw new RuntimeException("Join timeout not supported");
        }

        State consensusTimeout() throws IOException
        {
            throw new RuntimeException("Consensus timeout not supported");
        }
    }

    public static void main(String[] args) throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool(15);

        final DatagramSocket socket = new DatagramSocket(8888);
        byte[] inbuf = new byte[1024];
        byte[] outbuf = new byte[1024];
        final DatagramPacket in = new DatagramPacket(inbuf, inbuf.length);
        DatagramPacket out = new DatagramPacket(outbuf, outbuf.length, InetAddress.getByName("localhost"), 8888);

        for (int i = 0; i < outbuf.length; i++) outbuf[i] = (byte) 3;

        socket.connect(InetAddress.getByName("localhost"), 8888);
        pool.submit(new Runnable()
        {

            public void run()
            {
                try
                {
                    socket.receive(in);
                }
                catch (IOException e)
                {
                    e.printStackTrace();  //TODO: change body of catch statement use File | Settings | File Templates.
                }
            }
        });

        SocketAddress laddr = socket.getLocalSocketAddress();
        SocketAddress raddr = socket.getRemoteSocketAddress();
        socket.send(out);

        Thread.sleep(1 * 1000);


        URI ringURI = new URI(args[0]);
        ScheduledExecutorService clock = Executors.newScheduledThreadPool(5);
        FilePersistentStore store = new FilePersistentStore(new File("file.date"));

        TokenRingChannel ring = new TokenRingChannel(new DatagramSocketSyncChannelFactory().openSyncChannel(null, ringURI));
        ring.setAddress(InetAddress.getByName(ringURI.getHost()));
        ring.setPort(ringURI.getPort());

        BusChannel bus = new BusChannel(new MulticastSocketSyncChannelFactory().openSyncChannel(new URI(args[1])), 3696);


        final SingleRingChannel totem = new SingleRingChannel(bus, ring, new DefaultWireFormat(), clock, store);
        totem.setTokenLossTimeout(360 * 1000);
        totem.setTokenRetransTimeout(360 * 1000);
        totem.setJoinTimeout(360 * 1000);
        totem.setConsensusTimeout(360 * 1000);

        pool.submit(new Runnable()
        {
            public void run()
            {
                try
                {
                    totem.start();
                }
                catch (IOException e)
                {
                    e.printStackTrace();  //TODO: change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }
}
