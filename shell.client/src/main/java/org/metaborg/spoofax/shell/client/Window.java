package org.metaborg.spoofax.shell.client;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Represents a window.
 */
public class Window extends JFrame implements IDisplay {
    private static final long serialVersionUID = -7612408705090751318L;
    private JTextPane textPane;
    private Style style;
    private StyledDocument doc;

    /**
     * Creates a window.
     */
    public Window() {
        textPane = new JTextPane();

        this.setLayout(new FlowLayout());
        this.add(textPane);
        this.setSize(500, 500);
        this.setVisible(true);

        doc = textPane.getStyledDocument();
        style = textPane.addStyle("style", null);
    }
    @Override
    public void displayResult(ColoredString s) {
        s.getStrings().forEach(e -> {
            StyleConstants.setForeground(style, e.getColor());
            try {
                doc.insertString(doc.getLength(), e.getString(), style);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    @Override
    public void displayError(String s) {
        // TODO Auto-generated method stub
    }
}
