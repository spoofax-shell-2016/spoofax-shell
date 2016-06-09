package org.metaborg.spoofax.shell.output;

import java.util.List;
import java.util.Optional;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.context.IContext;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.unit.IUnit;
import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.commands.AbstractSpoofaxCommand;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Results of {@link AbstractSpoofaxCommand#execute(String...)} are wrapped in an
 * {@link ISpoofaxResult}. This is done so that each command has the same transparent return type
 * for the client to process in {@link IDisplay#displayResult(ISpoofaxResult)}.
 *
 * @param <T>
 *            The wrapped subclass of {@link IUnit}.
 */
public interface ISpoofaxResult<T extends IUnit> {
    /**
     * Return the AST of this unit as a {@link IStrategoTerm}, if present.
     *
     * @return An {@link Optional} of a {@link IStrategoTerm}.
     */
    Optional<IStrategoTerm> ast();

    /**
     * Return the {@link IContext} in which the unit was generated, if present.
     *
     * @return An {@link Optional} of an {@link IContext}.
     */
    Optional<IContext> context();

    /**
     * Return a list of {@link IMessage} for all contained units.
     *
     * @return A list of {@link IMessage}.
     */
    List<IMessage> messages();

    /**
     * Return the source {@link FileObject} for this unit.
     *
     * @return The source {@link FileObject} for this unit.
     */
    FileObject source();

    /**
     * Return the result of this unit.
     *
     * @return The result.
     */
    StyledText styled();

    /**
     * Return the wrapped {@link IUnit} subclass.
     *
     * @return The wrapped unit.
     */
    T unit();

    /**
     * Return a boolean indicating whether the result is valid.
     *
     * @return {@code true} iff this result is valid.
     */
    boolean valid();
}
