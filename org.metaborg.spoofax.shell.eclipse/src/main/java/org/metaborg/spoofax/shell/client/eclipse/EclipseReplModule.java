package org.metaborg.spoofax.shell.client.eclipse;

import java.io.InputStream;
import java.io.OutputStream;

import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.client.IEditor;
import org.metaborg.spoofax.shell.client.IInputHistory;
import org.metaborg.spoofax.shell.client.Repl;
import org.metaborg.spoofax.shell.client.console.ConsoleReplModule;
import org.metaborg.spoofax.shell.client.console.TerminalUserInterface;
import org.metaborg.spoofax.shell.client.console.history.JLine2InputHistory;
import org.metaborg.spoofax.shell.client.console.history.JLine2PersistentInputHistory;
import org.metaborg.spoofax.shell.client.console.strategy.ColorStrategy;
import org.metaborg.spoofax.shell.client.eclipse.strategy.EclipseStrategy;

import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * Bindings for the Eclipse REPL.
 */
public class EclipseReplModule extends ConsoleReplModule {
    private final InputStream in;
    private final OutputStream out;
    private final OutputStream err;

    /**
     * Instantiates a new EclipseReplModule.
     *
     * @param in
     *            The {@link InputStream} to pass to the {@link TerminalUserInterface}.
     * @param out
     *            The {@link OutputStream} to pass to the {@link TerminalUserInterface} to display
     *            regular output.
     * @param err
     *            The {@link OutputStream} to pass to the {@link TerminalUserInterface} to display
     *            errors.
     */
    public EclipseReplModule(InputStream in, OutputStream out, OutputStream err) {
        this.in = in;
        this.out = out;
        this.err = err;
    }

    @Override
    protected void configureUserInterface() {
        bind(IInputHistory.class).to(JLine2InputHistory.class);
        bind(JLine2InputHistory.class).to(JLine2PersistentInputHistory.class);
        bind(ColorStrategy.class).to(EclipseStrategy.class);

        // bind(TerminalUserInterface.class).in(Singleton.class);
        bind(IEditor.class).to(TerminalUserInterface.class);
        bind(IDisplay.class).to(TerminalUserInterface.class);

        bind(InputStream.class).annotatedWith(Names.named("in")).toInstance(this.in);
        bind(OutputStream.class).annotatedWith(Names.named("out")).toInstance(this.out);
        bind(OutputStream.class).annotatedWith(Names.named("err")).toInstance(this.err);

        bindConstant().annotatedWith(Names.named("historyPath"))
            .to(System.getProperty("user.home") + "/.spoofax_history");
    }

    @Override
    protected void configure() {
        super.configure();
        configureUserInterface();
        bind(Repl.class).in(Singleton.class);
    }
}
