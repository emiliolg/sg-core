
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.io.*;
import java.util.List;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.NewVirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.PsiTestUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.util.Diff;

import static com.intellij.openapi.vfs.VirtualFileManager.getInstance;

import static org.junit.Assert.*;

/**
 * Util class for plugin tests.
 */
@SuppressWarnings("WeakerAccess")
public class TestUtil {

    //~ Constructors .................................................................................................................................

    private TestUtil() {}

    //~ Methods ......................................................................................................................................

    /** checks difference between two files. */
    public static boolean checkDiff(File outFile, File goldenFile)
        throws FileNotFoundException
    {
        final List<Diff.Delta<String>> diffs = Diff.stringDiffer(new Diff.Equals<>()).diff(new FileReader(outFile), new FileReader(goldenFile));
        if (!diffs.isEmpty()) fail("diff -y -W 150 " + outFile + " " + goldenFile + "\n" + Diff.makeString(diffs));
        return true;
    }

    /** Util for creating a PsiFile. */
    public static PsiFile createFile(@NotNull final Module module, @NotNull final VirtualFile vDir, @NotNull final String fileName,
                                     @NotNull final String text) {
        return new WriteAction<PsiFile>() {
                @Override protected void run(@NotNull Result<PsiFile> result)
                    throws Throwable
                {
                    if (!ModuleRootManager.getInstance(module).getFileIndex().isInSourceContent(vDir)) addSourceContentToRoots(module, vDir);

                    final VirtualFile vFile = vDir.findOrCreateChildData(vDir, fileName);
                    VfsUtil.saveText(vFile, text);
                    assertNotNull(vFile);
                    final PsiManager myPsiManager = PsiManager.getInstance(module.getProject());
                    final PsiFile    file         = myPsiManager.findFile(vFile);
                    assertNotNull(file);
                    result.setResult(file);
                }
            }.execute().getResultObject();
    }

    /** Find child on given parent after being refresh. */
    public static VirtualFile findChild(VirtualFile parent, String child) {
        parent.getFileSystem().refresh(false);
        return parent instanceof NewVirtualFile ? ((NewVirtualFile) parent).refreshAndFindChild(child) : parent.findChild(child);
    }

    /** find VFile for given Path. */
    @Nullable public static VirtualFile findVFile(String filePath) {
        return getInstance().refreshAndFindFileByUrl(filePath);
    }

    /** Reads the file in given path and returns it as String. */
    public static String readFile(String file) {
        try {
            return readFile(new FileReader(file));
        }
        catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /** Reads the given file and returns it as String. */
    public static String readFile(File file) {
        try {
            return readFile(new FileReader(file));
        }
        catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /** refresh File System. */
    public static void refreshFileSystem() {
        getInstance().syncRefresh();
    }

    private static void addSourceContentToRoots(final Module module, final VirtualFile vDir) {
        PsiTestUtil.addSourceContentToRoots(module, vDir);
    }

    private static String readFile(FileReader r) {
        final StringBuilder  fileData = new StringBuilder();
        final BufferedReader reader   = new BufferedReader(r);
        try {
            final char[] buf     = new char[BUFF_SIZE];
            int          numRead;
            while ((numRead = reader.read(buf)) != -1)
                fileData.append(buf, 0, numRead);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        try {
            reader.close();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return fileData.toString();
    }

    //~ Static Fields ................................................................................................................................

    public static final String TARGET_PLUGIN_MM_TEST_OUTPUT_ERRORS = "target/plugin/mm/test-output/errors";

    private static final int BUFF_SIZE = 1024;
}  // end class TestUtil
