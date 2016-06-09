package org.metaborg.spoofax.shell.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.shell.client.IHook;
import org.metaborg.spoofax.shell.output.IResultFactory;
import org.metaborg.spoofax.shell.output.ISpoofaxResult;

import com.google.inject.Inject;

/**
 * An abstract base command for processing a string as an expression in some language, in some way.
 */
public abstract class AbstractSpoofaxCommand implements IReplCommand {
    protected final IResultFactory resultFactory;
    protected final IProject project;
    protected final ILanguageImpl lang;

    /**
     * Instantiate a new AbstractSpoofaxCommand.
     *
     * @param resultFactory
     *            The {@link ResulFactory} to create {@link ISpoofaxResult results}.
     * @param project
     *            The project in which this command should operate.
     * @param lang
     *            The language to which this command applies.
     */
    @Inject
    AbstractSpoofaxCommand(IResultFactory resultFactory, IProject project, ILanguageImpl lang) {
        this.resultFactory = resultFactory;
        this.project = project;
        this.lang = lang;
    }

    /**
     * Write {@code source} to a temporary file. This is required by Spoofax (for e.g. tracing).
     *
     * @param source
     *            The source code to write to the temporary file.
     * @return A {@link FileObject} representing the temporary file.
     * @throws IOException
     *             When writing to the file fails.
     */
    protected FileObject write(String source) throws IOException {
        // FIXME: hardcoded file path
        FileObject sourceFile = this.project.location().resolveFile("tmp.src");
        try (OutputStream os = sourceFile.getContent().getOutputStream()) {
            os.write(source.getBytes(Charset.forName("UTF-8")));
        }

        return sourceFile;
    }

    @Override
    public abstract IHook execute(String... args) throws MetaborgException;
}
