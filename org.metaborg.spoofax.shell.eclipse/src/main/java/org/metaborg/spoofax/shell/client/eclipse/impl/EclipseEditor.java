package org.metaborg.spoofax.shell.client.eclipse.impl;

import java.awt.Color;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.metaborg.core.completion.ICompletionService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.shell.client.IEditor;
import org.metaborg.spoofax.shell.client.IInputHistory;
import org.metaborg.spoofax.shell.output.StyledText;

import com.google.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * An Eclipse-based implementation of {@link IEditor}. It uses a {@link Text} widget in singleline
 * mode and attaches itself as a {@link KeyListener} to listen for certain keypresses. When an Enter
 * (e.g. linefeed or carriage return) is pressed, the {@link Observer}s are notified with the text
 * typed so far.
 *
 * Upon a Shift-Enter, a newline is appended and the {@link Text} widget switches to multiline mode.
 * The widget then functions like a regular editor, in which Shift+Enter is to be used to append
 * newlines and Enter still notifies the {@link Observable}s.
 *
 * History is automatically maintained through {@link EclipseHistory}. The regular Eclipse
 * keybindings apply in the {@link Text} widget.
 */
public class EclipseEditor extends KeyAdapter implements IEditor {
    private final Text input;
    private Subscriber<? super IEditor> observer;
    private StyledText prompt;
    private StyledText continuationPrompt;

    /**
     * Instantiates a new EclipseEditor.
     *
     * @param parent
     *            A {@link Composite} control which will be the parent of this EclipseEditor.
     *            (cannot be {@code null}).
     */
    @Inject
    public EclipseEditor(Composite parent) {
        this.input = new Text(parent, SWT.SINGLE);
        this.input.addKeyListener(this);

        this.setPrompt(new StyledText(Color.GREEN, "==> "));
        this.setContinuationPrompt(new StyledText("... "));
    }

    private void clear() {
        this.input.setText("");
    }

    /**
     * Returns the {@link Control} backing this EclipseEditor.
     *
     * @return The {@link Control} backing this EclipseEditor.
     */
    public Control getControl() {
        return this.input;
    }

    /**
     * Creates a new {@link Observable} from this editor. The subscriber will be notified via the
     * {@link KeyListener} functions when some notable key presses (e.g. Enter to submit input)
     * occur.
     *
     * @return A new {@link Observable} from this editor.
     */
    public Observable<IEditor> asObservable() {
        // FIXME: Allow more than one observer of this editor instance.
        return Observable.create(s -> {
            EclipseEditor.this.observer = s;
        });
    }

    @Override
    public String getInput() {
        return this.input.getText();
    }

    @Override
    public void setPrompt(StyledText promptString) {
        this.prompt = promptString;
    }

    @Override
    public StyledText getPrompt() {
        return this.prompt;
    }

    @Override
    public void setContinuationPrompt(StyledText promptString) {
        this.continuationPrompt = promptString;
    };

    @Override
    public StyledText getContinuationPrompt() {
        return this.continuationPrompt;
    }

    @Override
    public void setSpoofaxCompletion(ICompletionService<ISpoofaxParseUnit> completionService) {
        // TODO Auto-generated method stub
    }

    @Override
    public IInputHistory history() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.keyCode == SWT.CR || event.keyCode == SWT.LF) {
            observer.onNext(this);
            clear();
        }
    }

}
