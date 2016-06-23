package org.metaborg.spoofax.shell.client.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.metaborg.spoofax.eclipse.util.AbstractHandlerUtils;
import org.metaborg.spoofax.shell.client.eclipse.ReplView;
import org.metaborg.spoofax.shell.client.eclipse.impl.EclipseRepl;

/**
 * An {@link IHandler} implementation for the "Run in REPL" Spoofax (meta) menu. This handler will
 * run the project currently open in Spoofax Eclipse in a REPL session. It is up to the language
 * designer to build the project before running it.
 */
public class MetaRunHandler extends AbstractHandler {

    // TODO: either always build the language or ask the user, but the option to build the language
    // should be here!

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
        Shell shell = window.getShell();

        if (replaceRunningRepl(page, shell)) {
            openReplSession(page, shell, event);
        }

        // Must always return null.
        return null;
    }

    private boolean replaceRunningRepl(IWorkbenchPage page, Shell shell) {
        if (page.findView(ReplView.ID) != null) {
            return MessageDialog.openConfirm(shell, "Only one REPL session can run at a time",
                                             "Continuing will replace the current REPL session");
        }
        return true;
    }

    private void openReplSession(IWorkbenchPage page, Shell shell, ExecutionEvent event) {
        try {
            ReplView view = (ReplView) page.showView(ReplView.ID);
            EclipseRepl repl = view.getRepl();

            // Naive but simple way to load a new language.
            String language = getLanguage(event);
            if (language != null) {
                repl.onNext(":load " + language);
            } else {
                MessageDialog.openError(shell, "Something went wrong",
                                        "Could not find the language implementation project");
            }
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    // TODO: use "eclipse://" instead of absolute paths?
    // TODO: be able to handle more selections, such that the user is not required to click on the
    // project in the project explorer first when using the menu contribution.
    private String getLanguage(ExecutionEvent event) {
        // We can assume this is a Spoofax language project, as the "Run in REPL" menu item
        // only shows on projects that have the Spoofax meta nature (of course, one can manually add
        // said nature to a Java project, but hey, if you want to break things you get to keep the
        // pieces).
        IProject project = AbstractHandlerUtils.toProject(event);
        if (project == null) {
            return null;
        }
        return project.getLocation().toOSString();
    }

}