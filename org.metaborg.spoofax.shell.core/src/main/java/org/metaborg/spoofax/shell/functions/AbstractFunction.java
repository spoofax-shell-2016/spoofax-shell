package org.metaborg.spoofax.shell.functions;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.shell.commands.AbstractSpoofaxCommand;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.ISpoofaxResult;

import com.google.inject.Inject;

/**
 * Command for processing a String as an expression in some language.
 *
 * @param <A>
 *            The argument type of the {@link #execute(A)} method.
 * @param <R>
 */
public abstract class AbstractFunction<A extends ISpoofaxResult<?>, R extends ISpoofaxResult<?>> {
    protected final IResultFactory resultFactory;
    protected final IProject project;
    protected final ILanguageImpl lang;

    /**
     * Instantiate a {@link AbstractSpoofaxCommand}.
     *
     * @param resultFactory
     *            The {@link ResulFactory}.
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     */
    @Inject
    public AbstractFunction(IResultFactory resultFactory, IProject project, ILanguageImpl lang) {
        this.resultFactory = resultFactory;
        this.project = project;
        this.lang = lang;
    }

    public abstract R execute(A arg) throws MetaborgException;
}