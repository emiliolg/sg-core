
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashSet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.lesscss.LessException;

import tekgenesis.common.core.Constants;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;
import tekgenesis.form.WebResourceManager;

import static java.lang.Thread.currentThread;
import static java.util.Collections.list;

import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.security.shiro.web.URLConstants.FORMS_CSS;
import static tekgenesis.security.shiro.web.URLConstants.FORMS_LESS;
import static tekgenesis.security.shiro.web.URLConstants.STYLE_CSS;
import static tekgenesis.security.shiro.web.URLConstants.SUIGENERIS_CSS;

public class StyleContextListener implements ServletContextListener {

    //~ Methods ......................................................................................................................................

    @Override public void contextDestroyed(ServletContextEvent sce) {}

    /** Merge all user added styles and merge all suigeneris own styles. */
    @Override public void contextInitialized(ServletContextEvent sce) {
        mergeAll();
        merge(SUIGENERIS_CSS,
            "/public/sg/css/gwt-theme.css",
            SUIGENERIS_CSS,
            "/public/sg/css/predefined.css",
            "/public/sg/css/exception.css",
            "/public/sg/css/font-awesome.min.css");
    }

    private int copy(String path, FileOutputStream out) {
        final InputStream in = currentThread().getContextClassLoader().getResourceAsStream(path);
        if (in == null) throw new UncheckedIOException(new FileNotFoundException(path));
        return Files.copy(in, out, false);
    }

    private void merge(String path, String... paths) {
        final File resourceOut = WebResourceManager.getCompiledResource(path);
        resourceOut.getParentFile().mkdirs();

        try {
            final FileOutputStream out = new FileOutputStream(resourceOut);

            for (final String p : paths)
                copy(p, out);

            out.flush();
            out.close();
            WebResourceManager.getInstance().replaceShaPath(path, resourceOut);
        }
        catch (final IOException e) {
            logger.error("Failed to merge Suigeneris static resource: " + path, e);
        }
    }

    private void mergeAll() {
        try {
            final ClassLoader loader = currentThread().getContextClassLoader();

            final HashSet<URL> resources = new HashSet<>(list(loader.getResources(FORMS_LESS)));
            final boolean      anyLess   = !resources.isEmpty();
            resources.addAll(new HashSet<>(list(loader.getResources(STYLE_CSS))));

            if (!resources.isEmpty()) {
                final WebResourceManager webResourceManager = WebResourceManager.getInstance();

                final File lessFile = WebResourceManager.getCompiledResource(FORMS_LESS);
                lessFile.getParentFile().mkdirs();

                final FileOutputStream out = new FileOutputStream(lessFile);

                for (final URL res : resources) {
                    final InputStream in     = res.openStream();
                    final byte[]      buffer = new byte[Constants.KILO];
                    int               read;
                    while ((read = in.read(buffer)) != -1)
                        out.write(buffer, 0, read);
                    out.write('\n');
                    in.close();
                }
                out.flush();
                out.close();

                final File compiled = new File(lessFile.getPath().replace(FORMS_LESS, FORMS_CSS));

                if (anyLess) webResourceManager.compileLessResources(lessFile, compiled);
                else Files.copy(lessFile, compiled, true);

                lessFile.delete();

                webResourceManager.replaceShaPath(FORMS_CSS, compiled);
            }
        }
        catch (final LessException | IOException e) {
            logger.error("Failed to merge static forms.less or style.css files", e);
        }
    }  // end method mergeAll

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(StyleContextListener.class);
}  // end class StyleContextListener
