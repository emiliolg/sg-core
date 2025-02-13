
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.facet;

import java.util.Collection;
import java.util.List;

import com.intellij.facet.FacetType;
import com.intellij.framework.detection.DetectedFrameworkDescription;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.framework.detection.FileContentPattern;
import com.intellij.framework.detection.FrameworkDetectionContext;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.MMFileType;

import static tekgenesis.lang.mm.MetaModelJpsUtils.META_MODEL_SOURCES_PATH;
import static tekgenesis.lang.mm.MetaModelJpsUtils.META_MODEL_TEST_PATH;

/**
 * MM Framework Detector.
 */
class MMFrameworkDetector extends FacetBasedFrameworkDetector<MMFacet, MMFacetConfiguration> {

    //~ Constructors .................................................................................................................................

    protected MMFrameworkDetector() {
        super("mm.detector");
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public ElementPattern<FileContent> createSuitableFilePattern() {
        return FileContentPattern.fileContent();
    }

    @Override public List<? extends DetectedFrameworkDescription> detect(@NotNull Collection<VirtualFile>   newFiles,
                                                                         @NotNull FrameworkDetectionContext context) {
        final Seq<VirtualFile> filteredFiles = Colls.seq(newFiles).filter(virtualFile ->
                    virtualFile != null &&
                    (virtualFile.getPath().contains(META_MODEL_SOURCES_PATH) || virtualFile.getPath().contains(META_MODEL_TEST_PATH)) &&
                    !virtualFile.getPath().contains("src/test/data"));
        return super.detect(filteredFiles.toList(), context);
    }

    @NotNull @Override public FacetType<MMFacet, MMFacetConfiguration> getFacetType() {
        return new MMFacetType();
    }

    @NotNull @Override public FileType getFileType() {
        return MMFileType.INSTANCE;
    }
}  // end class MMFrameworkDetector
