package org.metaborg.spoofax.shell.commands;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.analysis.ISpoofaxAnalysisService;
import org.metaborg.spoofax.core.analysis.ISpoofaxAnalyzeResult;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.shell.output.AnalyzeResult;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.ParseResult;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test creating and using the {@link AnalyzeCommand}.
 */
@RunWith(MockitoJUnitRunner.class)
public class AnalyzeCommandTest {
    // Constructor mocks
    @Mock private IContextService contextService;
    @Mock private ISpoofaxAnalysisService analysisService;
    @Mock private IResultFactory resultFactory;
    @Mock private IProject project;
    @Mock private ILanguageImpl lang;

    @Mock private IContext context;

    @Mock private ParseResult parseResult;
    @Mock private ISpoofaxAnalyzeResult spoofaxAnalyzeResult;
    @Mock private AnalyzeResult analyzeResult;

    private FileObject sourceFile;
    private AnalyzeCommand analyzeCommand;


    /**
     * Set up mocks used in the test case.
     * @throws FileSystemException when resolving the temp file fails
     * @throws MetaborgException when parsing fails
     */
    @Before
    public void setup() throws FileSystemException, MetaborgException {
        sourceFile = VFS.getManager().resolveFile("ram://junit-temp");

        when(project.location()).thenReturn(sourceFile);

        when(parseResult.context()).thenReturn(Optional.of(context));

        when(analysisService.analyze(any(), any())).thenReturn(spoofaxAnalyzeResult);
        when(resultFactory.createAnalyzeResult(any())).thenReturn(analyzeResult);

        analyzeCommand =
            new AnalyzeCommand(contextService, analysisService, resultFactory, project, lang);
    }

    /**
     * Verify that the description of a command is never null.
     */
    @Test
    public void testDescription() {
        assertThat(analyzeCommand.description(), isA(String.class));
    }

    /**
     * Test parsing source that results in a valid {@link ISpoofaxParseUnit}.
     * @throws MetaborgException whParseResulten the source contains invalid syntax
     * @throws IOException when reading from file fails
     */
    @Test
    public void testAnalyzeValid() throws MetaborgException {
        when(analyzeResult.valid()).thenReturn(true);

        AnalyzeResult actual = analyzeCommand.execute(parseResult);
        assertEquals(actual, analyzeResult);
    }

    /**
     * Test parsing source that results in an invalid {@link ISpoofaxParseUnit}.
     * @throws MetaborgException when the source contains invalid syntax
     */
    @Test(expected = MetaborgException.class)
    public void testAnalyzeInvalid() throws MetaborgException {
        when(analyzeResult.valid()).thenReturn(false);

        analyzeCommand.execute(parseResult);
    }
}
