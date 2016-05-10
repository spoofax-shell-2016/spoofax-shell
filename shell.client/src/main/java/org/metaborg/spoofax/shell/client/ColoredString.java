package org.metaborg.spoofax.shell.client;

import org.fusesource.jansi.Ansi.Color;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;;

/**
 * One part of a colored string.
 */
class ColoredStringImpl {
    private static java.awt.Color[] colormap = {
        java.awt.Color.BLACK,
        java.awt.Color.RED,
        java.awt.Color.GREEN,
        java.awt.Color.YELLOW,
        java.awt.Color.BLUE,
        java.awt.Color.ORANGE,
        java.awt.Color.CYAN,
        java.awt.Color.WHITE,
        java.awt.Color.WHITE
    };

    private Color color;
    private String string;

    /**
     * Construct a colored string.
     * @param color the color
     * @param string the string
     */
    ColoredStringImpl(Color color, String string) {
        this.color = color;
        this.string = string;
    }

    /**
     * Returns the color of the string.
     * @return the color
     */
    public java.awt.Color getColor() {
        return colormap[this.color.value()];
    }

    /**
     * Returns the ansi color of the string.
     * @return ansi color
     */
    public Color getAnsiColor() {
        return this.color;
    }

    /**
     * Returns the string.
     * @return the string
     */
    public String getString() {
        return string;
    }
}

/**
 * Represents a string that can be colored.
 */
public class ColoredString {
    private List<ColoredStringImpl> coloredStrings;

    /**
     * Constructor for a string with default color.
     * @param string the string
     */
    public ColoredString(String string) {
        this(Color.WHITE, string);
    }

    /**
     * Constructor for a string with a color.
     * @param color  the color
     * @param string the string
     */
    public ColoredString(Color color, String string) {
        coloredStrings = Lists.newArrayList();
        coloredStrings.add(new ColoredStringImpl(color, string));
    }

    /**
     * Return all substrings.
     * @return list of strings
     */
    public List<ColoredStringImpl> getStrings() {
        return coloredStrings;
    }

    /**
     * Append another string.
     * @param string another string
     * @return this string
     */
    private ColoredString append(ColoredStringImpl string) {
        coloredStrings.add(string);
        return this;
    }

    /**
     * Append another string.
     * @param color the color
     * @param string another string
     * @return this string
     */
    public ColoredString append(Color color, String string) {
        return this.append(new ColoredStringImpl(color, string));
    }

    /**
     * Append another string.
     * @param string another string
     * @return this string
     */
    public ColoredString append(String string) {
        return this.append(Color.WHITE, string);
    }

    @Override
    public String toString() {
        return coloredStrings.stream()
        .map(e -> e.getString())
        .collect(Collectors.joining());
    }
}
