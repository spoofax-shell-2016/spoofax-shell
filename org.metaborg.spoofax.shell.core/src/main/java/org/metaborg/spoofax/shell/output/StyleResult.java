package org.metaborg.spoofax.shell.output;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.metaborg.core.context.IContext;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.style.IRegionStyle;
import org.metaborg.core.style.RegionStyle;
import org.metaborg.spoofax.core.stratego.IStrategoCommon;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class StyleResult extends ParseResult {

    private Iterable<IRegionStyle<IStrategoTerm>> styled;

    @Inject
    public StyleResult(IStrategoCommon common, @Assisted ISpoofaxParseUnit unit,
                       @Assisted Iterable<IRegionStyle<IStrategoTerm>> styled) {
        super(common, unit);
        this.styled = styled;
    }

    @Override
    public Optional<IContext> context() {
        return Optional.empty();
    }

    @Override
    public List<IMessage> messages() {
        return StreamSupport.stream(unit().messages().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public StyledText styled() {
        Iterable<IRegionStyle<String>> collect = StreamSupport.stream(styled.spliterator(), false)
                .map(e -> new RegionStyle<String>(e.region(), e.style(),
                        sourceText().substring(e.region().startOffset(),
                                               e.region().endOffset() + 1)))
        .collect(Collectors.toList());

        return new StyledText(collect);
    }

    @Override
    public String sourceText() {
        return unit().input().text();
    }

    @Override
    public boolean valid() {
        return unit().valid() && unit().success();
    }

    @Override
    public void accept(IResultVisitor visitor) {
        visitor.visitTermResult(this);
    }

}
