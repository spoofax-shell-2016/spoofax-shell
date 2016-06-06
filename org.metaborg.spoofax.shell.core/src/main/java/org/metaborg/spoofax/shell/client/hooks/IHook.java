package org.metaborg.spoofax.shell.client.hooks;

import java.util.function.Consumer;

import org.metaborg.spoofax.shell.client.IDisplay;

/**
 * Typedef interface.
 */
public interface IHook extends Consumer<IDisplay> {

    @Override
    void accept(IDisplay display);

}
