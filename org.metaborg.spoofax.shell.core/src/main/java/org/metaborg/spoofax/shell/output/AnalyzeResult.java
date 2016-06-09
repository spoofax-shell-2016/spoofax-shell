package org.metaborg.spoofax.shell.output;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.metaborg.core.context.IContext;
import org.metaborg.core.messages.IMessage;
import org.metaborg.spoofax.core.stratego.IStrategoCommon;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.shell.commands.AnalyzeCommand;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Represents the result of the {@link AnalyzeCommand}.
 */
public class AnalyzeResult extends AbstractSpoofaxResult<ISpoofaxAnalyzeUnit> {

    /**
     * Instantiate a new AnalyzeResult.
     *
     * @param common
     *            The {@link IStrategoCommon} service, used to pretty print {@link IStrategoTerm}s.
     * @param unit
     *            The wrapped {@link ISpoofaxAnalyzeUnit}.
     */
    @AssistedInject
    public AnalyzeResult(IStrategoCommon common, @Assisted ISpoofaxAnalyzeUnit unit) {
        super(common, unit);
    }

    // Duplication here and in TransformResult is intentional since no common ancestor of
    // ISpoofaxAnalyzeUnit and ISpoofaxTransformUnit exists with these functions.
    @SuppressWarnings("CPD-START")
    @Override
    public Optional<IStrategoTerm> ast() {
        return Optional.of(unit().ast());
    }

    @Override
    public Optional<IContext> context() {
        return Optional.of(unit().context());
    }

    @Override
    public List<IMessage> messages() {
        return StreamSupport.stream(unit().messages().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public StyledText styled() {
        if (valid()) {
            return toString(unit().ast());
        } else {
            return new StyledText(messages().toString());
        }
    }

    @SuppressWarnings("CPD-END")
    @Override
    public boolean valid() {
        return unit().valid();
    }

}
