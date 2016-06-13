package org.metaborg.spoofax.shell.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.VFSClassLoader;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.meta.lang.dynsem.interpreter.DynSemEntryPoint;
import org.metaborg.meta.lang.dynsem.interpreter.DynSemLanguage;
import org.metaborg.meta.lang.dynsem.interpreter.IDynSemLanguageParser;
import org.metaborg.meta.lang.dynsem.interpreter.ITermRegistry;
import org.metaborg.meta.lang.dynsem.interpreter.nodes.rules.RuleRegistry;
import org.metaborg.meta.lang.dynsem.interpreter.terms.ITerm;
import org.metaborg.meta.lang.dynsem.interpreter.terms.ITermTransformer;
import org.metaborg.spoofax.shell.util.StrategoUtil;
import org.spoofax.interpreter.terms.IStrategoTerm;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import com.oracle.truffle.api.vm.PolyglotEngine.Builder;

/**
 * Loads an interpreter that is present in the class path. This {@link IInterpreterLoader} uses
 * reflection to load the generated {@link DynSemEntryPoint} and {@link DynSemLanguage} subclasses.
 * It instantiates a {@link PolyglotEngine} with the {@link NonParser} that is provided, by using
 * the supported {@link DynSemLanguage#PARSER configuration parameter}.
 */
public class ClassPathInterpreterLoader implements IInterpreterLoader {
    private String langName;
    private String targetPackage;
    private ITermTransformer transformer;
    private VFSClassLoader loader;

    @Override
    public PolyglotEngine loadInterpreterForLanguage(ILanguageImpl langImpl)
        throws InterpreterLoadException {
        String path = "file:///opt/spoofax-workspace/metaborg-sl/org.metaborg.lang.sl.interp/target/sl.interpreter-0.1.jar";

        try {
            FileSystemManager manager = VFS.getManager();
            loader = new LangClassLoader(manager.resolveFile(path), manager,
                                         this.getClass().getClassLoader());
        } catch (FileSystemException e1) {
            e1.printStackTrace();
        }

        loadDynSemProperties(langImpl);

        DynSemEntryPoint entryPoint = getEntryPoint();

        transformer = entryPoint.getTransformer();
        IDynSemLanguageParser parser = entryPoint.getParser();
        RuleRegistry ruleRegistry = entryPoint.getRuleRegistry();
        ITermRegistry termRegistry = entryPoint.getTermRegistry();

        String mimeType = entryPoint.getMimeType();
        Thread.currentThread().setContextClassLoader(loader);
        PolyglotEngine builtEngine = null;
        try {
            Class<PolyglotEngine.Builder> polyBuilderType = (Class<PolyglotEngine.Builder>)
                    ClassUtils.getClass(loader, PolyglotEngine.Builder.class.getCanonicalName());
            Class<PolyglotEngine> polyEngineType = (Class<PolyglotEngine>)
                    ClassUtils.getClass(loader, PolyglotEngine.class.getCanonicalName());

            Method method = ClassUtils.getPublicMethod(polyEngineType, "newBuilder", new Class<?>[] { });
            PolyglotEngine.Builder builder = (Builder) method.invoke(null, null);
            builtEngine = builder.config(mimeType, DynSemLanguage.PARSER, parser)
                    .config(mimeType, DynSemLanguage.RULE_REGISTRY, ruleRegistry)
                    .config(mimeType, DynSemLanguage.TERM_REGISTRY, termRegistry).build();
            System.out.println();
            System.out.println(builder.getClass().getClassLoader());
            System.out.println(polyEngineType.getClassLoader());

            InputStreamReader specTermReader =
                new InputStreamReader(entryPoint.getSpecificationTerm(), "UTF-8");
            builtEngine.eval(Source.fromReader(specTermReader, "Evaluate to interpreter.")
                .withMimeType(mimeType));
        } catch (IOException | ReflectiveOperationException e) {
            throw new InterpreterLoadException(e);
        }
        return builtEngine;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ITerm getProgramTerm(IStrategoTerm input) throws InterpreterLoadException {
        try {
            String termSort = StrategoUtil.getSortForTerm(input);
            // Get the abstract class for the sort of the term.
            Class<? extends ITerm> generatedTermClass = (Class<? extends ITerm>) ClassUtils
                .getClass(targetPackage + ".terms.I" + termSort + "Term");
            return (ITerm) MethodUtils.invokeStaticMethod(generatedTermClass, "create", input);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException
                 | InvocationTargetException cause) {
            throw new InterpreterLoadException("Error constructing program term from input.",
                                               cause);
        }
    }

    @Override
    public ITermTransformer getTransformer() {
        return transformer;
    }

    private DynSemEntryPoint getEntryPoint() throws InterpreterLoadException {
        try {
            Class<DynSemEntryPoint> entryPointClass =
                this.<DynSemEntryPoint> getGeneratedClass("EntryPoint");
            return ConstructorUtils.invokeConstructor(entryPointClass);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                 | InvocationTargetException e) {
            throw new InterpreterLoadException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getGeneratedClass(String className) throws InterpreterLoadException {
        try {
            return (Class<T>) ClassUtils.getClass(loader, targetPackage + "." + langName + className);
        } catch (ClassNotFoundException e) {
            System.out.println("Is this where loading fails?");
            throw new InterpreterLoadException(e);
        }
    }

    /* Loads the required configurations from the dynsem.properties file parsed as a Properties
     * object. */
    private void loadDynSemProperties(ILanguageImpl langImpl) throws InterpreterLoadException {
        FileObject dynSemPropertiesFile = findDynSemPropertiesFileForLanguage(langImpl);

        Properties dynSemProperties = new Properties();
        try (InputStream in = dynSemPropertiesFile.getContent().getInputStream()) {
            dynSemProperties.load(in);
        } catch (IOException e) {
            throw new InterpreterLoadException("Error when trying to load \"dynsem.properties\".");
        }

        langName = dynSemProperties.getProperty("source.langname");
        String groupId = dynSemProperties.getProperty("project.groupid");
        String artifactId = dynSemProperties.getProperty("project.artifactid");
        targetPackage = dynSemProperties.getProperty("project.javapackage",
                                                     groupId + '.' + artifactId + ".generated");
    }

    private FileObject findDynSemPropertiesFileForLanguage(ILanguageImpl langImpl)
        throws InterpreterLoadException {
        FileObject dynSemPropertiesFile = null;
        for (FileObject fo : langImpl.locations()) {
            try {
                dynSemPropertiesFile = fo.getChild("dynsem.properties");
                if (dynSemPropertiesFile != null) {
                    break;
                }
            } catch (FileSystemException e) {
                continue;
            }
        }

        if (dynSemPropertiesFile == null) {
            throw new InterpreterLoadException("Missing \"dynsem.properties\" file");
        }
        return dynSemPropertiesFile;
    }
}
