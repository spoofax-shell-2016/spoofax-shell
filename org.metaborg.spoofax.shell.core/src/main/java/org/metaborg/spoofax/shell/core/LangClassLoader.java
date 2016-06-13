package org.metaborg.spoofax.shell.core;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.impl.VFSClassLoader;

public class LangClassLoader extends VFSClassLoader {

    public LangClassLoader(FileObject file, FileSystemManager manager, ClassLoader parent) throws FileSystemException {
        super(file, manager, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> loadClass = super.loadClass(name);
        System.out.println("Class name: " + name + " -> " + loadClass.getClassLoader());
        return loadClass;
    }
}
