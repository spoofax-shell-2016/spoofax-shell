package org.metaborg.spoofax.shell.commands;

import java.util.Set;
import java.util.function.Function;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.analysis.AnalyzerFacet;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryRequest;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.LanguageUtils;
import org.metaborg.core.menu.IMenuService;
import org.metaborg.core.project.IProject;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.client.hooks.IHook;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;
import org.metaborg.spoofax.shell.output.StyledText;
import org.metaborg.spoofax.shell.output.TransformResult;

import com.google.inject.Inject;

/**
 * Represents a command that loads a Spoofax language.
 */
public class LanguageCommand implements IReplCommand {
    private final ILanguageDiscoveryService langDiscoveryService;
    private final IResourceService resourceService;
    private final IMenuService menuService;
    private final ICommandInvoker invoker;
    private final IProject project;
    private ILanguageImpl lang;

    /**
     * Instantiate a {@link LanguageCommand}. Loads all commands applicable to a lanugage.
     *
     * @param langDiscoveryService
     *            the {@link ILanguageDiscoveryService}
     * @param resourceService
     *            the {@link IResourceService}
     * @param invoker
     *            the {@link ICommandInvoker}
     * @param menuService
     *            the {@link IMenuService}
     * @param project
     *            the associated {@link IProject}
     */
    @Inject
    public LanguageCommand(ILanguageDiscoveryService langDiscoveryService,
                           IResourceService resourceService, IMenuService menuService,
                           ICommandInvoker invoker,
                           IProject project) { // FIXME: don't use the hardcoded @Provides
        this.langDiscoveryService = langDiscoveryService;
        this.resourceService = resourceService;
        this.menuService = menuService;
        this.invoker = invoker;
        this.project = project;
    }

    @Override
    public String description() {
        return "Load a language from a path.";
    }

    private ILanguageImpl load(FileObject langloc) throws MetaborgException {
        Iterable<ILanguageDiscoveryRequest> requests = langDiscoveryService.request(langloc);
        Iterable<ILanguageComponent> components = langDiscoveryService.discover(requests);

        Set<ILanguageImpl> implementations = LanguageUtils.toImpls(components);
        lang = LanguageUtils.active(implementations);

        if (lang == null) {
            throw new MetaborgException("Cannot find a language implementation");
        }
        return lang;
    }

    @Override
    public IHook execute(String arg) throws MetaborgException {
        if (arg == null || arg.length() == 0) {
            throw new MetaborgException("Syntax: :lang <path>");
        }

        FileObject resolve = resourceService.resolve("zip:" + arg + "!/");
        ILanguageImpl lang = load(resolve);
        boolean analyze = lang.hasFacet(AnalyzerFacet.class);

        CommandBuilder builder = invoker.getCommandFactory().createBuilder(project, lang);

        invoker.resetCommands();
        invoker.addCommand("parse", builder.build(builder.parse(), "Parse the expression"));
        if (analyze) {
            invoker.addCommand("analyze", builder.build(builder.analyze(), "Analyze the expression"));
        }

        new TransformVisitor(menuService).getActions(lang).forEach((key, action) -> {
            Function<String, TransformResult> result;
            if (analyze) {
                result = builder.transformAnalyzed(action);
            } else {
                result = builder.transformParsed(action);
            }
            invoker.addCommand(key, builder.build(result, action.name()));
        });

        return (IDisplay display) -> display
            .displayMessage(new StyledText("Loaded language " + lang));
    }

}
