package org.metaborg.spoofax.shell.commands;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.syntax.ISpoofaxSyntaxService;
import org.metaborg.spoofax.core.syntax.SpoofaxSyntaxService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.client.hooks.IHook;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.InputResult;
import org.metaborg.spoofax.shell.output.ParseResult;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Represents a parse command sent to Spoofax.
 */
public class ParseCommand extends AbstractSpoofaxCommand<InputResult> {
    private static final String DESCRIPTION = "Parse an expression.";
    private final ISpoofaxSyntaxService syntaxService;

    /**
     * Instantiate a {@link ParseCommand}.
     *
     * @param syntaxService
     *            The {@link SpoofaxSyntaxService}.
     * @param resultFactory
     *            The {@link IResultFactory}.
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     */
    @Inject
    public ParseCommand(ISpoofaxSyntaxService syntaxService, IResultFactory resultFactory,
                        @Assisted IProject project, @Assisted ILanguageImpl lang) {
        super(resultFactory, project, lang);
        this.syntaxService = syntaxService;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    // private FileObject write(String source) throws IOException {
    // // FIXME: hardcoded file path
    // FileObject sourceFile = this.project.location().resolveFile("tmp.src");
    // try (OutputStream os = sourceFile.getContent().getOutputStream()) {
    // os.write(source.getBytes(Charset.forName("UTF-8")));
    // }
    //
    // return sourceFile;
    // }

    @Override
    public IHook execute(InputResult arg) throws MetaborgException {
        ISpoofaxParseUnit parse = syntaxService.parse(arg.unit());
        ParseResult result = resultFactory.createParseResult(parse);
        return (IDisplay display) -> display.displayResult(result);
    }
}
