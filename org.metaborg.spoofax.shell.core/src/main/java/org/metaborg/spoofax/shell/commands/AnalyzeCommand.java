package org.metaborg.spoofax.shell.commands;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.analysis.IAnalysisService;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.analysis.ISpoofaxAnalysisService;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.shell.output.AnalyzeResult;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.ParseResult;
import org.metaborg.util.concurrent.IClosableLock;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Represents an analyze command sent to Spoofax.
 */
public class AnalyzeCommand extends AbstractSpoofaxCommand<ParseResult, AnalyzeResult> {
    private static final String DESCRIPTION = "Analyze an expression.";
    private final IContextService contextService;
    private final ISpoofaxAnalysisService analysisService;

    /**
     * Instantiate an {@link AnalyzeCommand}.
     *
     * @param contextService
     *            The {@link IContextService}.
     * @param analysisService
     *            The {@link IAnalysisService}
     * @param resultFactory
     *            The {@link ResultFactory}.
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     */
    @Inject
    public AnalyzeCommand(IContextService contextService, ISpoofaxAnalysisService analysisService,
                          IResultFactory resultFactory, @Assisted IProject project,
                          @Assisted ILanguageImpl lang) {
        super(resultFactory, project, lang);
        this.contextService = contextService;
        this.analysisService = analysisService;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    private AnalyzeResult analyze(ParseResult unit) throws MetaborgException {
        IContext context = unit.context().orElse(contextService.get(unit.source(), project, lang));

        ISpoofaxAnalyzeUnit analyze;
        try (IClosableLock lock = context.write()) {
            analyze = analysisService.analyze(unit.unit(), context).result();
        }
        AnalyzeResult result = resultFactory.createAnalyzeResult(analyze);

        // TODO: pass the result to the client instead of throwing an exception -- The client needs
        // the result in order to do fancy stuff.
        if (!result.valid()) {
            String collect = Stream.concat(Stream.of("Analyze messages:"),
                                           result.messages().stream().map(IMessage::message))
                    .collect(Collectors.joining("\n"));
            throw new MetaborgException(collect);
        }
        return result;
    }

    @Override
    public AnalyzeResult execute(ParseResult arg) throws MetaborgException {
        return analyze(arg);
    }
}
