package org.metaborg.spoofax.shell.output;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.core.syntax.JSGLRParserConfiguration;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxTransformUnit;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.name.Named;

/**
 * Factory for creating {@link ISpoofaxResult results}.
 */
public interface IResultFactory {

    /**
     * Create an {@link InputResult} that can be passed to the REPL.
     *
     * @param unit
     *            The wrapped {@link ISpoofaxInputUnit}.
     * @return An {@link InputResult}.
     */
    InputResult createInputResult(ISpoofaxInputUnit unit);

    /**
     * Create an {@link InputResult} that can be passed to the REPL.
     *
     * @param lang
     *            The language of this {@link InputResult}.
     * @param file
     *            The {@link FileObject} containing the source expression.
     * @param source
     *            The source expression.
     * @return An {@link InputResult}.
     */
    InputResult createInputResult(ILanguageImpl lang, FileObject file, String source);

    /**
     * Create an {@link InputResult} that can be passed to the REPL.
     *
     * @param lang
     *            The language of this {@link InputResult}.
     * @param file
     *            The {@link FileObject} containing the source expression.
     * @param source
     *            The source expression.
     * @param parserConfig
     *            The {@link JSGLRParserConfiguration parser configuration}.
     * @return An {@link InputResult}.
     */
    InputResult createInputResult(ILanguageImpl lang, FileObject file, String source,
                                  JSGLRParserConfiguration parserConfig);

    /**
     * Create a {@link ParseResult} that can be passed to the REPL.
     *
     * @param unit
     *            The wrapped {@link ISpoofaxParseUnit}.
     * @return A {@link ParseResult}.
     */
    ParseResult createParseResult(ISpoofaxParseUnit unit);

    /**
     * Create an {@link AnalyzeResult} that can be passed to the REPL.
     *
     * @param unit
     *            the wrapped {@link ISpoofaxAnalyzeUnit}
     * @return an {@link AnalyzeResult}
     */
    AnalyzeResult createAnalyzeResult(ISpoofaxAnalyzeUnit unit);

    /**
     * Create a {@link TransformResult} that can be passed to the REPL.
     *
     * @param unit
     *            The wrapped {@link ISpoofaxTransformUnit}.
     * @return A {@link TransformResult}.
     */
    TransformResult createTransformResult(ISpoofaxTransformUnit<?> unit);

    /**
     * Create an {@link EvaluateResult} that can be passed to the REPL.
     *
     * @param parsed
     *            The wrapped {@link ParseResult}.
     * @param result
     *            The result of the evaluation.
     * @return An {@link EvaluateResult}.
     */
    @Named("parsed")
    EvaluateResult createEvaluateResult(ParseResult parsed, IStrategoTerm result);

    /**
     * Create an {@link EvaluateResult} that can be passed to the REPL.
     *
     * @param analyzed
     *            The wrapped {@link AnalyzeResult}.
     * @param result
     *            The result of the evaluation.
     * @return An {@link EvaluateResult}.
     */
    @Named("analyzed")
    EvaluateResult createEvaluateResult(AnalyzeResult analyzed, IStrategoTerm result);

}
