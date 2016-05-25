package org.metaborg.spoofax.shell.client.console.strategy;

import org.fusesource.jansi.Ansi;
import org.metaborg.spoofax.shell.client.console.AnsiColors;
import org.metaborg.spoofax.shell.core.StyledText;

/**
 * A {@link ColorStategy} to translate {@link StyledText} to ANSI color codes.
 */
public class AnsiStrategy implements ColorStrategy {

    @Override
    public String style(StyledText text) {
        Ansi ansi = Ansi.ansi();
        text.getSource().stream().forEach(e -> {
            if (e.style() != null && e.style().color() != null) {
                ansi.fg(AnsiColors.findClosest(e.style().color())).a(e.fragment()).reset();
            } else {
                ansi.a(e.fragment());
            }
        });
        return ansi.toString();
    }

}
