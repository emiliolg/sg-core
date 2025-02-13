
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.jgoodies.common.base.Strings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.*;
import tekgenesis.mmcompiler.builder.QContext;

import static tekgenesis.lang.mm.MMElementType.IGNORABLE;
import static tekgenesis.lang.mm.MMElementType.SCHEMA;

/**
 * Helper class to deal with imports on psi environment.
 */
public class Imports {

    //~ Constructors .................................................................................................................................

    private Imports() {}

    //~ Methods ......................................................................................................................................

    /**
     * Adds new import to import list if it is needed.
     *
     * @return  false when the fqn have to be used in code (e.g. when conflicting imports already
     *          exist)
     */
    public static boolean addMetaModelImport(@NotNull MMFile file, @NotNull PsiMetaModel<?> model) {
        final String fqn = model.getFullName();
        if (Strings.isEmpty(fqn)) return false;

        final QContext context = file.getQContext();

        return !context.needsQualification(fqn) || (nameCanBeImported(fqn, context) && addImportForMetaModel(file, model));
    }

    /** Adds new import to import list if it is needed. */
    public static void addMetaModelImportIfNeeded(@NotNull PsiMetaModel<?> mm, @NotNull PsiMetaModelCodeReferenceElement element) {
        final MMFile file = element.getContainingFile();

        final String fqn = mm.getFullName();
        if (Strings.isEmpty(fqn)) return;

        final QContext context = file.getQContext();

        if (!context.needsQualification(fqn) || context.hasImportNameConflict(fqn)) return;

        addImportForMetaModel(file, mm);
    }

    /** Return true if meta model fqn can be imported. */
    public static boolean nameCanBeImported(@NotNull String fqn, QContext context) {
        return !context.hasImportNameConflict(fqn);
    }

    private static boolean addImportForMetaModel(@NotNull MMFile file, @NotNull PsiMetaModel<?> model) {
        final Project            project  = file.getProject();
        final PsiDocumentManager instance = PsiDocumentManager.getInstance(project);
        instance.commitAllDocuments();

        final Document document = FileDocumentManager.getInstance().getDocument(file.getViewProvider().getVirtualFile());
        if (document != null) {
            final PsiElement anchor = resolveNextImportLocation(file);
            if (anchor != null) {
                final String s = createImportStatement(model, needsWhiteSpace(anchor, true), needsWhiteSpace(anchor, false));
                document.insertString(anchor.getStartOffsetInParent() + anchor.getTextLength(), s);
                instance.commitAllDocuments();
                return true;
            }
        }
        return false;
    }

    private static PsiElement ahead(PsiElement element, int nth, MMElementType type) {
        PsiElement result = element;
        int        ahead  = nth + 1;
        while (ahead > 0 && result != null) {
            if (result.getNode().getElementType() == type) return result;
            result = result.getNextSibling();
            ahead--;
        }
        return null;
    }

    @NotNull private static String createImportStatement(@NotNull PsiMetaModel<?> model, boolean before, boolean after) {
        return (before ? "\n" : "") + "\nimport " + model.getFullName() + ";" + (after ? "\n" : "");
    }

    private static boolean hasExactImportConflict(String fqName, PsiJavaFile file) {
        final PsiImportList imports = file.getImportList();
        if (imports == null) return false;
        final PsiImportStatement[] importStatements = imports.getImportStatements();
        final int                  lastDotIndex     = fqName.lastIndexOf((int) '.');
        final String               shortName        = fqName.substring(lastDotIndex + 1);
        final String               dottedShortName  = '.' + shortName;
        for (final PsiImportStatement importStatement : importStatements) {
            final String importName = importStatement.getQualifiedName();
            if (importName == null) return false;
            if (!importName.equals(fqName) && importName.endsWith(dottedShortName)) return true;
        }
        return false;
    }

    private static boolean needsWhiteSpace(PsiElement anchor, boolean before) {
        if (before) {
            final PsiElement prev = anchor.getPrevSibling();
            return !(prev instanceof PsiWhiteSpace) || !(prev.getPrevSibling() instanceof PsiImport || prev.getTextLength() > 1);
        }

        final PsiElement next = anchor.getNextSibling();
        return !(next instanceof PsiWhiteSpace && next.getTextLength() > 1);
    }

    @Nullable private static PsiElement resolveNextImportLocation(@NotNull MMFile file) {
        final PsiElement anchor;

        final PsiImport[] imports = file.findChildrenByClass(PsiImport.class);
        if (imports.length > 0) anchor = imports[imports.length - 1];  // After last import
        else {
            final PsiDomain  domain = file.findChildByClass(PsiDomain.class);
            final PsiElement schema = ahead(domain, 2, SCHEMA);
            anchor = schema != null ? ahead(schema, 2, IGNORABLE) : domain;
        }

        if (anchor != null) {
            final PsiElement next = anchor.getNextSibling();
            if (next.getNode().getElementType() == IGNORABLE) return next;
        }
        return anchor;
    }  // end method resolveNextImportLocation
}  // end class Imports
