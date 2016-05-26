package org.metaborg.spoofax.shell.client.console.strategy;

import org.metaborg.spoofax.shell.core.StyledText;

/**
 * The main interface for the coloring strategy, according to the strategy design pattern. Different
 * console environment use different means of coloring (e.g. some terminals support ANSI, others
 * don't).
 */
public interface ColorStrategy {
    /**
     * Convert the {@link StyledText} into a color-coded string understood by the console.
     *
     * @param text
     *            The {@link StyledText} to translate.
     * @return A color-coded string understood by the console.
     */
    String style(StyledText text);
}
