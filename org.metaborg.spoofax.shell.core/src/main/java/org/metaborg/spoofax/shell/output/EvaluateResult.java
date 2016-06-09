package org.metaborg.spoofax.shell.output;

import java.util.List;
import java.util.Optional;

import org.metaborg.core.context.IContext;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.unit.IUnit;
import org.metaborg.spoofax.core.stratego.IStrategoCommon;
import org.metaborg.spoofax.shell.commands.EvaluateCommand;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Represents the result of the {@link EvaluateCommand}.
 */
public abstract class EvaluateResult extends AbstractSpoofaxResult<IUnit> {
    private final AbstractSpoofaxResult<?> wrappedDelegate;
    private final IStrategoTerm result;

    /**
     * The result of the evaluation of an analyzed AST.
     */
    public static class Analyzed extends EvaluateResult {

        /**
         * Instantiate a new EvaluateResult.
         *
         * @param common
         *            The {@link IStrategoCommon} service, used to pretty print
         *            {@link IStrategoTerm}s.
         * @param analyzed
         *            The wrapped {@link AnalyzeResult}.
         * @param result
         *            The result of the evaluation.
         */
        @Inject
        public Analyzed(IStrategoCommon common, @Assisted AnalyzeResult analyzed,
                        @Assisted IStrategoTerm result) {
            super(common, analyzed, result);
        }
    }

    /**
     * The result of the evaluation of a parsed, but not analyzed, AST.
     */
    public static class Parsed extends EvaluateResult {

        /**
         * Instantiate a new EvaluateResult.
         *
         * @param common
         *            The {@link IStrategoCommon} service, used to pretty print
         *            {@link IStrategoTerm}s.
         * @param parsed
         *            The wrapped {@link ParseResult}.
         * @param result
         *            The result of the evaluation.
         */
        @Inject
        public Parsed(IStrategoCommon common, @Assisted ParseResult parsed,
                      @Assisted IStrategoTerm result) {
            super(common, parsed, result);
        }
    }

    private <T extends IUnit> EvaluateResult(IStrategoCommon common,
                                             AbstractSpoofaxResult<T> wrappedResult,
                                             IStrategoTerm result) {
        super(common, wrappedResult.unit());
        this.wrappedDelegate = wrappedResult;
        this.result = result;
    }

    @Override
    public Optional<IStrategoTerm> ast() {
        return Optional.of(result);
    }

    @Override
    public StyledText styled() {
        return toString(ast().get());
    }

    @Override
    public boolean valid() {
        return wrappedDelegate.valid();
    }

    @Override
    public Optional<IContext> context() {
        return wrappedDelegate.context();
    }

    @Override
    public List<IMessage> messages() {
        return wrappedDelegate.messages();
    }
}
