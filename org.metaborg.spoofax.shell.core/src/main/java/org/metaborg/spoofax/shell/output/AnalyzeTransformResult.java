package org.metaborg.spoofax.shell.output;

import org.metaborg.spoofax.core.stratego.IStrategoCommon;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxInputUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxTransformUnit;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * 
 */
public class AnalyzeTransformResult extends AbstractTransformResult<ISpoofaxAnalyzeUnit> {
    /**
     * Create a {@link AnalyzeTransformResult}.
     * @param common  the {@link IStrategoCommon} service
     * @param unit    the wrapped {@link ISpoofaxTransformUnit}
     */
	@AssistedInject
	public AnalyzeTransformResult(IStrategoCommon common,
								  @Assisted ISpoofaxTransformUnit<ISpoofaxAnalyzeUnit> unit) {
		super(common, unit);
	}

	@Override
	public ISpoofaxInputUnit input() {
		return unit().input().input().input();
	}
}
