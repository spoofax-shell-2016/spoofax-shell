package org.metaborg.spoofax.shell.functions;

import java.util.Map;

import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.shell.ShellFacet;
import org.metaborg.spoofax.shell.core.IEvaluationStrategy;
import org.metaborg.spoofax.shell.output.EvaluateResult;
import org.metaborg.spoofax.shell.output.FailOrSuccessResult;
import org.metaborg.spoofax.shell.output.IResult;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.ParseResult;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Creates an {@link EvaluateResult} from a given {@link ParseResult}.
 */
public class PEvalFunction extends ContextualSpoofaxFunction<ParseResult, EvaluateResult> {
    private final Map<String, IEvaluationStrategy> evaluationStrategies;

    /**
     * Instantiate a {@link PEvalFunction}.
     *
     * @param evaluationStrategies
     *            The {@link IEvaluationStrategy} implementations, grouped by their names as keys.
     * @param contextService
     *            The {@link IContextService}.
     * @param resultFactory
     *            The {@link IResultFactory} for creating delegate commands.
     * @param project
     *            The {@link IProject} in which this command should operate.
     * @param lang
     *            The {@link ILanguageImpl} to which this command applies.
     */
    @Inject
    public PEvalFunction(Map<String, IEvaluationStrategy> evaluationStrategies,
                         IContextService contextService, IResultFactory resultFactory,
                         @Assisted IProject project, @Assisted ILanguageImpl lang) {
        super(contextService, resultFactory, project, lang);
        this.evaluationStrategies = evaluationStrategies;
    }

    @Override
    protected FailOrSuccessResult<EvaluateResult, IResult>
            applyThrowing(IContext context, ParseResult a) throws Exception {
        ShellFacet facet = context.language().facet(ShellFacet.class);
        IEvaluationStrategy evalStrategy = evaluationStrategies.get(facet.getEvaluationMethod());
        IStrategoTerm result = evalStrategy.evaluate(a, context);

        return FailOrSuccessResult.ofSpoofaxResult(resultFactory.createEvaluateResult(a, result));
    }
}
