package org.metaborg.spoofax.shell.invoker;

import org.metaborg.core.action.ITransformAction;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.shell.commands.AbstractSpoofaxCommand;
import org.metaborg.spoofax.shell.commands.AnalyzeCommand;
import org.metaborg.spoofax.shell.commands.AnalyzedTransformCommand;
import org.metaborg.spoofax.shell.commands.ParseCommand;
import org.metaborg.spoofax.shell.commands.ParsedTransformCommand;

/**
 * Factory interface for creating {@link AbstractSpoofaxCommand}s by Guice.
 */
public interface ICommandFactory {
    /**
     * Factory method for creating a {@link ParseCommand}.
     *
     * @param project
     *            The associated {@link IProject}.
     * @param lang
     *            The associated {@link ILanguageImpl}.
     * @return A {@link ParseCommand}
     */
    ParseCommand createParse(IProject project, ILanguageImpl lang);

    /**
     * Factory method for creating an {@link AnalyzeCommand}.
     *
     * @param project
     *            The associated {@link IProject}.
     * @param lang
     *            The associated {@link ILanguageImpl}.
     * @return An {@link AnalyzeCommand}
     */
    AnalyzeCommand createAnalyze(IProject project, ILanguageImpl lang);

    /**
     * Factory method for creating a {@link ParsedTransformCommand}.
     *
     * @param project
     *            The associated {@link IProject}.
     * @param lang
     *            The associated {@link ILanguageImpl}.
     * @param action
     *            The {@link ITransformAction} that this command executes.
     * @return A {@link ParsedTransformCommand}.
     */
    ParsedTransformCommand createParsedTransform(IProject project, ILanguageImpl lang,
                                                 ITransformAction action);

    /**
     * Factory method for creating an {@link AnalyzedTransformCommand}.
     *
     * @param project
     *            The associated {@link IProject}.
     * @param lang
     *            The associated {@link ILanguageImpl}.
     * @param action
     *            The {@link ITransformAction} that this command executes.
     * @return A {@link AnalyzedTransformCommand}.
     */
    AnalyzedTransformCommand createAnalyzedTransform(IProject project, ILanguageImpl lang,
                                                     ITransformAction action);
}
