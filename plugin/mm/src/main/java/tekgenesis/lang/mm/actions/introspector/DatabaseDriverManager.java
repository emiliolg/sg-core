
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions.introspector;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static tekgenesis.common.core.Constants.JAVA_CLASS_EXT;

/**
 * Manager of database drivers extracted from driver jar. Has to be instantiated to use instance
 * class loader
 */
class DatabaseDriverManager {

    //~ Constructors .................................................................................................................................

    /** Constructor for driver manager. */
    public DatabaseDriverManager() {}

    //~ Methods ......................................................................................................................................

    /** Load drivers from a jar file path. */
    public List<Driver> loadDrivers(String libraryPath) {
        final List<Driver> drivers = new ArrayList<>();
        if (new File(libraryPath).isFile()) {
            try {
                final URL[]          urls        = { new File(libraryPath).toURI().toURL() };
                final URLClassLoader classLoader = URLClassLoader.newInstance(urls, getClass().getClassLoader());

                final JarFile               jarFile = new JarFile(libraryPath);
                final Enumeration<JarEntry> entries = jarFile.entries();
                addDriverEntries(drivers, classLoader, entries);
            }
            catch (final Exception ignored) {}
        }
        return drivers;
    }

    private void addDriverEntries(List<Driver> drivers, URLClassLoader classLoader, Enumeration<JarEntry> entries)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String   name  = entry.getName();
            if (name.endsWith(JAVA_CLASS_EXT)) {
                final int index     = name.lastIndexOf('.');
                String    className = name.substring(0, index);
                className = className.replace('/', '.').replace('\\', '.');

                if (className.contains("Driver")) {
                    final Class<?> clazz = classLoader.loadClass(className);
                    if (Driver.class.isAssignableFrom(clazz)) {
                        final Driver driver = (Driver) clazz.newInstance();
                        drivers.add(driver);
                    }
                }
            }
        }
    }
}  // end class DatabaseDriverManager
