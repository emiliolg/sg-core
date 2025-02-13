
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi.structure;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.newStructureView.StructureViewComponent;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import tekgenesis.lang.mm.psi.MMFile;

/**
 * Structure view factory for MM.
 */
public class MetaModelStructureViewFactory implements PsiStructureViewFactory {

    //~ Methods ......................................................................................................................................

    @Override public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile) {
        return (fileEditor, project) -> new MetaModelStructureViewComponent(fileEditor, new MetaModelStructureView((MMFile) psiFile), project);
    }

    //~ Inner Classes ................................................................................................................................

    private static class MetaModelStructureViewComponent extends StructureViewComponent {
        private MetaModelStructureViewComponent(FileEditor editor, StructureViewModel structureViewModel, Project project) {
            super(editor, structureViewModel, project, true);
        }

        private static final long serialVersionUID = 12324324233L;
    }
}
