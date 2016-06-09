package org.metaborg.spoofax.shell.commands;

import org.metaborg.core.MetaborgException;
import org.metaborg.spoofax.shell.client.IHook;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;

/**
 * This is an interface for REPL commands: the REPL uses the command pattern to process user input.
 * <p>
 * Implementations of this interface are bound to names in
 * {@link ReplModule#bindCommands(MapBinder<String, IReplCommand>) ReplModule}. The
 * {@link ICommandInvoker} then processes the input, finds the command and calls its
 * {@link #execute(String... args) execute} method. The {@link #description()} method is used in the
 * {@link HelpCommand}.
 */
public interface IReplCommand {

    /**
     * Get the description of this command. The description is used in the {@link HelpCommand}.
     *
     * @return The description of this command.
     */
    String description();

    /**
     * Execute this command.
     *
     * @param args
     *            The arguments for this command.
     * @return An {@link IHook} to process the result of this command.
     * @throws MetaborgException
     *             When something goes wrong during execution.
     */
    IHook execute(String... args) throws MetaborgException;
}
