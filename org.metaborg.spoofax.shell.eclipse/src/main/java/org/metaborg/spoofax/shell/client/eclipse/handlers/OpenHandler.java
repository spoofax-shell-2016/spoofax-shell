package org.metaborg.spoofax.shell.client.eclipse.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.metaborg.spoofax.eclipse.util.AbstractHandlerUtils;
import org.metaborg.spoofax.shell.client.eclipse.ReplView;
import org.metaborg.spoofax.shell.client.eclipse.impl.EclipseRepl;

/**
 * An {@link IHandler} implementation for the "Open in Spoofax REPL" command.
 * This handler will open the files that were selected when this command got
 * activated in the current REPL session.
 */
public class OpenHandler extends AbstractHandler {

    // FIXME: this should check whether the files that are to be opened use the
    // same language as is currently open in this REPL session, although this is
    // sort of enforced by the evaluation that won't work.

    // FIXME: the only situation where this goes wrong still is when a user tries
    // to open a file in an already open REPL that hasn't yet loaded a language.
    // This cannot be fixed, as it is currently not possible for a frontend to
    // see if a language has been loaded.

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();

        ReplView view = (ReplView) page.findView(ReplView.ID);
        if (view != null) {
            Iterable<IFile> files = AbstractHandlerUtils.toFiles(event);
            EclipseRepl repl = view.getRepl();
            files.forEach(file -> {
                repl.onNext(String.format(":open %s", file.getLocationURI().toString()));
            });
        }

        // Must return null.
        return null;
    }

}
