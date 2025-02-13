
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.gutter;

import java.util.Collection;
import java.util.List;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlAttribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.gutter.JavaMMLineMarker.NavigationMarker;

import static tekgenesis.codegen.html.HtmlFactoryCodeGenerator.DEFAULT_CLASS_NAME;
import static tekgenesis.codegen.html.HtmlFactoryCodeGenerator.resolveIdentifier;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.lang.mm.gutter.GutterManager.IMPLEMENTED_IN;
import static tekgenesis.lang.mm.gutter.GutterManager.IMPLEMENTING_ICON;
import static tekgenesis.lang.mm.psi.PsiUtils.getPsiClassForFqnNullable;

/**
 * Line Marker Provider for XHTML files.
 */
public class XhtmlMMLineMarker implements LineMarkerProvider {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("rawtypes")
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> result) {}

    @Nullable @Override public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (psiElement instanceof XmlAttribute) {
            final XmlAttribute attrValue = (XmlAttribute) psiElement;
            final String       value     = attrValue.getValue();

            if (("sg-view".equals(attrValue.getName()) || "sui-view".equals(attrValue.getName())) && value != null) {
                final PsiClass clazz          = getPsiClassForFqnNullable(attrValue.getProject(), isEmpty(value) ? DEFAULT_CLASS_NAME : value);
                final PsiFile  containingFile = psiElement.getContainingFile();
                if (clazz != null && containingFile != null) {
                    final VirtualFile virtualFile = containingFile.getVirtualFile();
                    if (virtualFile != null) {
                        final PsiMethod[] methodsByName = clazz.findMethodsByName(resolveIdentifier(virtualFile.getPath()), false);
                        if (methodsByName.length == 1 && methodsByName[0] != null) return getSuiViewLineMarker(attrValue, clazz, methodsByName[0]);
                    }
                }
            }
        }
        return null;
    }

    @NotNull private LineMarkerInfo<?> getSuiViewLineMarker(final XmlAttribute attrValue, final PsiClass clazz, final PsiMethod psiMethod) {
        return new NavigationMarker(attrValue.getParent(),
            psiMethod,
            ast -> IMPLEMENTED_IN + clazz.getQualifiedName() + "." + psiMethod.getName(),
            IMPLEMENTING_ICON);
    }
}  // end class XhtmlMMLineMarker
