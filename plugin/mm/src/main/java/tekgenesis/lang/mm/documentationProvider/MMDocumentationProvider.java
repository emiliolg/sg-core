
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.documentationProvider;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.intellij.lang.documentation.CodeDocumentationProvider;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.MMLeafElement;
import tekgenesis.lang.mm.psi.PsiEntityField;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.lang.mm.psi.PsiWidgetType;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.Strings.toCamelCase;
import static tekgenesis.common.util.Files.readInput;
import static tekgenesis.lang.mm.psi.PsiUtils.*;

/**
 * Provider for mm quick documentation.
 */
public class MMDocumentationProvider implements CodeDocumentationProvider {

    //~ Methods ......................................................................................................................................

    @Override public PsiComment findExistingDocComment(PsiComment psiComment) {
        return null;
    }

    /** Generate doc for widget. */
    @Override public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        Option<String> doc = Option.empty();
        if (isTypeNodeOrWidget(element)) doc = docFromWidgetChild(element);
        else if (element instanceof PsiEntityField) {
            if (isTypeNodeOrWidget(originalElement)) doc = docFromWidgetChild(originalElement);
        }
        return doc.isPresent() ? doc.get() : NO_DOCUMENTATION_FOUND;
    }

    @Nullable @Override public String generateDocumentationContentStub(PsiComment contextComment) {
        return null;
    }

    @Nullable @Override public Pair<PsiElement, PsiComment> parseContext(@NotNull PsiElement startPoint) {
        return null;
    }

    @Nullable @Override public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
        return null;
    }

    @Override public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object o, PsiElement psiElement) {
        if (psiElement instanceof MMLeafElement) {
            final MMLeafElement leaf = cast(psiElement);
            leaf.setPosibleCompletion(o.toString());
            return leaf;
        }

        if (!(o instanceof MetaModel)) return psiElement;

        final MetaModel mm = (MetaModel) o;

        return findMMFileIncludingNonProjectItems(psiElement.getProject(), getModuleOrFail(psiElement), mm.getSourceName())  //
               .flatMap(f -> f.getMetaModel(mm.getName()))                                                                   //
               .castTo(PsiElement.class)                                                                                     //
               .orElse(psiElement);
    }

    @Nullable @Override public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Nullable @Override public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
        return null;
    }

    private Option<String> docFromWidgetChild(@Nullable PsiElement element) {  //
        return ofNullable(element).flatMap(e -> findParentOfType(element, PsiWidget.class)).map(PsiWidget::getWidgetType).map(type -> {
            final String      widgetId         = toCamelCase(type.getId());
            final InputStream resourceAsStream = MMDocumentationProvider.class.getResourceAsStream(
                    "/plugin-doc/widgets." + widgetId.toLowerCase() + ".html");
            return readInput(new InputStreamReader(resourceAsStream));
        });
    }

    private boolean isTypeNodeOrWidget(@Nullable PsiElement element) {
        return findParentOfType(element, PsiWidgetType.class).isPresent() || findParentWithElementType(element, MMElementType.TYPE).isPresent();
    }

    //~ Static Fields ................................................................................................................................

    public static final String NO_DOCUMENTATION_FOUND = "No Documentation Found";
}  // end class MMDocumentationProvider
