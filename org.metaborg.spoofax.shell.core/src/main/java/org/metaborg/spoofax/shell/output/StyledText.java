package org.metaborg.spoofax.shell.output;

import java.awt.Color;
import java.util.List;
import java.util.stream.IntStream;

import org.metaborg.core.source.ISourceRegion;
import org.metaborg.core.source.SourceRegion;
import org.metaborg.core.style.IRegionStyle;
import org.metaborg.core.style.IStyle;
import org.metaborg.core.style.RegionStyle;
import org.metaborg.core.style.Style;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.common.collect.Lists;

/**
 * Represents a styled text containing several styled strings, each represented by an
 * {@link IRegionStyle}.
 */
public class StyledText {
    private final List<IRegionStyle<IStrategoTerm>> source;
    private StringBuffer textBuffer;

    /**
     * Create a styled text from a string with no style.
     *
     * @param text
     *            The unstyled text.
     */
    public StyledText(String text) {
        this(defaultStyle(), text);
    }

    /**
     * Create a styled text from a colored string.
     *
     * @param color
     *            The color to apply.
     * @param text
     *            The unstyled text.
     */
    public StyledText(Color color, String text) {
        this(colorStyle(color, null), text);
    }

    /**
     * Create a styled text from a string with a style.
     *
     * @param style
     *            the style
     * @param text
     *            The unstyled text.
     */
    public StyledText(IStyle style, String text) {
        this.source = Lists.newArrayList();
        this.textBuffer = new StringBuffer();

        this.append(style, text);
    }

    /**
     * Create a styled text from a list of styled Stratego terms.
     *
     * @param sourceRegions
     *            The list of styled Stratego terms.
     */
    public StyledText(Iterable<IRegionStyle<IStrategoTerm>> sourceRegions) {
        this.source = Lists.newArrayList(sourceRegions);
    }

    /**
     * Return all the styled strings in this text.
     *
     * @return All the styled strings in this text.
     */
    public List<IRegionStyle<IStrategoTerm>> getSource() {
        return source;
    }

    /**
     * Append a string with no style to this styled text.
     *
     * @param text
     *            The unstyled text.
     * @return The styled text.
     */
    public StyledText append(String text) {
        return this.append(defaultStyle(), text);
    }

    /**
     * Append a colored string to this styled text.
     *
     * @param color
     *            The color to apply.
     * @param text
     *            The unstyled text.
     * @return The styled text.
     */
    public StyledText append(Color color, String text) {
        return this.append(colorStyle(color, null), text);
    }

    /**
     * Append a string with an arbitrary style to this styled text.
     *
     * @param style
     *            The style to apply.
     * @param text
     *            The unstyled text.
     * @return The styled text.
     */
    public StyledText append(IStyle style, String text) {
        int start = this.textBuffer.length();
        return this.append(new SourceRegion(start, start + text.length() - 1), style, text);
    }

    /**
     * Append a string with a region and a style to this styled text.
     *
     * @param region
     *            The region to style.
     * @param style
     *            The style to apply.
     * @param text
     *            The unstyled text.
     * @return The styled text.
     */
    private StyledText append(ISourceRegion region, IStyle style, String text) {
        this.source.add(new RegionStyle<>(region, style, null));
        this.textBuffer.append(text);

        return this;
    }

    private static IStyle defaultStyle() {
        return colorStyle(null, null);
    }

    private static IStyle colorStyle(Color fg, Color bg) {
        return new Style(fg, bg, false, false, false);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StyledText)) {
            return false;
        }

        StyledText other = (StyledText) obj;
        if (source.size() != other.source.size()) {
            return false;
        }

        return IntStream.range(0, source.size())
            .mapToObj(e -> {
                IRegionStyle<IStrategoTerm> s = source.get(e);
                IRegionStyle<IStrategoTerm> o = other.source.get(e);

                return s.fragment().equals(o.fragment())
                        && (s.style() == o.style()
                            || s.style() != null && s.style().equals(o.style())
                            || o.style() != null && o.style().equals(s.style()));
            })
            .allMatch(e -> e);
    }

    @Override
    public int hashCode() {
        return source.stream()
                .map(e -> e.fragment().hashCode() * e.style().hashCode())
                .reduce(1, (a, b) -> a * b);
    }

    @Override
    public String toString() {
        return textBuffer.toString();
    }
}
