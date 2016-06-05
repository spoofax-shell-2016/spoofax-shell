package org.metaborg.spoofax.shell.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.syntax.ISpoofaxSyntaxService;
import org.metaborg.spoofax.core.syntax.SpoofaxSyntaxService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.InputResult;
import org.metaborg.spoofax.shell.output.ParseResult;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Represents a parse command sent to Spoofax.
 */
public class ParseCommand extends AbstractSpoofaxCommand<String, ParseResult> {
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

    private FileObject write(String source) throws IOException {
        // FIXME: hardcoded file path
        FileObject sourceFile = this.project.location().resolveFile("tmp.src");
        try (OutputStream os = sourceFile.getContent().getOutputStream()) {
            os.write(source.getBytes(Charset.forName("UTF-8")));
        }

        return sourceFile;
    }

    private ParseResult parse(InputResult unit) throws MetaborgException {
        ISpoofaxParseUnit parse = syntaxService.parse(unit.unit());
        ParseResult result = resultFactory.createParseResult(parse);
        // TODO: pass the result to the client instead of throwing an exception -- The client needs
        // the result in order to do fancy stuff.
        if (!result.valid()) {
            String collect = Stream
                .concat(Stream.of("Parse messages:"),
                        result.messages().stream().map(IMessage::message))
                .collect(Collectors.joining("\n"));
            throw new MetaborgException(collect);
        }
        return result;
    }

    @Override
    public ParseResult execute(String arg) throws MetaborgException {
        try {
            // TODO: can we make this InputUnit here and drop the otherwise unused InputResult?
            InputResult input = resultFactory.createInputResult(lang, write(arg), arg);
            return parse(input);
        } catch (IOException e) {
            throw new MetaborgException("Cannot write to temporary source file.");
        }
    }
}
