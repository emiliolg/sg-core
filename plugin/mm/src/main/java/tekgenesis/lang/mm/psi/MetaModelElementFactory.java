
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * MetaModel Element Factory :).
 */
class MetaModelElementFactory {

    //~ Constructors .................................................................................................................................

    private MetaModelElementFactory() {}

    //~ Methods ......................................................................................................................................

    /** Creates an identifier element. */
    @NotNull public static PsiElement createIdentifier(@NotNull final Project project, @NotNull final String name) {
        final MMFile dummyFile = createEntityFile(project, "package dummy; entity " + name + "{}");
        return ensureNotNull(dummyFile.getEntity(name)).getIdentifier();
    }

    /** Creates a dummy mm file. */
    @NotNull private static MMFile createEntityFile(@NotNull final Project project, @NotNull final String text) {
        @NonNls final String filename = "dummy.mm";
        return PsiUtils.createMMFileFromText(project, filename, text);
    }
}
