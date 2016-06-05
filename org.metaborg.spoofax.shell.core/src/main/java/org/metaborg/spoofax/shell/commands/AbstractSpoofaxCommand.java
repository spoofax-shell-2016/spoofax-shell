package org.metaborg.spoofax.shell.commands;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.ISpoofaxResult;

import com.google.inject.Inject;

/**
 * Command for processing a String as an expression in some language.
 *
 * @param <A>
 *            The argument type of the {@link #execute(A)} method.
 * @param <R>
 *            The return type of the {@link #execute(A)} method.
 */
public abstract class AbstractSpoofaxCommand<A, R extends ISpoofaxResult<?>>
    implements IReplCommand<A, R> {
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
    public AbstractSpoofaxCommand(IResultFactory resultFactory, IProject project,
                                  ILanguageImpl lang) {
        this.resultFactory = resultFactory;
        this.project = project;
        this.lang = lang;
    }

    @Override
    public abstract R execute(A arg) throws MetaborgException;
}