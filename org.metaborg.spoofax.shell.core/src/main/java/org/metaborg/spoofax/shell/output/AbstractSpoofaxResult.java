package org.metaborg.spoofax.shell.output;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.unit.IUnit;
import org.metaborg.spoofax.core.stratego.IStrategoCommon;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * An abstract base class of {@link ISpoofaxResult}.
 *
 * @param <T>
 *            The wrapped subclass of {@link IUnit}.
 */
public abstract class AbstractSpoofaxResult<T extends IUnit> implements ISpoofaxResult<IUnit> {
    private final IStrategoCommon common;
    private final T unit;

    /**
     * Instantiate a new AbstractSpoofaxResult.
     *
     * @param common
     *            The {@link IStrategoCommon} service, used to pretty print {@link IStrategoTerm}s.
     * @param unit
     *            The wrapped {@link IUnit}.
     */
    public AbstractSpoofaxResult(IStrategoCommon common, T unit) {
        this.common = common;
        this.unit = unit;
    }

    @Override
    public FileObject source() {
        return unit.source();
    }

    @Override
    public T unit() {
        return unit;
    }

    /**
     * Return a textual, pretty printed representation of an {@link IStrategoTerm}.
     *
     * @param term
     *            The term to pretty print.
     * @return The pretty printed textual representation.
     */
    public StyledText toString(IStrategoTerm term) {
        return new StyledText(common.toString(term));
    }
}
