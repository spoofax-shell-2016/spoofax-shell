package org.metaborg.spoofax.shell.ipython;

import java.io.IOException;

import org.metaborg.spoofax.shell.client.Repl;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class IPythonRepl {

    public static void main(String[] args) {
        try {
            Injector injector = Guice.createInjector(new IPythonModule());
            Repl repl = injector.getInstance(Repl.class);
            repl.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
