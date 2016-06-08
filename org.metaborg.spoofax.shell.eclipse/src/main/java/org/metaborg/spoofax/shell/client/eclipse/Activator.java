package org.metaborg.spoofax.shell.client.eclipse;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.metaborg.spoofax.shell.client.eclipse.impl.EclipseReplModule;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The Activator class controls the plugin's life cycle. It is instantiated by Eclipse
 * automatically. See {@link org.eclipse.core.runtime.Plugin#Plugin()} and
 * {@link AbstractUIPlugin#AbstractUIPlugin()} for more information.
 *
 * Currently its use is to keep track of certain objects that need to be available for the plugin.
 */
public class Activator extends AbstractUIPlugin {
    public static final String ID = "org.metaborg.spoofax.shell.eclipse";
    private static Activator plugin;
    private static Injector injector;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        Activator.plugin = this;
        // TODO: Add extension on Spoofax Eclipse (Meta?), load the module via them and use their
        // injector.
        Activator.injector = Guice.createInjector(new EclipseReplModule());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        Activator.injector = null;
        Activator.plugin = null;
        super.stop(context);
    }

    /**
     * Return the shared Activator instance.
     *
     * @return The shared Activator instance.
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Return the shared {@link Injector} instance.
     *
     * @return The shared {@link Injector} instance.
     */
    public static Injector injector() {
        return injector;
    }

    /**
     * Return the plugin's ID.
     *
     * @return The plugin's ID.
     */
    public static String getPluginID() {
        return getDefault().getBundle().getSymbolicName();
    }

    /**
     * Return an {@link ImageDescriptor} for the image file at the given plugin-relative path.
     *
     * @param path
     *            The plugin-relative path to the image.
     * @return The {@link ImageDescriptor} for the image at the given path.
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(getPluginID(), path);
    }

}
