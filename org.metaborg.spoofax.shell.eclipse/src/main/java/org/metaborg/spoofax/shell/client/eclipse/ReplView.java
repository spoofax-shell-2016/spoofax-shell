package org.metaborg.spoofax.shell.client.eclipse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.metaborg.spoofax.shell.client.eclipse.impl.EclipseDisplay;
import org.metaborg.spoofax.shell.client.eclipse.impl.EclipseEditor;
import org.metaborg.spoofax.shell.client.eclipse.impl.EclipseRepl;
import org.metaborg.spoofax.shell.client.eclipse.impl.ICompositeFactory;

import com.google.inject.Injector;

/**
 * The workbench view containing all the widgets (see {@link EclipseDisplay} and
 * {@link EclipseEditor}) that together form the REPL. Note that because the plugin is registered to
 * Eclipse as a singleton, at most one ReplView will be active at any given time.
 */
public class ReplView extends ViewPart {
    public static final String ID = Activator.ID + ".view";
    private static final int DISPLAYWEIGHT = 5;
    private static final int EDITORWEIGHT = 1;
    private EclipseRepl repl;
    private EclipseEditor editor;
    private ColorManager colorManager;

    @Override
    public void createPartControl(Composite parent) {
        Injector injector = Activator.injector();
        SashForm page = new SashForm(parent, SWT.VERTICAL | SWT.LEFT_TO_RIGHT);

        // Create the display first so it appears on top in the sash.
        ICompositeFactory factory = injector.getInstance(ICompositeFactory.class);
        EclipseDisplay display = factory.createDisplay(page);
        this.editor = factory.createEditor(page);

        // Must be after the instantiation of the two widgets.
        page.setWeights(new int[] { DISPLAYWEIGHT, EDITORWEIGHT });

        // Instantiate the REPL and add it as observer of the editor.
        this.repl = factory.createRepl(display);
        this.editor.asObservable().subscribe(this.repl);

        // Retrieve the color manager so that it can be disposed of when the view is closed.
        this.colorManager = injector.getInstance(ColorManager.class);
    }

    /**
     * Return the currently active {@link EclipseRepl}.
     *
     * @return The currently active {@link EclipseRepl.
     */
    public EclipseRepl getRepl() {
        return this.repl;
    }

    @Override
    public void setFocus() {
        this.editor.setFocus();
    }

    @Override
    public void dispose() {
        this.colorManager.dispose();
        super.dispose();
    }

}
