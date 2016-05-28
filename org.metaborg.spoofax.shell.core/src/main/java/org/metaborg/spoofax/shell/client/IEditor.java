package org.metaborg.spoofax.shell.client;

import org.metaborg.core.completion.ICompletionService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.shell.output.StyledText;

/**
 * An {@link IEditor} is where expressions in some language can be typed. It takes care of
 * keybindings, offering completions, the input history and multiline editing capabilities.
 */
public interface IEditor {

    /**
     * Get the input from the user, optionally spanning multiple lines.
     *
     * @return The input typed in by the user.
     */
    String getInput();

    /**
     * Set the prompt to display.
     *
     * @param promptString
     *            The prompt string.
     */
    void setPrompt(StyledText promptString);

    /**
     * Returns the prompt used by this EclipseEditor.
     *
     * @return The prompt used by this EclipseEditor.
     */
    StyledText getPrompt();

    /**
     * Set the prompt to display when in multiline mode.
     *
     * @param promptString
     *            The prompt string.
     */
    void setContinuationPrompt(StyledText promptString);

    /**
     * Returns the continuation prompt used by this EclipseEditor.
     *
     * @return The continuation prompt used by this EclipseEditor.
     */
    StyledText getContinuationPrompt();

    /**
     * Set the completion service to be used when hitting TAB.
     *
     * @param completionService
     *            The {@link ICompletionService} for providing completion.
     */
    void setSpoofaxCompletion(ICompletionService<ISpoofaxParseUnit> completionService);

    /**
     * @return The history of evaluated expressions, oldest entries first.
     */
    IInputHistory history();
}