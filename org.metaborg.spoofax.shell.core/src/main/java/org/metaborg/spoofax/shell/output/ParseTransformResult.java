package org.metaborg.spoofax.shell.output;

import org.metaborg.spoofax.core.stratego.IStrategoCommon;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxTransformUnit;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * 
 */
public class ParseTransformResult extends AbstractTransformResult<ISpoofaxParseUnit> {
    /**
     * Create a {@link ParseTransformResult}.
     * @param common  the {@link IStrategoCommon} service
     * @param unit    the wrapped {@link ISpoofaxTransformUnit}
     */
	@AssistedInject
	public ParseTransformResult(IStrategoCommon common,
								@Assisted ISpoofaxTransformUnit<ISpoofaxParseUnit> unit) {
		super(common, unit);
	}

	@Override
	public ISpoofaxInputUnit input() {
		return unit().input().input();
	}
}
