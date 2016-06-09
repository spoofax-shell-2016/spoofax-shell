package org.metaborg.spoofax.shell.invoker;

import java.util.Arrays;
import java.util.Map;

import org.metaborg.core.MetaborgException;
import org.metaborg.spoofax.shell.client.IHook;
import org.metaborg.spoofax.shell.commands.IReplCommand;

/**
 * An interface for binding (to user-facing names) and executing {@link IReplCommand}s.
 */
public interface ICommandInvoker {

    /**
     * Return the {@link IReplCommand} bound to {@code commandName}.
     *
     * @param commandName
     *            The name of the {@link IReplCommand} to find.
     * @return The {@link IReplCommand} bound to {@code commandName}.
     * @throws CommandNotFoundException
     *             When the command could not be found.
     */
    IReplCommand commandFromName(String commandName) throws CommandNotFoundException;

    /**
     * The prefix required for the user to "invoke" commands, e.g. {@code :}.
     *
     * @return The prefix of the {@link IReplCommand commands}. The {@link IReplCommand commands}
     *         are stored without this prefix.
     */
    String commandPrefix();

    /**
     * Execute the {@link IReplCommand} which is bound to the given command name, minus the prefix.
     *
     * @param optionallyPrefixedCommandName
     *            The name of the {@link IReplCommand} to be executed.
     * @return An {@link IHook} to process the result of the executed command.
     * @throws CommandNotFoundException
     *             When the command could not be found.
     * @throws MetaborgException
     *             When something goes wrong during execution.
     * @see IReplCommand#execute(String...)
     */
    default IHook execute(String optionallyPrefixedCommandName)
        throws CommandNotFoundException, MetaborgException {
        if (optionallyPrefixedCommandName.startsWith(commandPrefix())) {
            String[] split = optionallyPrefixedCommandName.split("\\s+", 2);
            String commandName = split[0].substring(commandPrefix().length());
            String[] argument =
                split.length > 1 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];
            return commandFromName(commandName).execute(argument);
        } else {
            // FIXME: create sensible way to set default
            return commandFromName("eval").execute(optionallyPrefixedCommandName);
        }
    }

    /**
     * Add a command to the list of available commands.
     *
     * @param name
     *            The name to bind the {@link IReplCommand} to.
     * @param command
     *            The {@link IReplCommand} to add to the list.
     */
    void addCommand(String name, IReplCommand command);

    /**
     * Get the command factory used to create {@link IReplCommand commands}.
     *
     * @return An {@link ICommandFactory}.
     */
    ICommandFactory getCommandFactory();

    /**
     * Get a list of all available {@link IReplCommand commands}.
     *
     * @return A {@link Map} from command name to {@link IReplCommand}.
     */
    Map<String, IReplCommand> getCommands();

    /**
     * Reset the list of available commands to its initial value.
     */
    void resetCommands();
}
