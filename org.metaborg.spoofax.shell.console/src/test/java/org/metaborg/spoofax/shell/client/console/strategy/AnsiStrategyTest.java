package org.metaborg.spoofax.shell.client.console.strategy;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.metaborg.core.style.Style;
import org.metaborg.spoofax.shell.core.StyledText;

/**
 * Tests {@link AnsiStrategy}.
 */
@RunWith(Parameterized.class)
public class AnsiStrategyTest {
    private final AnsiStrategy strategy;
    private final String expected;
    private final StyledText input;

    /**
     * Creates input for parameterized test cases.
     *
     * @return An array of parameterized input.
     */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            // @formatter:off
            { "Regular string", new StyledText("Regular string") },
            { "[34mBlue foreground[m", new StyledText(Color.BLUE, "Blue foreground") },
            // Background colors, old, italics and underscore are not translated.
            { "[33mYellow on Green bold italic underscore[m",
              new StyledText(new Style(Color.YELLOW, Color.GREEN, true, true, true),
                             "Yellow on Green bold italic underscore") }
            // TODO: StyledText(Iterable<IRegionStyle<StrategoString>> sourceRegions);
            // @formatter:on
        });
    }

    /**
     * Instantiates a parameterized instance of this test.
     *
     * @param expected
     *            The expected ANSI color-coded output.
     * @param input
     *            The input {@link StyledText} to translate.
     */
    public AnsiStrategyTest(String expected, StyledText input) {
        this.strategy = new AnsiStrategy();
        this.expected = expected;
        this.input = input;
    }

    /**
     * Run the actual test.
     */
    @Test
    public void test() {
        assertEquals(expected, strategy.style(input));
        // org.fusesource.jansi.Ansi keeps state, so a new one needs to be instantiated for
        // every translation. This test ensures that this is caught.
        assertEquals(expected, strategy.style(input));
    }

}
