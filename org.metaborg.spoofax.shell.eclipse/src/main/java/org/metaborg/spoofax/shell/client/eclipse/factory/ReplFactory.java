package org.metaborg.spoofax.shell.client.eclipse.factory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.metaborg.spoofax.shell.client.eclipse.ReplConsole;

/**
 * An {@link IConsoleFactory} to provide Spoofax REPLs.
 */
public class ReplFactory implements IConsoleFactory {
    private final IConsoleManager consoleManager;

    /**
     * Instantiates a new ReplFactory. Note that this method is called automatically by Eclipse, so
     * you do not need to instantiate a ReplFactory yourself.
     */
    public ReplFactory() {
        this.consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    }

    @Override
    public void openConsole() {
        final ReplConsole console = new ReplConsole();
        consoleManager.addConsoles(new IConsole[] { console });
        consoleManager.showConsoleView(console);
        Job job = new Job("REPL") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                console.run();
                return Status.OK_STATUS;
            }
        };
        job.setSystem(true);
        job.setPriority(Job.SHORT);
        job.schedule();
    }

}
