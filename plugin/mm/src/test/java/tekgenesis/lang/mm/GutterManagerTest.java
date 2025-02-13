
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.GlobalSearchScope;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.gutter.GutterManager;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiEntity;

/**
 * Test for GutterManager.
 */

@SuppressWarnings("DuplicateStringLiteralInspection")
public class GutterManagerTest extends MMProjectTest {

    //~ Methods ......................................................................................................................................

    /** test that checks getMM. */
    public void testGetMM() {
        @SuppressWarnings("ConstantConditions")
        final PsiClass psiClass = myJavaFacade.findPackage("westeros").findClassByShortName("Jaime", GlobalSearchScope.allScope(myProject))[0];
        GutterManager.getMM(psiClass).ifPresent(mm -> {
            assertNotNull(mm);
            assertInstanceOf(mm, PsiEntity.class);
            assertEquals("invalid PsiClass Name", "Jaime", mm.getName());
        });
    }
    /** test that checks getPsiClass. */
    public void testGetPsiClass() {
        final Option<MMFile> mmFileOption = ProjectUtils.findMmFileInProject(myProject, "westeros", "Starks");
        assert (mmFileOption.isPresent());
        final MMFile mmFile = mmFileOption.get();
        mmFile.getMetaModel("Arya").ifPresent(arya -> {
            final PsiClass psiClass = GutterManager.getPsiClass(arya);
            assertNotNull(psiClass);
            assertInstanceOf(psiClass.getParent(), PsiJavaFile.class);
            assertFalse(psiClass.isInterface());
            assertEquals("invalid PsiClass Name", "Arya", psiClass.getName());
            assertEquals("invalid Amount of Fields in class Name", 1, psiClass.getAllFields().length);
        });
    }

    @Override protected String getProjectPath() {
        return TestMMGraph.PLUGIN_MM_TEST_PROJECT;
    }
}
