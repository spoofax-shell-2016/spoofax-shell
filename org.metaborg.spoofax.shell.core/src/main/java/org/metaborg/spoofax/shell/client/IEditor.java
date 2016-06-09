package org.metaborg.spoofax.shell.client;

import org.metaborg.core.completion.ICompletionService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;

/**
 * An {@link IEditor} is an object in which expressions in some language can be typed.
 * <p>
 * It takes care of key bindings, completions suggestions, the input history and multiline editing
 * capabilities. Its use is not mandatory (e.g. it is not implicitly required by anything bound in
 * {@link ReplModule} ), but considering all clients need some way of passing user input to the REPL
 * an interface is provided for convenience.
 */
public interface IEditor {

    /**
     * Get the input from the user, optionally spanning multiple lines.
     *
     * @return The input typed in by the user.
     */
    String getInput();

    /**
     * Set the completion service to be used when the user asks for completion suggestions.
     *
     * @param completionService
     *            The {@link ICompletionService} for providing completion suggestions.
     */
    void setSpoofaxCompletion(ICompletionService<ISpoofaxParseUnit> completionService);

    /**
     * Get the input history.
     *
     * @return The history of evaluated expressions, oldest entries first.
     */
    IInputHistory history();
}