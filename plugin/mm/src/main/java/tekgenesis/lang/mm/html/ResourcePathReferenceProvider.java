
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.html;

import java.util.List;

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.lang.LangBundle;
import com.intellij.openapi.paths.PathReference;
import com.intellij.openapi.paths.PathReferenceProvider;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;

import static tekgenesis.common.core.Constants.EMPTY_OBJECT_ARRAY;
import static tekgenesis.common.core.Constants.RESOURCES;

/**
 * Path provider.
 */
public class ResourcePathReferenceProvider implements PathReferenceProvider {

    //~ Methods ......................................................................................................................................

    @Override public boolean createReferences(@NotNull PsiElement element, @NotNull List<PsiReference> references, boolean soft) {
        if (element instanceof XmlAttributeValue && startsWithAny(((XmlAttributeValue) element).getValue()))
            addHtmlReference((XmlAttributeValue) element, references, ((XmlAttributeValue) element).getValue().trim());
        return false;
    }

    @Nullable @Override public PathReference getPathReference(@NotNull String path, @NotNull PsiElement element) {
        return null;
    }

    private void addHtmlReference(@NotNull XmlAttributeValue element, @NotNull List<PsiReference> references, @NotNull String value) {
        references.clear();
        for (final String path : getAttributePaths(value.trim()))
            addPathReference(references, element, value, path);
    }

    private void addPathReference(@NotNull List<PsiReference> references, XmlAttributeValue xmlElement, String value, String path) {
        final String finalPath = path.startsWith(PUBLIC) ? path.substring(PUBLIC.length()) : path;
        final int    first     = value.indexOf(finalPath) + 1;
        final int    end       = first + finalPath.length();
        references.add(new StaticPathReference(xmlElement, TextRange.create(first, end), path));
    }

    private String resolveInnerPath(String value) {
        if (value.startsWith(XHTML_RESOURCE)) return value.substring(XHTML_RESOURCE.length(), value.length() - 1);
        if (value.startsWith(XHTML_SHA)) return PUBLIC + value.substring(XHTML_SHA.length(), value.length() - 1);
        if (value.startsWith(MUST_RES_OPEN)) return value.substring(MUST_RES_OPEN.length(), value.length() - MUST_RES_CLOSE.length());
        if (value.startsWith(MUST_SHA_OPEN)) return PUBLIC + value.substring(MUST_SHA_OPEN.length(), value.length() - MUST_SHA_CLOSE.length());
        return "";
    }

    private boolean startsWithAny(String value) {
        for (final String start : starts)
            if (value.startsWith(start)) return true;
        return false;
    }

    @NotNull private String[] getAttributePaths(final String value) {
        return resolveInnerPath(value).trim().split("\\s*,\\s*");
    }

    //~ Static Fields ................................................................................................................................

    private static final String       XHTML_RESOURCE = "@Resource(";
    private static final String       MUST_RES_OPEN  = "{{#resource}}";
    private static final String       MUST_RES_CLOSE = "{{/resource}}";
    private static final String       MUST_SHA_OPEN  = "{{#sha}}";
    private static final String       MUST_SHA_CLOSE = "{{/sha}}";
    private static final String       XHTML_SHA      = "@Sha(";
    private static final List<String> starts         = Colls.listOf(XHTML_RESOURCE, XHTML_SHA, MUST_RES_OPEN, MUST_SHA_OPEN);
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String       PUBLIC = "/public";

    //~ Inner Classes ................................................................................................................................

    static class StaticPathReference extends PsiReferenceBase<XmlAttributeValue> implements EmptyResolveMessageProvider {
        private final String path;

        /** Path reference from a path to a file. */
        StaticPathReference(@NotNull XmlAttributeValue element, TextRange range, String path) {
            super(element, range);
            this.path = path;
        }

        @Nullable @Override public PsiElement resolve() {
            final PsiFile file = getElement().getContainingFile();
            if (file != null) {
                final VirtualFile resourcesDirectory = getResourcesDirectory(file.getContainingDirectory());
                if (resourcesDirectory != null) {
                    final VirtualFile refVirtualFile = resourcesDirectory.findFileByRelativePath(path);
                    if (refVirtualFile != null) return PsiManager.getInstance(file.getProject()).findFile(refVirtualFile);
                }
            }
            return null;
        }

        @NotNull @Override public String getUnresolvedMessagePattern() {
            return LangBundle.message("error.cannot.resolve") + " " + LangBundle.message("terms.file") + " '" +
                   StringUtil.escapePattern(getCanonicalText()) + "'";
        }

        @NotNull @Override public Object[] getVariants() {
            return EMPTY_OBJECT_ARRAY;
        }

        @Nullable private VirtualFile getResourcesDirectory(@Nullable PsiDirectory directory) {
            if (directory == null) return null;
            else if (RESOURCES.equals(directory.getName())) return directory.getVirtualFile();
            else return getResourcesDirectory(directory.getParentDirectory());
        }
    }
}  // end class ResourcePathReferenceProvider
