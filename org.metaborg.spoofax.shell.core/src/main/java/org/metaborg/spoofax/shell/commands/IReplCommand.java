package org.metaborg.spoofax.shell.commands;

import org.metaborg.core.MetaborgException;
import org.metaborg.spoofax.shell.client.hooks.IHook;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;

/**
 * Interface for REPL commands. Used together with {@link ICommandInvoker}, instances of
 * implementors of this interface can be bound to names and descriptions.
 *
 * @param <A>
 *            The argument type of the {@link #execute(A)} method.
 */
public interface IReplCommand<A> {

    /**
     * @return The description of this command.
     */
    String description();

    /**
     * Execute this command.
     *
     * @param arg
     *            The arguments for this command.
     * @return An {@link IHook} to process the result of this command.
     * @throws MetaborgException
     *             When something goes wrong during execution.
     */
    IHook execute(A arg) throws MetaborgException;
}
