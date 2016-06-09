package org.metaborg.spoofax.shell.invoker;

import java.util.Map;

import org.metaborg.spoofax.shell.commands.IReplCommand;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * Default implementation of an {@link ICommandInvoker}.
 */
public class SpoofaxCommandInvoker implements ICommandInvoker {
    private final ICommandFactory factory;
    private final Map<String, IReplCommand> defaults;
    private final Map<String, IReplCommand> commands;

    /**
     * Instantiate a new SpoofaxCommandInvoker.
     *
     * @param factory
     *            An {@link ICommandFactory} to create
     *            {@link org.metaborg.spoofax.shell.commands.AbstractSpoofaxCommand Spoofax
     *            commands}.
     * @param defaults
     *            The default commands, with the names they are bound to as key (without prefix).
     */
    @Inject
    public SpoofaxCommandInvoker(ICommandFactory factory, Map<String, IReplCommand> defaults) {
        this.factory = factory;
        this.defaults = defaults;
        this.commands = Maps.newConcurrentMap();
        this.resetCommands();
    }

    @Override
    public IReplCommand commandFromName(String commandName) throws CommandNotFoundException {
        if (!commands.containsKey(commandName)) {
            throw new CommandNotFoundException(commandName);
        }
        return commands.get(commandName);
    }

    @Override
    public String commandPrefix() {
        return ":";
    }

    @Override
    public void addCommand(String name, IReplCommand command) {
        commands.put(name, command);
    }

    @Override
    public Map<String, IReplCommand> getCommands() {
        return commands;
    }

    @Override
    public ICommandFactory getCommandFactory() {
        return factory;
    }

    @Override
    public void resetCommands() {
        commands.clear();
        commands.putAll(defaults);
    }
}
