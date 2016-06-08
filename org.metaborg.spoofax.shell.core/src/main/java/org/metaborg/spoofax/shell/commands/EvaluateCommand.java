package org.metaborg.spoofax.shell.commands;

import java.io.IOException;
import java.util.Map;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.shell.ShellFacet;
import org.metaborg.spoofax.shell.client.IHook;
import org.metaborg.spoofax.shell.core.IEvaluationStrategy;
import org.metaborg.spoofax.shell.invoker.ICommandFactory;
import org.metaborg.spoofax.shell.output.AnalyzeResult;
import org.metaborg.spoofax.shell.output.EvaluateResult;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.InputResult;
import org.metaborg.spoofax.shell.output.ParseResult;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Represents an evaluate command sent to Spoofax.
 */
public class EvaluateCommand extends SpoofaxCommand {
    private static final String DESCRIPTION = "Evaluate an expression";

    private IContextService contextService;
    private ParseCommand parseCommand;
    private AnalyzeCommand analyzeCommand;
    private IResultFactory unitFactory;

    @Inject
    private Map<String, IEvaluationStrategy> evaluationStrategies;
    private EvaluationInvocationStrategy invocationStrategy;

    /**
     * Interface for what happens before evaluation of parsed input (i.e. either analyze or evaluate
     * as-is).
     */
    private interface EvaluationInvocationStrategy {
        EvaluateResult performEvaluation(IContext context, ParseResult parsed,
                                         IEvaluationStrategy evalStrategy)
            throws MetaborgException;
    }

    /**
     * Analyze before invoking the evaluation strategy.
     */
    private class AnalyzedInvocationStrategy implements EvaluationInvocationStrategy {
        @Override
        public EvaluateResult performEvaluation(IContext context, ParseResult parsed,
                                                IEvaluationStrategy evalStrategy)
            throws MetaborgException {
            AnalyzeResult analyzed = analyzeCommand.analyze(parsed);
            IStrategoTerm ast = evalStrategy.evaluate(analyzed, context);
            return unitFactory.createEvaluateResult(analyzed, ast);
        }
    }

    /**
     * Analyze before invoking the evaluation strategy.
     */
    private class NonAnalyzedInvocationStrategy implements EvaluationInvocationStrategy {
        @Override
        public EvaluateResult performEvaluation(IContext context, ParseResult parsed,
                                                IEvaluationStrategy evalStrategy)
            throws MetaborgException {
            IStrategoTerm ast = evalStrategy.evaluate(parsed, context);
            return unitFactory.createEvaluateResult(parsed, ast);
        }
    }

    /**
     * Instantiate an {@link EvaluateCommand}.
     *
     * @param contextService
     *            The {@link IContextService}.
     * @param commandFactory
     *            The {@link ICommandFactory} for creating delegate commands.
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     */
    @Inject
    // CHECKSTYLE.OFF: |
    public EvaluateCommand(IContextService contextService, ICommandFactory commandFactory,
                           IResultFactory unitFactory, @Assisted IProject project,
                           @Assisted ILanguageImpl lang, @Assisted boolean analyzed) {
        // CHECKSTYLE.ON: |
        super(unitFactory, project, lang);
        this.contextService = contextService;
        this.parseCommand = commandFactory.createParse(project, lang);
        this.analyzeCommand = commandFactory.createAnalyze(project, lang);
        this.unitFactory = unitFactory;

        this.invocationStrategy =
            analyzed ? new AnalyzedInvocationStrategy() : new NonAnalyzedInvocationStrategy();
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    private EvaluateResult evaluate(ParseResult parsed) throws MetaborgException {
        IContext context =
            parsed.context().orElse(contextService.get(parsed.source(), project, lang));
        ShellFacet facet = context.language().facet(ShellFacet.class);
        IEvaluationStrategy evalStrategy = evaluationStrategies.get(facet.getEvaluationMethod());
        return invocationStrategy.performEvaluation(context, parsed, evalStrategy);
    }

    @Override
    public IHook execute(String... args) throws MetaborgException {
        try {
            InputResult input = unitFactory.createInputResult(lang, write(args[0]), args[0]);
            ParseResult parse = parseCommand.parse(input);
            EvaluateResult result = this.evaluate(parse);
            return (display) -> display.displayResult(result);
        } catch (IOException e) {
            throw new MetaborgException("Cannot write to temporary source file.");
        }
    }
}
