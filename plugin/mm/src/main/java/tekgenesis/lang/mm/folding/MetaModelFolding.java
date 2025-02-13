
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.folding;

import java.util.ArrayList;
import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.field.FieldOption;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.*;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.mmcompiler.ast.MMToken;

import static java.util.Arrays.asList;

import static tekgenesis.lang.mm.injection.MetaModelLanguageInjector.isNotInjected;

/**
 * Folding builder for headers and footers in MM Forms, parameters in handler routes and searchable
 * by entity option.
 */
@SuppressWarnings("WeakerAccess")
public class MetaModelFolding implements FoldingBuilder {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode astNode, @NotNull Document document) {
        final PsiElement              psi    = astNode.getPsi();
        final List<FoldingDescriptor> result = new ArrayList<>();
        if (psi instanceof MMFile) {
            final MMFile file = (MMFile) psi;
            if (isNotInjected(file)) {
                collectFormHeaderFooterFolding(result, file);
                collectRouteParametersFolding(result, file);
                collectSearchableFolding(result, file);
            }
        }
        return result.toArray(new FoldingDescriptor[result.size()]);
    }

    @Override public String getPlaceholderText(@NotNull ASTNode node) {
        if (node instanceof MMCommonComposite) {
            final PsiElement parent = ((MMCommonComposite) node).getParent();
            if (parent instanceof PsiFieldOption) {
                final PsiFieldOption option = (PsiFieldOption) parent;
                if (option.getOption() == FieldOption.PARAMETERS) return getParametersPlaceholderText(option);
            }

            if (parent instanceof MMCommonComposite && ((MMCommonComposite) parent).getType() == MMToken.SEARCHABLE)
                return getPlaceholderFromEntityFields(((MMCommonComposite) node));
        }
        return DEFAULT_PLACEHOLDER_TEXT;
    }  // end method getPlaceholderText

    @Override public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return true;
    }

    private void collectElementList(List<FoldingDescriptor> result, MMCommonComposite element) {
        final ASTNode list = element.findChildByType(MMElementType.LIST);
        if (list != null) result.add(new FoldingDescriptor(list, list.getTextRange()));
    }

    private void collectFormHeaderFooterFolding(List<FoldingDescriptor> result, MMFile mmFile) {
        for (final PsiForm form : mmFile.getForms()) {
            for (final PsiWidget widget : form.getWidgets()) {
                final WidgetType widgetType = widget.getWidgetType();
                if (widgetType == WidgetType.FOOTER || widgetType == WidgetType.HEADER) collectElementList(result, widget);
            }
        }
    }

    private void collectRouteParametersFolding(List<FoldingDescriptor> result, MMFile file) {
        for (final PsiHandler handler : file.getHandlers()) {
            for (final PsiHandlerField route : handler.getFields()) {
                for (final PsiFieldOption option : route.getParametersOption())
                    collectElementList(result, option);
            }
        }
    }

    private void collectSearchableFolding(List<FoldingDescriptor> result, MMFile file) {
        for (final PsiEntity entity : file.getEntities()) {
            final MMCommonComposite searchableOpt = entity.getHighLevelOption(MMToken.SEARCHABLE);
            if (searchableOpt != null) collectElementList(result, searchableOpt);
        }
    }

    @NotNull private String getParametersPlaceholderText(PsiFieldOption option) {
        final MMCommonComposite fields = (MMCommonComposite) option.findPsiChildByType(MMElementType.LIST);
        return getPlaceholderFromEntityFields(fields);
    }

    @NotNull private String getPlaceholderFromEntityFields(@Nullable MMCommonComposite fields) {
        if (fields != null) {
            final List<PsiModelField> parameters = asList(fields.getChildrenAsPsiElements(MMElementType.ENTITY_FIELD, FIELDS_ARRAY_FACTORY));
            return Colls.map(parameters, MMCommonComposite::getName).mkString("{", ",", "}");
        }
        return DEFAULT_PLACEHOLDER_TEXT;
    }

    //~ Static Fields ................................................................................................................................

    private static final ArrayFactory<PsiModelField> FIELDS_ARRAY_FACTORY = PsiModelField[]::new;

    private static final String DEFAULT_PLACEHOLDER_TEXT = "{...}";
}  // end class MetaModelFolding
