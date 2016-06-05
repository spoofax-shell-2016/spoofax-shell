package org.metaborg.spoofax.shell.client.hooks;

import java.util.function.Consumer;

import org.metaborg.spoofax.shell.commands.AbstractSpoofaxCommand;
import org.metaborg.spoofax.shell.output.ISpoofaxResult;

/**
 * Called with the result of executing a {@link AbstractSpoofaxCommand}.
 *
 * @see {@link ISpoofaxResult}.
 */
public interface IResultHook extends Consumer<ISpoofaxResult<?>> {
}