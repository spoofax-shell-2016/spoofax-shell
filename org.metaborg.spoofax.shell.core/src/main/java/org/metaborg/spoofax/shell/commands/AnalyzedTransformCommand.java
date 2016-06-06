package org.metaborg.spoofax.shell.commands;

import java.util.Collection;

import org.metaborg.core.action.ITransformAction;
import org.metaborg.core.action.ITransformGoal;
import org.metaborg.core.context.IContext;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.core.transform.TransformException;
import org.metaborg.spoofax.core.transform.ISpoofaxTransformService;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxTransformUnit;
import org.metaborg.spoofax.shell.output.AnalyzeResult;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.TransformResult;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * A transform command working with a language that does define an analysis step.
 */
public class AnalyzedTransformCommand extends AbstractTransformCommand<AnalyzeResult> {

    /**
     * Instantiate a new {@link AnalyzedTransformCommand}.
     *
     * @param transformService
     *            The {@link ISpoofaxTransformService}.
     * @param resultFactory
     *            The {@link ResultFactory}.
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     * @param action
     *            The {@link ITransformAction} that this command executes.
     */
    @Inject
    public AnalyzedTransformCommand(ISpoofaxTransformService transformService,
                                    IResultFactory resultFactory, @Assisted IProject project,
                                    @Assisted ILanguageImpl lang,
                                    @Assisted ITransformAction action) {
        super(transformService, resultFactory, project, lang, action);
    }

    @Override
    protected TransformResult transform(AnalyzeResult arg, ITransformGoal goal)
        throws TransformException {
        ISpoofaxAnalyzeUnit unit = arg.unit();
        IContext context = unit.context();

        Collection<ISpoofaxTransformUnit<ISpoofaxAnalyzeUnit>> transform =
            transformService.transform(unit, context, goal);
        return resultFactory.createTransformResult(transform.iterator().next());
    }

}
