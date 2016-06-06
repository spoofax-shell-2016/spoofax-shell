package org.metaborg.spoofax.shell.commands;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.analysis.IAnalysisService;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.analysis.ISpoofaxAnalysisService;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.client.hooks.IHook;
import org.metaborg.spoofax.shell.output.AnalyzeResult;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.ParseResult;
import org.metaborg.util.concurrent.IClosableLock;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Represents an analyze command sent to Spoofax.
 */
public class AnalyzeCommand extends AbstractSpoofaxCommand<ParseResult> {
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

    @Override
    public IHook execute(ParseResult arg) throws MetaborgException {
        IContext context = arg.context().orElse(contextService.get(arg.source(), project, lang));

        ISpoofaxAnalyzeUnit analyze;
        try (IClosableLock lock = context.write()) {
            analyze = analysisService.analyze(arg.unit(), context).result();
        }
        AnalyzeResult result = resultFactory.createAnalyzeResult(analyze);

        return (IDisplay display) -> display.displayResult(result);
    }
}
