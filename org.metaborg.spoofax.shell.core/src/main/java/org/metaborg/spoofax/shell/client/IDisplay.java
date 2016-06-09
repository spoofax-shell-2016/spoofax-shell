package org.metaborg.spoofax.shell.client;

import org.metaborg.spoofax.shell.commands.IReplCommand;
import org.metaborg.spoofax.shell.output.ISpoofaxResult;
import org.metaborg.spoofax.shell.output.StyledText;

/**
 * An {@link IDisplay} is accepting and processing the results of a {@link IReplCommand command}.
 * Clients are free to process and display these results however they require.
 */
public interface IDisplay {

    /**
     * Process an {@link ISpoofaxResult}.
     *
     * @param result
     *            The result to be processed.
     */
    void displayResult(ISpoofaxResult<?> result);

    /**
     * Process a {@link StyledText} message.
     *
     * @param message
     *            The message to be processed.
     */
    void displayMessage(StyledText message);

}
