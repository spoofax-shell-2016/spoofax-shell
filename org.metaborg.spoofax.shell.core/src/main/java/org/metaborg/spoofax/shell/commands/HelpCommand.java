package org.metaborg.spoofax.shell.commands;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.metaborg.core.MetaborgException;
import org.metaborg.spoofax.shell.client.IHook;
import org.metaborg.spoofax.shell.invoker.CommandNotFoundException;
import org.metaborg.spoofax.shell.invoker.ICommandInvoker;
import org.metaborg.spoofax.shell.output.StyledText;

import com.google.inject.Inject;

/**
 * This command prints help messages in two ways:
 * <p>
 * <ol>
 * <li>It lists all the available commands with their description,</li>
 * <li>It lists a single command with its description.</li>
 * </ol>
 * <p>
 * For the first, use {@code :help}. For the second, use {@code :help <commandname>}.
 */
public class HelpCommand implements IReplCommand {
    private static final String DESCRIPTION = "Display help messages\nEither use `:help` to list "
                                              + "all available commands, or `:help <command>` to "
                                              + "list a single command";
    private final ICommandInvoker invoker;

    /**
     * Instantiate a new HelpCommand.
     *
     * @param invoker
     *            The {@link ICommandInvoker} to retrieve the commands from.
     */
    @Inject
    public HelpCommand(ICommandInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    private String formathelp(Map<String, IReplCommand> commands) {
        int longestCommand = commands.keySet().stream().mapToInt(a -> a.length()).max().orElse(0);
        String format = "%-" + longestCommand + "s %s";

        return commands.keySet().stream().flatMap(name -> {
            String[] sname = name.split("\\R");
            String[] sdesc = commands.get(name).description().split("\\R");

            return IntStream.range(0, Math.max(sname.length, sdesc.length))
                    .<String>mapToObj(idx -> String.format(format,
                                                   idx < sname.length ? sname[idx] : "",
                                                   idx < sdesc.length ? sdesc[idx] : ""));
        }).collect(Collectors.joining("\n"));
    }

    @Override
    public IHook execute(String... args) throws MetaborgException {
        try {
            Map<String, IReplCommand> commands;
            if (args.length > 0) {
                IReplCommand command = invoker.commandFromName(args[0]);
                commands = Collections.singletonMap(args[0], command);
            } else {
                commands = invoker.getCommands();
            }

            return (display) -> display
                .displayMessage(new StyledText(formathelp(commands)));
        } catch (CommandNotFoundException e) {
            throw new MetaborgException("Command not found: " + e.commandName());
        }
    }

}
