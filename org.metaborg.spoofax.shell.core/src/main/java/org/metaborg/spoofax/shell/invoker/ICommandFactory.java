package org.metaborg.spoofax.shell.invoker;

import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.shell.commands.AbstractSpoofaxCommand;
import org.metaborg.spoofax.shell.commands.AnalyzeCommand;
import org.metaborg.spoofax.shell.commands.EvaluateCommand;
import org.metaborg.spoofax.shell.commands.ParseCommand;
import org.metaborg.spoofax.shell.commands.TransformCommand;

/**
 * Factory for creating {@link AbstractSpoofaxCommand Spoofax commands}.
 */
public interface ICommandFactory {
    /**
     * Factory method for creating a {@link ParseCommand}.
     *
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     * @return A {@link ParseCommand}.
     */
    ParseCommand createParse(IProject project, ILanguageImpl lang);

    /**
     * Factory method for creating an {@link AnalyzeCommand}.
     *
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     * @return An {@link AnalyzeCommand}.
     */
    AnalyzeCommand createAnalyze(IProject project, ILanguageImpl lang);

    /**
     * Factory method for creating a {@link TransformCommand}.
     *
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     * @param analyzed
     *            Whether this language should be analyzed or not.
     * @return An {@link TransformCommand}.
     */
    TransformCommand createTransform(IProject project, ILanguageImpl lang, boolean analyzed);

    /**
     * Factory method for creating an {@link EvaluateCommand}.
     *
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     * @param analyzed
     *            Whether this language should be analyzed or not.
     * @return An {@link EvaluateCommand}.
     */
    EvaluateCommand createEvaluate(IProject project, ILanguageImpl lang, boolean analyzed);
}
