package org.metaborg.spoofax.shell.client.console.commands;

import org.metaborg.spoofax.shell.client.console.impl.ConsoleRepl;
import org.metaborg.spoofax.shell.commands.IReplCommand;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Exit the REPL.
 */
public class ExitCommand implements IReplCommand<Void, Void> {

    private final Provider<ConsoleRepl> replProvider;

    /**
     * Instantiates a new ExitCommand.
     *
     * @param replProvider
     *            Provides the REPL instance.
     */
    @Inject
    public ExitCommand(Provider<ConsoleRepl> replProvider) {
        this.replProvider = replProvider;
    }

    @Override
    public String description() {
        return "Exit the REPL session.";
    }

    @Override
    public Void execute(Void arg) {
        replProvider.get().setRunning(false);
        return arg;
    }
}
