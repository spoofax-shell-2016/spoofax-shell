package org.metaborg.spoofax.shell.ipython;

import java.io.InputStream;
import java.io.OutputStream;

import org.metaborg.spoofax.shell.client.IDisplay;
import org.metaborg.spoofax.shell.client.IEditor;
import org.metaborg.spoofax.shell.client.Repl;
import org.metaborg.spoofax.shell.client.ReplModule;

import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class IPythonModule extends ReplModule {

    private void configureUserInterface() {
        bind(IPythonKernel.class).in(Singleton.class);
        bind(IEditor.class).to(IPythonKernel.class);
        bind(IDisplay.class).to(IPythonKernel.class);

        bind(InputStream.class).annotatedWith(Names.named("in")).toInstance(System.in);
        bind(OutputStream.class).annotatedWith(Names.named("out")).toInstance(System.out);
        bind(OutputStream.class).annotatedWith(Names.named("err")).toInstance(System.err);

        bindConstant().annotatedWith(Names.named("historyPath"))
            .to(System.getProperty("user.home") + "/.spoofax_history");
    }

    @Override
    protected void configure() {
        super.configure();

        configureUserInterface();

        bind(Repl.class).in(Singleton.class);
    }

}
