package org.metaborg.spoofax.shell.client.eclipse.strategy;

import org.metaborg.spoofax.shell.client.console.strategy.ColorStrategy;
import org.metaborg.spoofax.shell.core.StyledText;

/**
 * A {@link ColorStategy} to translate {@link StyledText} to strings the Eclipse console
 * understands.
 */
public class EclipseStrategy implements ColorStrategy {

    @Override
    public String style(StyledText text) {
        return text.toString();
    }

}
