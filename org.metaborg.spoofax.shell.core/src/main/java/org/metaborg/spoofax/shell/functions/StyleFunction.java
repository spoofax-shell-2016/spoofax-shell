package org.metaborg.spoofax.shell.functions;

import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.core.style.IRegionCategory;
import org.metaborg.core.style.IRegionStyle;
import org.metaborg.core.syntax.ParseException;
import org.metaborg.spoofax.core.style.ISpoofaxCategorizerService;
import org.metaborg.spoofax.core.style.ISpoofaxStylerService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.shell.output.FailOrSuccessResult;
import org.metaborg.spoofax.shell.output.IResult;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.ParseResult;
import org.metaborg.spoofax.shell.output.StyleResult;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class StyleFunction extends AbstractSpoofaxFunction<ParseResult, StyleResult> {
    private ISpoofaxCategorizerService categorizerService;
    private ISpoofaxStylerService stylerService;

    @Inject
    public StyleFunction(ISpoofaxCategorizerService categorizerService, ISpoofaxStylerService stylerService,
                         IResultFactory resultFactory, @Assisted IProject project,
                         @Assisted ILanguageImpl lang) {
        super(resultFactory, project, lang);
        this.categorizerService = categorizerService;
        this.stylerService = stylerService;
    }

    @Override
    protected FailOrSuccessResult<StyleResult, IResult> applyThrowing(ParseResult a)
        throws ParseException {
        ISpoofaxParseUnit unit = a.unit();
        Iterable<IRegionCategory<IStrategoTerm>> categorized = categorizerService.categorize(lang, unit);
        Iterable<IRegionStyle<IStrategoTerm>> styled = stylerService.styleParsed(lang, categorized);

        return FailOrSuccessResult.ofSpoofaxResult(resultFactory.createStyleResult(unit, styled));
    }
}
