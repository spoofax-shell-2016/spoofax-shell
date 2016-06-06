package org.metaborg.spoofax.shell.commands;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.core.syntax.ParseException;
import org.metaborg.spoofax.core.syntax.ISpoofaxSyntaxService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.InputResult;
import org.metaborg.spoofax.shell.output.ParseResult;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test creating and using the {@link ParseCommand}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ParseCommandTest {
    // Constructor mocks
    @Mock private ISpoofaxSyntaxService syntaxService;
    @Mock private IResultFactory resultFactory;
    @Mock private IProject project;
    @Mock private ILanguageImpl lang;
    @Mock private IDisplay display;
    @Mock private InputResult inputResult;
    @Mock private ParseResult parseResult;

    private FileObject sourceFile;
    private ParseCommand parseCommand;

    /**
     * Set up mocks used in the test case.
     * @throws FileSystemException when resolving the temp file fails
     * @throws ParseException when parsing fails
     */
    @Before
    public void setup() throws FileSystemException, ParseException {
        sourceFile = VFS.getManager().resolveFile("ram://junit-temp");

        when(project.location()).thenReturn(sourceFile);

        when(resultFactory.createInputResult(any(), any(), any())).thenReturn(inputResult);
        when(resultFactory.createParseResult(any())).thenReturn(parseResult);

        parseCommand = new ParseCommand(syntaxService, resultFactory, project, lang);
    }

    /**
     * Verify that the description of a command is never null.
     */
    @Test
    public void testDescription() {
        assertThat(parseCommand.description(), isA(String.class));
    }

    /**
     * Test the {@link ParseCommand} for source resulting in a valid {@link ISpoofaxParseUnit}.
     *
     * @throws MetaborgException
     *             should not happen.
     */
    @Test
    public void testExecute() throws MetaborgException {
        parseCommand.execute(inputResult).accept(display);
        verify(display, times(1)).displayResult(parseResult);
    }
}
