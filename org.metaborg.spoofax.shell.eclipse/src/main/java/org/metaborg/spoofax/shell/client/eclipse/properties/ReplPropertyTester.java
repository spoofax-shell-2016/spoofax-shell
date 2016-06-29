package org.metaborg.spoofax.shell.client.eclipse.properties;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.metaborg.spoofax.shell.client.eclipse.ReplView;

/**
 * A {@link PropertyTester} to test properties relevant for the Spoofax Shell
 * REPL. It can test whether there is a REPL open (property {@code "isReplOpen"}).
 */
public class ReplPropertyTester extends PropertyTester {

    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        switch (property) {
        case "isReplOpen":
            return isReplOpen();
        default:
        }

        return false;
    }

    private boolean isReplOpen() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();

        return page.findView(ReplView.ID) != null;
    }

}
