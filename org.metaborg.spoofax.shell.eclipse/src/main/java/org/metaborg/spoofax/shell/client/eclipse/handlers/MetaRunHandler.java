package org.metaborg.spoofax.shell.client.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.metaborg.spoofax.eclipse.util.AbstractHandlerUtils;

/**
 * An {@link IHandler} implementation for the "Run in REPL" Spoofax (meta) menu. This handler will
 * build the language currently open in Spoofax and will then launch a REPL session with said
 * language.
 */
public class MetaRunHandler extends AbstractHandler {

    // TODO: how does this deal with a REPL already open? We should probably throw up a dialog
    // asking the user for confirmation to close the current session and then open a new one.
    // (Remember that the REPL is currently a singleton.)

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        // IWorkbenchPage page = window.getActivePage();

        try {
            // ReplView view = (ReplView) page.showView(ReplView.ID);
            // EclipseRepl repl = view.getRepl();
            buildLanguage(event);
            // Naive but simple way to load a new language.
            // repl.onNext(":load " + buildLanguage(event));
        } catch (CoreException e) {
            e.printStackTrace();
        }

        // Must always return null.
        return null;
    }

    // TODO: use "eclipse://"?
    // TODO: build in background thread, monitor progress to user.
    private String buildLanguage(ExecutionEvent event) throws CoreException {
        // We can assume this is a Spoofax language project, as the "Run in REPL" menu item
        // only shows on projects that have the Spoofax meta nature (of course, one can manually add
        // said nature to a Java project, but hey, if you want to break things you get to keep the
        // pieces).
        IProject project = AbstractHandlerUtils.toProject(event);
        if (project == null) {
            return null;
        }

        project.build(IncrementalProjectBuilder.FULL_BUILD, null);
        String res = project.getLocation().toOSString();
        System.out.println("Project I built and am now loading: " + res);
        return res;
    }

}
