
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.gutter;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.swing.*;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.lang.mm.psi.MMCommonComposite;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiForm;

import static com.intellij.codeHighlighting.Pass.LINE_MARKERS;
import static com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.LEFT;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.core.Constants.HTML_CLASS;
import static tekgenesis.common.core.Constants.HTML_EXT;
import static tekgenesis.common.core.Constants.TEMPLATE_EXTS;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.deCapitalizeFirst;
import static tekgenesis.lang.mm.gutter.GutterManager.*;
import static tekgenesis.lang.mm.psi.PsiUtils.*;

/**
 * Java navigation to meta models and inner tables and entities.
 */
public class JavaMMLineMarker implements LineMarkerProvider {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("rawtypes")
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {}

    @Override public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (element instanceof PsiClass) {
            final PsiClass clazz = (PsiClass) element;

            return getScopeClass(clazz)                                         //
                   .map(scope ->
                        getMM(scope)                                            //
                        .castTo(PsiForm.class)                                  //
                        .flatMap(form -> getFormTableLineMarkerInfo(clazz, form)))  //
                   .orElseGet(() ->
                        getMM(clazz)                                            //
                        .map(mm -> getMetaModelLineMarkerInfo(clazz, mm)))      //
                   .orElseGet(() -> extractDbObjectMarker(clazz));
        }

