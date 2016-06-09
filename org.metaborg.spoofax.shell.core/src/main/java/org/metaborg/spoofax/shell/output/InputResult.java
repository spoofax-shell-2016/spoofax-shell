package org.metaborg.spoofax.shell.output;

import java.util.List;
import java.util.Optional;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.context.IContext;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.messages.IMessage;
import org.metaborg.spoofax.core.stratego.IStrategoCommon;
import org.metaborg.spoofax.core.syntax.JSGLRParserConfiguration;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxUnitService;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.common.collect.Lists;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * The InputResult wraps the {@link ISpoofaxInputUnit} to provide a base result.
 */
public class InputResult extends AbstractSpoofaxResult<ISpoofaxInputUnit> {

    /**
     * Instantiate a new InputResult.
     *
     * @param common
     *            The {@link IStrategoCommon} service, used to pretty print {@link IStrategoTerm}s.
     * @param unit
     *            The wrapped {@link ISpoofaxInputUnit}.
     */
    @AssistedInject
    public InputResult(IStrategoCommon common, @Assisted ISpoofaxInputUnit unit) {
        super(common, unit);
    }

    /**
     * Instantiate a new InputResult from source.
     *
     * @param common
     *            The {@link IStrategoCommon} service, used to pretty print {@link IStrategoTerm}s.
     * @param unitService
     *            The {@link ISpoofaxUnitService} to create {@link ISpoofaxInputUnit}s.
     * @param lang
     *            The language of this {@link InputResult}.
     * @param file
     *            The {@link FileObject} containing the source expression.
     * @param source
     *            The source expression.
     * @param parserConfig
     *            The {@link JSGLRParserConfiguration parser configuration}.
     */
    @AssistedInject
    public InputResult(IStrategoCommon common, ISpoofaxUnitService unitService,
                       @Assisted ILanguageImpl lang, @Assisted FileObject file,
                       @Assisted String source, @Assisted JSGLRParserConfiguration parserConfig) {
        super(common, unitService.inputUnit(file, source, lang, null, parserConfig));
    }

    /**
     * Create a {@link InputResult} from source.
     *
     * @param common
     *            The {@link IStrategoCommon} service, used to pretty print {@link IStrategoTerm}s.
     * @param unitService
     *            The {@link ISpoofaxUnitService} to create {@link ISpoofaxInputUnit}s.
     * @param lang
     *            The language of this {@link InputResult}.
     * @param file
     *            The {@link FileObject} containing the source expression.
     * @param source
     *            The source expression.
     */
    @AssistedInject
    public InputResult(IStrategoCommon common, ISpoofaxUnitService unitService,
                       @Assisted ILanguageImpl lang, @Assisted FileObject file,
                       @Assisted String source) {
        this(common, unitService, lang, file, source, null);
    }

    @Override
    public Optional<IStrategoTerm> ast() {
        return Optional.empty();
    }

    @Override
    public Optional<IContext> context() {
        return Optional.empty();
    }

    @Override
    public List<IMessage> messages() {
        return Lists.newArrayList();
    }

    @Override
    public StyledText styled() {
        return new StyledText(unit().text());
    }

    @Override
    public boolean valid() {
        return true;
    }

}
