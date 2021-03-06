package org.metaborg.spoofax.shell.core;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.meta.lang.dynsem.interpreter.terms.ITerm;
import org.metaborg.meta.lang.dynsem.interpreter.terms.ITermTransformer;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.oracle.truffle.api.vm.PolyglotEngine;

/**
 * Interface for loading a DynSem interpreter.
 */
public interface IInterpreterLoader {

    /**
     * Loads the DynSem interpreter for the given language implementation.
     *
     * @param langImpl
     *            A language implementation with a DynSem specification.
     * @return The DynSem entrypoint for the interpreter.
     * @throws InterpreterLoadException
     *             When loading results in an error.
     */
    PolyglotEngine loadInterpreterForLanguage(ILanguageImpl langImpl)
        throws InterpreterLoadException;

    /**
     * Returns a {@link ITerm} corresponding to the given {@link IStrategoAppl}.
     *
     * @param appl
     *            The {@link IStrategoAppl}.
     * @return The corresponding {@link ITerm}.
     * @throws InterpreterLoadException
     *             When finding the {@link ITerm} results in failure.
     */
    ITerm getProgramTerm(IStrategoAppl appl) throws InterpreterLoadException;

    /**
     * Returns the transformer which optionally transforms a given {@link IStrategoTerm} before
     * evaluation. By default this is the {@link ITermTransformer.IDENTITY identity transformation}.
     *
     * @return The {@link ITermTransformer} that is applied before evaluation.
     */
    ITermTransformer getTransformer();

    /**
     * Exception thrown when loading results in an error.
     */
    class InterpreterLoadException extends MetaborgException {

        /**
         * @param cause
         *            The cause for this exception.
         */
        InterpreterLoadException(Exception cause) {
            super(cause);
        }

        /**
         * @param string
         *            The message for this exception.
         */
        InterpreterLoadException(String string) {
            super(string);
        }

        /**
         * @param string
         *            The message for this exception.
         * @param cause
         *            The cause for this exception.
         */
        InterpreterLoadException(String string, Exception cause) {
            super(string, cause);
        }

        private static final long serialVersionUID = 2278699128224382308L;
    }
}
