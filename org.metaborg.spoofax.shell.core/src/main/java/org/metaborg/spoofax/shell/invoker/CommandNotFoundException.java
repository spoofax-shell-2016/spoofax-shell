package org.metaborg.spoofax.shell.invoker;

import org.metaborg.spoofax.shell.commands.IReplCommand;

/**
 * Thrown when the user requests an {@link IReplCommand} that could not be found.
 */
public class CommandNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String commandName;

    /**
     * Instantiate a new CommandNotFoundException.
     *
     * @param commandName
     *            The command name for which no {@link IReplCommand} could be found.
     */
    public CommandNotFoundException(String commandName) {
        this.commandName = commandName;
    }

    /**
     * @return The command name for which no {@link IReplCommand} could be found.
     */
    public String commandName() {
        return commandName;
    }

    @Override
    public String getMessage() {
        return "Command named \"" + commandName() + "\" was not found.";
    }
}
