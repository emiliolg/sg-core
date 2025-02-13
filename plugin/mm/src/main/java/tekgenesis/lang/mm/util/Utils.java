
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.util;

import java.io.File;
import java.io.IOException;

import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.logging.Logger;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.type.MetaModel;

import static tekgenesis.lang.mm.codeInspection.GeneratedTranslationInspection.RESOURCES;

/**
 */
public final class Utils {

    //~ Constructors .................................................................................................................................

    private Utils() {}

    //~ Methods ......................................................................................................................................

    /**
     * @param   mmPath  mm file path
     * @param   model   Model
     *
     * @return  MM File
     */
    @NotNull public static File ensureDestinationFile(@NotNull final String mmPath, @NotNull MetaModel model, @NotNull String extension,
                                                      @Nullable String articfactPath) {
        String directoryPath = getResourcesDirectoryPath(mmPath, articfactPath);
        directoryPath = directoryPath + model.getDomain().replaceAll("\\.", "/");

        ensureDestinationDirectory(directoryPath);
        final String fileName = model.getName() + "_" + extension;
        return new File(directoryPath + File.separator + fileName);
    }

    /**
     * Get MM source path.
     *
     * @param   dispatcher  MMFile Dispatcher
     *
     * @return  File
     */
    @NotNull public static String getMMSourcePath(@NotNull final MMFile dispatcher) {
        final VirtualFile root = PsiUtils.getSourceRootForFile(dispatcher);
        if (root == null) LOGGER.error("Attempting to find source root mm file for " + dispatcher + " failed.");
        return root != null ? root.getPath() : "";
    }

    /** Get or Create Folder. */
    public static VirtualFile getOrCreateDirectory(@NotNull VirtualFile rootDir, String name)
        throws IOException
    {
        final VirtualFile child = rootDir.findChild(name);

        if (child != null) return child;
        else {
            final File file = new File(rootDir.getCanonicalPath(), name);
            file.mkdir();
            return FileUtils.refreshAndFindVFile(file.getCanonicalPath());
        }
    }  // end method getOrCreateDirectory

    /**
     * @param   mmPath         mm file path
     * @param   articfactPath  artifact path
     *
     * @return  domain resources directory
     */
    @NotNull public static String getResourcesDirectoryPath(@NotNull String mmPath, @Nullable String articfactPath) {
        final String directoryPath = mmPath + "/../" + RESOURCES;
        return articfactPath != null ? directoryPath + articfactPath + File.separator : directoryPath;
    }

    /** @param  directoryPath  directory path */
    private static void ensureDestinationDirectory(@NotNull String directoryPath) {
        final File outputDirectory = new File(directoryPath);
        if (!outputDirectory.exists()) outputDirectory.mkdirs();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger LOGGER = Logger.getLogger(Utils.class);
}  // end class Utils
