package org.metaborg.spoofax.shell.commands;

import java.util.Collection;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.action.ITransformAction;
import org.metaborg.core.action.ITransformGoal;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.transform.ISpoofaxTransformService;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxTransformUnit;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.ParseResult;
import org.metaborg.spoofax.shell.output.TransformResult;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * A transform command working with a language that does not define an analysis step.
 */
public class ParsedTransformCommand extends AbstractTransformCommand<ParseResult> {
    private final IContextService contextService;

    /**
     * Instantiate a new {@link ParsedTransformCommand}.
     *
     * @param contextService
     *            The {@link IContextService}.
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
    public ParsedTransformCommand(IContextService contextService,
                                  ISpoofaxTransformService transformService,
                                  IResultFactory resultFactory,
                                  @Assisted IProject project, @Assisted ILanguageImpl lang,
                                  @Assisted ITransformAction action) {
        super(transformService, resultFactory, project, lang,
              action);
        this.contextService = contextService;
    }

    @Override
    protected TransformResult transform(ParseResult arg, ITransformGoal goal)
        throws MetaborgException {
        ISpoofaxParseUnit unit = arg.unit();
        IContext context = contextService.get(unit.source(), project, lang);

        Collection<ISpoofaxTransformUnit<ISpoofaxParseUnit>> transform =
            transformService.transform(unit, context, goal);
        return resultFactory.createTransformResult(transform.iterator().next());
    }

}
