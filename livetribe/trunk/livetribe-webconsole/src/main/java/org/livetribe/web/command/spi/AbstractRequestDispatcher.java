/*
 * Copyright 2006 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.web.command.spi;

import java.util.Map;

import org.livetribe.web.command.Command;

/**
 * $Rev: 51 $
 */
public abstract class AbstractRequestDispatcher extends AbstractCommandPerformer
{
    protected static final String VARIABLE_START = "${";
    protected static final String VARIABLE_END = "}";
    protected static final String COMMAND_RESULT_PREFIX = ".result.";

    protected AbstractRequestDispatcher(Map configuration)
    {
        super(configuration);
    }

    public void perform(CommandContext context)
    {
        String commandResult = context.getCommandResult();
        if (Command.NONE.equals(commandResult)) return;

        String dispatch = getResultDispatch(context, null);
        if (dispatch == null) return;

        dispatch = resolveVariables(context, dispatch);

        try
        {
            dispatch(context, dispatch);
        }
        catch (Exception x)
        {
            // Try to figure out the error result for this page
            dispatch = getResultDispatch(context, Command.ERROR);
            dispatch = resolveVariables(context, dispatch);

            // Let's hope this one goes fine; if not, we give up
            // TODO: we may hardcode a generic error page here
            dispatch(context, dispatch);
        }
    }

    protected abstract String getResultDispatch(CommandContext context, String preferredResult);

    protected abstract void dispatch(CommandContext context, String dispatch);

    protected String resolveVariables(CommandContext context, String candidate)
    {
        StringBuffer result = new StringBuffer();
        int start = 0;
        int varStart = candidate.indexOf(VARIABLE_START, start);

        while (varStart >= 0)
        {
            int varEnd = candidate.indexOf(VARIABLE_END);
            if (varEnd < 0) throw new CommandException("Syntax error in " + candidate + ": no variable end delimiter found");

            result.append(candidate.substring(start, varStart));
            String variableName = candidate.substring(varStart + VARIABLE_START.length(), varEnd);

            String variableValue = null;
            if (hasProperty(context.getCommand(), variableName))
            {
                variableValue = resolveVariable(context, variableName);
                if (variableValue == null) variableValue = resolveImplicitVariable(context, variableName);
            }
            else
            {
                variableValue = resolveImplicitVariable(context, variableName);
            }
            if (variableValue == null) throw new CommandException("Could not resolve variable " + variableName + " in " + candidate);
            result.append(variableValue);

            start = varEnd + VARIABLE_END.length();
            varStart = candidate.indexOf(VARIABLE_START, start);
        }

        result.append(candidate.substring(start, candidate.length()));
        return result.toString();
    }

    protected String resolveVariable(CommandContext context, String variableName)
    {
        return (String)getPropertyAndConvert(context.getCommand(), variableName, String.class);
    }

    protected abstract String resolveImplicitVariable(CommandContext context, String variableName);
}
