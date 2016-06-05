package org.metaborg.spoofax.shell.commands;

import org.metaborg.core.MetaborgException;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;

/**
 * Interface for REPL commands. Used together with {@link ICommandInvoker}, instances of
 * implementors of this interface can be bound to names and descriptions.
 *
 * @param <A>
 *            The argument type of the {@link #execute(A)} method.
 * @param <R>
 *            The return type of the {@link #execute(A)} method.
 */
public interface IReplCommand<A, R> {

    /**
     * @return The description of this command.
     */
    String description();

    /**
     * Execute this command.
     *
     * @param arg
     *            The arguments for this command.
     * @return The result of this command.
     * @throws MetaborgException
     *             When something goes wrong during execution.
     */
    R execute(A arg) throws MetaborgException;
}
