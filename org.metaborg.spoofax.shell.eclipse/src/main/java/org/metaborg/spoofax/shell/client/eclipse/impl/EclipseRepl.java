package org.metaborg.spoofax.shell.client.eclipse.impl;

import java.awt.Color;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.UIJob;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.style.Style;
import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.client.IHook;
import org.metaborg.spoofax.shell.client.IRepl;
import org.metaborg.spoofax.shell.invoker.CommandNotFoundException;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;
import org.metaborg.spoofax.shell.output.StyledText;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import rx.Observer;

/**
 * An Eclipse-based implementation of {@link IRepl}.
 *
 * It uses a multiline input editor with keyboard shortcuts, including persistent history, syntax
 * highlighting and error marking.
 *
 * Note that this class evaluates input in a separate thread.
 */
public class EclipseRepl implements IRepl, Observer<String> {
    private static final int INPUT_RED = 232;
    private static final int INPUT_GREEN = 242;
    private static final int INPUT_BLUE = 254;
    private final IDisplay display;
    private final ICommandInvoker invoker;

    /**
     * Instantiates a new EclipseRepl.
     *
     * @param invoker
     *            The {@link ICommandInvoker} for executing user input.
     * @param display
     *            The {@link EclipseDisplay} to send results to.
     */
    @AssistedInject
    public EclipseRepl(ICommandInvoker invoker, @Assisted IDisplay display) {
        this.display = display;
        this.invoker = invoker;
    }

    @Override
    public ICommandInvoker getInvoker() {
        return this.invoker;
    }

    @Override
    public void onCompleted() {
        // We don't ever call onCompleted ourselves, so if it's called it is unexpectedly and
        // probably an error somewhere. The pipeline cannot be restored, either.
        System.err
            .println("The observer/observable pipeline has completed unexpectedly."
                     + "There is nothing more to do, try restarting the REPL.");
    }

    @Override
    public void onError(Throwable t) {
        // Do not display this to the user, as it is an internal exception.
        t.printStackTrace();
    }

    @Override
    public void onNext(String input) {
        appendInputToDisplay(input);
        runAsJob(input);
    }

    private void appendInputToDisplay(String input) {
        // TODO: Style input! Output cannot be styled since there is no way to "pretty-prettyprint"
        // it back to a format of the language currently being used. As such, it cannot be
        // highlighted.
        Color inputBackgroundColor = new Color(INPUT_RED, INPUT_GREEN, INPUT_BLUE);
        Style style = new Style(null, inputBackgroundColor, false, false, false);
        // FIXME: Input is not really a "message"...
        this.display.displayMessage(new StyledText(style, input));
    }

    private void runAsJob(final String input) {
        Job job = new Job("Spoofax REPL evaluation job") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    runAsUIJob(eval(input));
                    return Status.OK_STATUS;
                } catch (MetaborgException | CommandNotFoundException e) {
                    StyledText message = new StyledText(Color.RED, e.getMessage());
                    runAsUIJob((display) -> display.displayMessage(message));
                    return Status.CANCEL_STATUS;
                }
            }
        };
        job.setSystem(true);
        job.schedule();
    }

    private void runAsUIJob(IHook hook) {
        Job job = new UIJob("Spoofax REPL display job") {
            @Override
            public IStatus runInUIThread(IProgressMonitor arg0) {
                hook.accept(display);
                return Status.OK_STATUS;
            }
        };
        job.setPriority(Job.SHORT);
        job.setSystem(true);
        job.schedule();
    }

}
