package org.metaborg.spoofax.shell.client.hooks;

import java.util.function.Consumer;

import org.metaborg.spoofax.shell.commands.IReplCommand;
import org.metaborg.spoofax.shell.output.StyledText;

/**
 * Called with the result of executing an {@link IReplCommand}.
 */
public interface IMessageHook extends Consumer<StyledText> {
}
