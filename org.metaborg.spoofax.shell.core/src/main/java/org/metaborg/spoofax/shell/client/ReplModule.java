package org.metaborg.spoofax.shell.client;

import org.metaborg.core.project.IProjectService;
import org.metaborg.core.project.ISimpleProjectService;
import org.metaborg.core.project.SimpleProjectService;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.shell.commands.HelpCommand;
import org.metaborg.spoofax.shell.commands.IReplCommand;
import org.metaborg.spoofax.shell.commands.LanguageCommand;
import org.metaborg.spoofax.shell.core.ClassPathInterpreterLoader;
import org.metaborg.spoofax.shell.core.DynSemEvaluationStrategy;
import org.metaborg.spoofax.shell.core.IEvaluationStrategy;
import org.metaborg.spoofax.shell.core.IInterpreterLoader;
import org.metaborg.spoofax.shell.invoker.ICommandFactory;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;
import org.metaborg.spoofax.shell.invoker.SpoofaxCommandInvoker;
import org.metaborg.spoofax.shell.output.EvaluateResult;
import org.metaborg.spoofax.shell.output.IResultFactory;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;

/**
 * This {@link Guice} {@link Module} binds the core REPL classes.
 * <p>
 * It is intended to be subclassed by client implementations, who should bind their implementations
 * of {@link IRepl} and {@link IDisplay}. The {@link IReplCommand}s require an {@link IHook}
 * implementation, but since it is a functional interface implementations can be created with
 * lambdas at runtime. For this reason, an implementation does not need to be bound by the client.
 * The other interfaces defined in this package ({@link IEditor} and {@link IInputHistory}) are not
 * mandatory to implement.
 */
public abstract class ReplModule extends SpoofaxModule {

    /**
     * Binds the default {@link IReplCommand commands}.
     *
     * @param commandBinder
     *            The {@link MapBinder} for binding the commands to their names.
     */
    protected void bindCommands(MapBinder<String, IReplCommand> commandBinder) {
        commandBinder.addBinding("help").to(HelpCommand.class).in(Singleton.class);
        commandBinder.addBinding("load").to(LanguageCommand.class).in(Singleton.class);

        bind(ICommandInvoker.class).to(SpoofaxCommandInvoker.class);
    }

    /**
     * Binds the {@link IEvaluationStrategy evaluation strategies}.
     *
     * @param evalStrategyBinder
     *            The {@link MapBinder} for binding the strategies to their names.
     */
    protected void bindEvalStrategies(MapBinder<String, IEvaluationStrategy> evalStrategyBinder) {
        bind(IInterpreterLoader.class).to(ClassPathInterpreterLoader.class);
        evalStrategyBinder.addBinding("dynsem").to(DynSemEvaluationStrategy.class);
    }

    @Override
    protected void bindProject() {
        bind(SimpleProjectService.class).in(Singleton.class);
        bind(ISimpleProjectService.class).to(SimpleProjectService.class);
        bind(IProjectService.class).to(SimpleProjectService.class);
    }

    @Override
    protected void configure() {
        super.configure();

        MapBinder<String, IReplCommand> commandBinder =
            MapBinder.newMapBinder(binder(), String.class, IReplCommand.class);
        MapBinder<String, IEvaluationStrategy> evalStrategyBinder =
            MapBinder.newMapBinder(binder(), String.class, IEvaluationStrategy.class);
        bindCommands(commandBinder);
        bindEvalStrategies(evalStrategyBinder);

        install(new FactoryModuleBuilder().build(ICommandFactory.class));
        install(new FactoryModuleBuilder()
            .implement(EvaluateResult.class, Names.named("parsed"), EvaluateResult.Parsed.class)
            .implement(EvaluateResult.class, Names.named("analyzed"), EvaluateResult.Analyzed.class)
            .build(IResultFactory.class));
    }

}
