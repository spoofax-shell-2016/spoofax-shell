package org.metaborg.spoofax.shell.client.console.impl;

import static org.junit.Assert.fail;
import static org.metaborg.spoofax.shell.client.console.impl.TerminalUserInterfaceTest.C_D;
import static org.metaborg.spoofax.shell.client.console.impl.TerminalUserInterfaceTest.ENTER;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.metaborg.core.MetaborgException;
import org.metaborg.spoofax.shell.client.IEditor;
import org.metaborg.spoofax.shell.client.console.commands.ExitCommand;
import org.metaborg.spoofax.shell.commands.IReplCommand;
import org.metaborg.spoofax.shell.core.ReplModule;
import org.metaborg.spoofax.shell.invoker.CommandNotFoundException;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;
import org.mockito.Mockito;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

/**
 * Tests {@link ConsoleRepl}.
 */
public class ConsoleReplTest {
    private Injector injector;
    private ConsoleRepl repl;
    private ByteArrayInputStream in;
    private ByteArrayOutputStream out;
    private IEditor editorSpy;
    private ICommandInvoker invokerSpy;

    /**
     * Create an {@link Injector} with {@link Module} {@code overrides}.
     *
     * @param overrides
     *            The module overrides.
     */
    private void createInjector(Module... overrides) {
        Module overridden = Modules.override(replModule()).with(overrides);
        injector = Guice.createInjector(overridden);
    }

    private ReplModule replModule() {
        return new ConsoleReplModule();
    }

    /**
     * Setup and inject the {@link InputStream}s and {@link OutputStream}s.
     *
     * @param overrides
     *            Module overrides w.r.t. ReplModule.
     */
    private void createRepl(Module... overrides) {
        createInjector(overrides);
        repl = injector.getInstance(ConsoleRepl.class);
    }

    /**
     * Setup the input streams.
     *
     * @param inputString
     *            The user input that will be simulated.
     * @throws UnsupportedEncodingException
     *             When UTF-8 is not supported.
     */
    private void setUp(String inputString) throws UnsupportedEncodingException {
        in = new ByteArrayInputStream(inputString.getBytes("UTF-8"));
        out = new ByteArrayOutputStream();

        injector = Guice.createInjector(replModule());
    }

    private void setUpCtrlD() throws IOException {
        setUp(String.valueOf(C_D));
        invokerSpy = mock(ICommandInvoker.class, RETURNS_MOCKS);

        // Create a user input simulated ConsoleRepl with the mock invoker.
        createRepl(new UserInputSimulationModule(in, out), new MockModule(invokerSpy));
    }

    /**
     * Test whether the REPl exits when Control-D is pressed.
     */
    @Test
    public void testCtrlDDoesExit() {
        try {
            setUpCtrlD();
            repl.run();
            // Ensure that the command invoker is never called with any command.
            verify(invokerSpy, never()).execute(anyString());
        } catch (IOException | MetaborgException | CommandNotFoundException e) {
            fail("Should not happen");
        }
    }

    private void setUpExit() throws IOException {
        setUp(":exit" + ENTER + ENTER + ":exit" + ENTER + ENTER);
        createInjector(new UserInputSimulationModule(in, out));
        invokerSpy = spy(injector.getInstance(ICommandInvoker.class));
        editorSpy = spy(injector.getInstance(IEditor.class));

        // Create a user input simulated ConsoleRepl with the mock invoker and mock editor.
        createRepl(new UserInputSimulationModule(in, out), new MockModule(invokerSpy, editorSpy));
    }

    /**
     * Tests the {@link ExitCommand}.
     */
    @Test
    public void testExitCommand() {
        try {
            setUpExit();

            // Stub the invoker so that it returns an exit command which we can spy on.
            ExitCommand exitCommandSpy = spy(new ExitCommand(() -> repl));
            Mockito.<IReplCommand> when(invokerSpy.commandFromName("exit"))
                .thenReturn(exitCommandSpy);

            repl.run();

            // Ensure that the command was given to the invoker just once.
            verify(invokerSpy, times(1)).execute(":exit");

            // Ensure that exitCommand was executed once.
            verify(exitCommandSpy, times(1)).execute(null);

            // Verify that the Editor was not asked for input after the exit command was executed.
            verify(editorSpy, times(1)).getInput();
        } catch (IOException | CommandNotFoundException | MetaborgException e) {
            fail("Should not happen");
        }
    }
}
