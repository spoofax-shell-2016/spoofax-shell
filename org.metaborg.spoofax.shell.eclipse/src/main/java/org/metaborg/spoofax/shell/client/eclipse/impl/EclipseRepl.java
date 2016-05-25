package org.metaborg.spoofax.shell.client.eclipse.impl;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.metaborg.core.MetaborgException;
import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.client.IEditor;
import org.metaborg.spoofax.shell.core.IRepl;
import org.metaborg.spoofax.shell.invoker.CommandNotFoundException;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;
import org.metaborg.spoofax.shell.output.StyledText;

import com.google.inject.Inject;

/**
 * An Eclipse-based REPL.
 *
 * It uses a multiline input editor with keyboard shortcuts, including persistent history, syntax
 * highlighting and error marking.
 */
public class EclipseRepl implements IRepl, Observer {
    private final IDisplay display;
    private final ICommandInvoker invoker;

    /**
     * Instantiates a new EclipseRepl.
     *
     * @param display
     *            The {@link IDisplay} to print results to.
     * @param invoker
     *            The {@link ICommandInvoker} for executing user input.
     */
    @Inject
    public EclipseRepl(IDisplay display, ICommandInvoker invoker) {
        this.display = display;
        this.invoker = invoker;
    }

    @Override
    public ICommandInvoker getInvoker() {
        return this.invoker;
    }

    @Override
    public void update(Observable observable, Object arg) {
        IEditor editor = (IEditor) observable;
        String input = editor.getInput();
        appendInputToDisplay(editor, input);
        runAsJob(input);
    }

    private void appendInputToDisplay(IEditor editor, String input) {
        // TODO: handle multiline input: every string after '\n' should be prepended with the
        // continuationPrompt.
        display.displayResult(new StyledText(editor.getPrompt() + " " + input));
    }

    private void runAsJob(final String input) {
        Job job = new Job("Spoofax REPL") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    eval(input);
                    return Status.OK_STATUS;
                } catch (MetaborgException | CommandNotFoundException e) {
                    display.displayError(new StyledText(Color.RED, e.getMessage()));
                    return Status.CANCEL_STATUS;
                }
            }
        };
        job.setSystem(true);
        job.schedule();
    }
}