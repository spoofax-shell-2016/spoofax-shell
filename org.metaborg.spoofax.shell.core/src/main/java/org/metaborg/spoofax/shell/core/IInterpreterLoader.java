package org.metaborg.spoofax.shell.core;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;

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
     * @throws InterpreterLoadException When loading results in an error.
     */
    PolyglotEngine loadInterpreterForLanguage(ILanguageImpl langImpl)
        throws InterpreterLoadException;

    /**
     * Exception thrown when loading results in an error.
     */
    class InterpreterLoadException extends MetaborgException {

        /**
         * @param e The reason for this exception.
         */
        InterpreterLoadException(Exception e) {
            super(e);
        }

        /**
         * @param string The message for this exception.
         */
        InterpreterLoadException(String string) {
            super(string);
        }

        private static final long serialVersionUID = 2278699128224382308L;
    }
}