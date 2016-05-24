package org.metaborg.spoofax.shell.client.eclipse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.ui.console.IOConsole;
import org.metaborg.spoofax.shell.client.Repl;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * An {@link org.eclipse.ui.IConsole} hosting a {@link org.metaborg.spoofax.shell.client.Repl}.
 */
public class ReplConsole extends IOConsole implements Runnable {
    private static final String CONSOLENAME = "Spoofax REPL";
    private final Repl repl;

    /**
     * Instantiates a new ReplConsole.
     */
    @Inject
    public ReplConsole() {
        super(CONSOLENAME, null);
        InputStream in = this.getInputStream();
        OutputStream out = this.newOutputStream();
        OutputStream err = this.newOutputStream();

        Module module = new EclipseReplModule(in, out, err);
        Injector injector = Guice.createInjector(module);
        this.repl = injector.getInstance(Repl.class);
    }

    /**
     * Runs the {@link Repl}.
     *
     * @throws IOException
     *             When an IO error occurs.
     */
    @Override
    public void run() {
        try {
            this.repl.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
