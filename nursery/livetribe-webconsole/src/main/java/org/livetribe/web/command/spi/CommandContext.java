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

import org.livetribe.web.command.Command;

/**
 * $Rev: 51 $
 */
public class CommandContext
{
    public static final String COMMAND_REQUEST_PARAM_KEY = "command";
    private Command command;
    private String alias;
    private String result;

    public Command getCommand()
    {
        return command;
    }

    public void setCommand(Command command)
    {
        this.command = command;
    }

    public void setCommandAlias(String alias)
    {
        this.alias = alias;
    }

    public String getCommandAlias()
    {
        return alias;
    }

    public String getCommandResult()
    {
        return result;
    }

    public void setCommandResult(String result)
    {
        this.result = result;
    }
}
