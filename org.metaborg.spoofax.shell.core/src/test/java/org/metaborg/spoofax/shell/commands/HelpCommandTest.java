package org.metaborg.spoofax.shell.commands;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.metaborg.core.MetaborgException;
import org.metaborg.spoofax.shell.invoker.CommandNotFoundException;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;
import org.metaborg.spoofax.shell.output.StyledText;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Maps;

/**
 * Test creating and using the {@link HelpCommand}.
 */
@RunWith(MockitoJUnitRunner.class)
public class HelpCommandTest {
    // Constructor mocks
    @Mock private ICommandInvoker invoker;

    // Command mocks
    private IReplCommand<?, ?> singleLineComment;
    private IReplCommand<?, ?> multiLineComment;

    private HelpCommand helpCommand;
    private Map<String, IReplCommand<?, ?>> commands;

    /**
     * Set up mocks used in the test case.
     * @throws CommandNotFoundException when command could not be found
     */
    @Before
    public void setup() throws CommandNotFoundException {
        singleLineComment = mock(IReplCommand.class, RETURNS_MOCKS);
        multiLineComment = mock(IReplCommand.class, RETURNS_MOCKS);

        commands = Maps.newHashMap();
        commands.put("name-1", singleLineComment);
        commands.put("name-2", multiLineComment);

        when(singleLineComment.description()).thenReturn("test-1");
        when(multiLineComment.description()).thenReturn("test-2\ntest-2");
        when(invoker.getCommands()).thenReturn(commands);

        Mockito.<IReplCommand<?, ?>>when(invoker.commandFromName("name-1"))
        .thenReturn(singleLineComment);
        Mockito.<IReplCommand<?, ?>>when(invoker.commandFromName("name-2"))
        .thenReturn(multiLineComment);

        helpCommand = new HelpCommand(invoker);
    }

    /**
     * Verify that the description of a command is never null.
     */
    @Test
    public void testDescription() {
        assertThat(helpCommand.description(), isA(String.class));
    }

    /**
     * Test getting help for a command that does not exist.
     *
     * @throws MetaborgException
     *             expected.
     */
    @Test(expected = MetaborgException.class)
    public void testCommandNotFound() throws MetaborgException {
        try {
            when(invoker.commandFromName(any())).thenThrow(new CommandNotFoundException("error"));
            helpCommand.execute("invalid-command");
        } catch (CommandNotFoundException e) {
            fail("Should not happen");
        }
    }

    /**
     * Test getting help for an existing command with a single line description.
     *
     * @throws MetaborgException
     *             Not expected
     */
    @Test
    public void testCommandSingleLine() throws MetaborgException {
        String expected = "name-1 test-1";
        StyledText actual = helpCommand.execute("name-1");
        assertEquals(expected, actual.toString());
    }

    /**
     * Test getting help for an existing command with a multi-line description.
     *
     * @throws MetaborgException
     *             Not expected.
     */
    @Test
    public void testCommandMultiLine() throws MetaborgException {
        String expected = "name-2 test-2\n" + "       test-2";
        StyledText actual = helpCommand.execute("name-2");
        assertEquals(expected, actual.toString());
    }

    /**
     * Test getting help for an existing command with a multi-line description.
     *
     * @throws MetaborgException
     *             Not expected.
     */
    @Test
    public void testCommands() throws MetaborgException {
        String expected = "name-1 test-1\n"
                          + "name-2 test-2\n"
                          + "       test-2";
        StyledText actual = helpCommand.execute("");
        assertEquals(expected.toString(), actual.toString());
    }

}