        if (element instanceof PsiMethod) {
            final PsiMethod method           = (PsiMethod) element;
            final PsiType   methodReturnType = method.getReturnType();

            if (belongsToInterface(method, HTML_FACTORY_CLASS) && methodReturnType != null && HTML_CLASS.equals(methodReturnType.getCanonicalText()))
                return getMethodToXhtmlLineMarkerInfo(method, method.getBody());
        }
        return null;
    }

    private boolean belongsToInterface(@NotNull final PsiMethod method, @NotNull final String interfaceName) {
        final PsiClass containingClass = method.getContainingClass();
        if (containingClass != null) {
            for (final PsiClass anInterface : containingClass.getInterfaces()) {
                if (interfaceName.equals(anInterface.getQualifiedName())) return true;
            }
        }
        return false;
    }

    @NotNull private String createMetaModelTooltip(@NotNull final MMCommonComposite mm) {
        final MMFile file = mm.getContainingFile();
        return IMPLEMENTS + mm.getName() + " in " + file.getPathToSourceRoot() + "." + file.getName();
    }

    @NotNull private String createTableTooltip(@NotNull final MMCommonComposite mm, @NotNull final PsiForm form) {
        final MMFile file = mm.getContainingFile();
        return IMPLEMENTS + mm.getName() + " of " + form.getName() + " in " + file.getPathToSourceRoot() + "." + file.getName();
    }

    @NotNull private Function<PsiElement, String> createXhtmlTooltip(final PsiMethod method, final PsiLiteralExpression path) {
        return psiLiteralExpression -> IMPLEMENTED_IN + method.getName() + " in " + path.getValue();
    }

    @Nullable private LineMarkerInfo<?> extractDbObjectMarker(PsiClass clazz) {
        final String fqn = clazz.getQualifiedName();
        if (fqn == null || (!fqn.endsWith(SEARCHER_SUFFIX) && !fqn.endsWith(TABLE_CLASS_NAME))) return null;

        final boolean isSearcherSuffix = fqn.endsWith(SEARCHER_SUFFIX);
        final String  dbObjectName     = getDbObjectName(fqn, isSearcherSuffix);

        return getMM(clazz, dbObjectName).map(mm -> getMetaModelLineMarkerInfo(clazz, mm)).getOrNull();
    }

    @NotNull private File resourcesDir(File sourcesDir) {
        return new File(sourcesDir.getParent(), Constants.RESOURCES);
    }

    @NotNull private String getDbObjectName(String fqn, boolean isSearcherSuffix) {
        final int end = fqn.lastIndexOf(isSearcherSuffix ? SEARCHER_SUFFIX : TABLE_CLASS_NAME);

        final String dbObjectFqn = fqn.substring(0, end);
        return isSearcherSuffix ? dbObjectFqn : Strings.replaceLast(dbObjectFqn, ".g.", ".");
    }

    private Option<LineMarkerInfo<?>> getFormTableLineMarkerInfo(@NotNull final PsiClass clazz, @NotNull final PsiForm mm) {
        final String table = clazz.getName();
        if (table == null) return empty();

        final int endIndex = table.indexOf(ROW_CLASS_SUFFIX);
        if (endIndex == -1) return empty();

        return Option.ofNullable(mm.getFieldNullable(deCapitalizeFirst(table.substring(0, endIndex))))  //
               .map(w -> {
            final PsiIdentifier id = clazz.getNameIdentifier();
            return id != null
            ? NavigationGutterIconBuilder.create(GutterManager.IMPLEMENTING_ICON)
                                         .setTarget(w)
                                         .setTooltipText(createTableTooltip(w, mm))
                                         .createLineMarkerInfo(id)
            : null;
        });
    }

    @Nullable private File getHtmlDirInElementModule(PsiElement element) {
        final Module module = ModuleUtilCore.findModuleForPsiElement(element);
        if (module != null) {
            final VirtualFile mmDir = MMModuleComponent.getMMDir(module);
            if (mmDir != null) {
                final File htmlDir = new File(resourcesDir(new File(mmDir.getPath())), HTML_EXT);
                return htmlDir.exists() ? htmlDir : null;
            }
        }
        return null;
    }

    @Nullable private LineMarkerInfo<?> getMetaModelLineMarkerInfo(@NotNull final PsiClass clazz, @NotNull final MMCommonComposite mm) {
        final NavigationGutterIconBuilder<PsiElement> iconBuilder = NavigationGutterIconBuilder.create(GutterManager.IMPLEMENTING_ICON)
                                                                    .setTarget(mm)
                                                                    .setTooltipText(createMetaModelTooltip(mm));

        final PsiIdentifier id = clazz.getNameIdentifier();
        return id != null ? iconBuilder.createLineMarkerInfo(id) : null;
    }

    @Nullable private LineMarkerInfo<?> getMethodToXhtmlLineMarkerInfo(final PsiMethod method, @Nullable final PsiCodeBlock body) {
        if (body != null) {
            final PsiLiteralExpression path = getPsiLiteralPath(body);
            if (path != null) {
                final VirtualFile fileByPath = getVirtualFileInHtmlPath(method, path);
                if (fileByPath != null) {
                    final PsiFile psiFile = PsiManager.getInstance(method.getProject()).findFile(fileByPath);

                    if (psiFile != null) return new NavigationMarker(method, psiFile, createXhtmlTooltip(method, path), IMPLEMENTED_ICON);
                }
            }
        }
        return null;
    }

    @Nullable private PsiLiteralExpression getPsiLiteralPath(PsiCodeBlock body) {
        final PsiStatement[]     statements = body.getStatements();
        final JavaLiteralVisitor visitor    = new JavaLiteralVisitor();
        for (final PsiStatement statement : statements)
            statement.acceptChildren(visitor);
        return visitor.getLiteralPath();
    }

    /** Return scope class option (table scope classes are form classes, forms have no scope). */
    private Option<PsiClass> getScopeClass(PsiClass clazz) {
        final PsiElement scope = clazz.getScope();
        return scope instanceof PsiClass ? some((PsiClass) scope) : empty();
    }

    @Nullable private VirtualFile getVirtualFileInHtmlPath(PsiMethod method, PsiLiteralExpression path) {
        final File        htmlDir     = getHtmlDirInElementModule(method);
        final VirtualFile virtualFile = method.getContainingFile().getVirtualFile();
        if (htmlDir != null && htmlDir.exists() && htmlDir.isDirectory() && virtualFile != null) {
            for (final String extension : TEMPLATE_EXTS) {
                final VirtualFile html = virtualFile.getFileSystem().findFileByPath(htmlDir.getPath() + path.getValue() + "." + extension);
                if (html != null) return html;
            }
        }
        return null;
    }

    //~ Inner Classes ................................................................................................................................

    private static class JavaLiteralVisitor extends JavaElementVisitor {
        private PsiLiteralExpression literalPath = null;

        @Override public void visitElement(final PsiElement psiElement) {
            for (final PsiElement e : psiElement.getChildren())
                e.acceptChildren(this);
        }

        @Override public void visitLiteralExpression(final PsiLiteralExpression expression) {
            final PsiElement parent = expression.getParent();
            if (parent != null) {
                final PsiElement parentsParent = parent.getParent();
                if (parentsParent instanceof PsiMethodCallExpression &&
                    HTML_EXT.equals(((PsiMethodCallExpression) parentsParent).getMethodExpression().getReferenceName())) literalPath = expression;
            }
        }

        @Nullable PsiLiteralExpression getLiteralPath()
        {
            return literalPath;
        }
    }

    static class NavigationMarker extends LineMarkerInfo<PsiElement> {
        /** Construct Navigation Marker. */
        NavigationMarker(@NotNull final PsiElement source, @NotNull final PsiElement target, @NotNull final Function<PsiElement, String> tooltip,
                         @NotNull Icon icon) {
            super(source,
                new TextRange(source.getTextOffset(), source.getTextOffset()),
                icon,
                LINE_MARKERS,
                tooltip,
                (e, s) -> scrollTo(target),
                LEFT);
        }
    }
}  // end class JavaMMLineMarker
