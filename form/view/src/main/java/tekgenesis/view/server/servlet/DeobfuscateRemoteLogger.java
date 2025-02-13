
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;

import com.google.gwt.logging.server.RemoteLoggingServiceImpl;

import org.jetbrains.annotations.NonNls;

import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;

import static tekgenesis.common.core.Constants.CLASSES_DIR;
import static tekgenesis.common.core.Constants.JAVA_CLASS_EXT;
import static tekgenesis.common.core.Option.option;

/**
 * By default, {@link RemoteLoggingServiceImpl} does not do any deobfuscation. In order to do server
 * side deobfuscation, we had to extend it and set the symbolMaps directory accordingly.
 */
public class DeobfuscateRemoteLogger extends RemoteLoggingServiceImpl {

    //~ Instance Fields ..............................................................................................................................

    private final Logger logger = Logger.getLogger(DeobfuscateRemoteLogger.class);

    //~ Methods ......................................................................................................................................

    @Override public void init()
        throws ServletException
    {
        super.init();
        for (final String webInfDir : getWebInfDir()) {
            final String symbolMapsPath = webInfDir + SYMBOL_MAPS_DIR;
            final File   symbolMapsDir  = new File(symbolMapsPath);

            if (!symbolMapsDir.exists()) {
                symbolMapsDir.mkdir();
                try {
                    final String symbolMapsZipPath = webInfDir + SYMBOL_MAPS_ZIP_DIR;
                    final File   symbolMapsZip     = new File(symbolMapsZipPath, SYMBOL_MAPS_ZIP);

                    if (symbolMapsZip.exists()) {
                        // Build ZipInputStream to symbolMaps zip file.
                        final ZipInputStream zis = new ZipInputStream(new FileInputStream(symbolMapsZip));

                        // First entry, to start iteration.
                        ZipEntry ze = zis.getNextEntry();

                        while (ze != null) {
                            // Create File to represent ZipEntry.
                            final File newFile = new File(symbolMapsPath + File.separator + ze.getName());
                            // Prepare an OutputStream to write to it.
                            final FileOutputStream fos = new FileOutputStream(newFile);

                            // Copy ZipEntry to new File.
                            Files.copy(zis, fos, false, true);

                            // Prepare to precces the next.
                            ze = zis.getNextEntry();
                        }

                        // Closes last entry.
                        zis.closeEntry();

                        // Finally close the ZipInputStream.
                        zis.close();

                        // Delete .zip file.
                        symbolMapsZip.delete();
                    }
                }
                catch (final IOException e) {
                    logger.warning("Error unzipping symbol maps, expect obfuscated client stack traces on server logs.", e);
                }
            }

            // Finally, set directory to symbolMaps dir.
            setSymbolMapsDirectory(symbolMapsPath);
        }
    }  // end method init

    private Option<String> getWebInfDir() {
        final URL resource = getClass().getResource(getClass().getSimpleName() + JAVA_CLASS_EXT);
        final int endIndex = resource.getFile().indexOf(CLASSES_DIR);
        return endIndex != -1 ? option(resource.getFile().substring(0, endIndex)) : Option.empty();
    }

    //~ Static Fields ................................................................................................................................

    public static final int BUFFER_SIZE = 1024;

    private static final long           serialVersionUID    = -2604052243336376264L;
    @NonNls private static final String SYMBOL_MAPS_DIR     = "deploy/sgforms/symbolMaps";
    @NonNls private static final String SYMBOL_MAPS_ZIP_DIR = "deploy/sgforms";
    @NonNls private static final String SYMBOL_MAPS_ZIP     = "symbolMaps.zip";
}  // end class DeobfuscateRemoteLogger
