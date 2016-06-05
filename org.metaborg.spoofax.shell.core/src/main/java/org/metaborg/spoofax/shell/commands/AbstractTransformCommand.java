package org.metaborg.spoofax.shell.commands;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.action.ITransformAction;
import org.metaborg.core.action.ITransformGoal;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.messages.IMessage;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.transform.ISpoofaxTransformService;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.TransformResult;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Represents an evaluate command sent to Spoofax.
 *
 * @param <A>
 *            The argument type of the {@link #execute(A)} method. Depending on whether or not the
 *            language implementation offers an analyze step, this is either {@link AnalysisResult}
 *            or {@link ParseResult>.
 */
public abstract class AbstractTransformCommand<A>
    extends AbstractSpoofaxCommand<A, TransformResult> {

    protected final ISpoofaxTransformService transformService;
    private final ITransformAction action;

    /**
     * Instantiate a new {@link TransformCommand}.
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
    public AbstractTransformCommand(ISpoofaxTransformService transformService,
                                    IResultFactory resultFactory,
                                    @Assisted IProject project, @Assisted ILanguageImpl lang,
                                    @Assisted ITransformAction action) {
        super(resultFactory, project, lang);
        this.transformService = transformService;
        this.action = action;
    }

    @Override
    public String description() {
        return action.name();
    }

    /**
     * Transform the given argument according to the given {@link ITransformAction}.
     *
     * @param arg
     *            The argument to transform.
     * @param goal
     *            The {@link ITransformGoal} by which to transform.
     * @return The result of the transformation.
     * @throws MetaborgException
     *             When there is an exception during the transformation.
     */
    protected abstract TransformResult transform(A arg, ITransformGoal goal)
        throws MetaborgException;

    @Override
    public TransformResult execute(A arg) throws MetaborgException {
        TransformResult result = transform(arg, action.goal());

        // TODO: pass the result to the client instead of throwing an exception -- The client needs
        // the result in order to do fancy stuff.
        if (!result.valid()) {
            String collect = Stream
                .concat(Stream.of("Transform messages:"),
                        result.messages().stream().map(IMessage::message))
                .collect(Collectors.joining("\n"));
            throw new MetaborgException(collect);
        }

        return result;
    }

}
