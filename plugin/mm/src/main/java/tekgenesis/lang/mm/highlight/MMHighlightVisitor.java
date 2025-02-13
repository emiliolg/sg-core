
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.highlight;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.psi.*;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.parser.Highlight;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.lang.mm.psi.MetaModelReferenceKind.PACKAGE_NAME_KIND;
import static tekgenesis.lang.mm.psi.MetaModelReferences.getReferenceKind;
import static tekgenesis.lang.mm.psi.MetaModelReferences.isNotBasicType;
import static tekgenesis.parser.Highlight.TYPE_H;

/**
 * MM Highlighter Visitor.
 */
class MMHighlightVisitor extends MetaModelElementVisitor implements HighlightVisitor {

    //~ Instance Fields ..............................................................................................................................

    private HighlightInfoHolder holder = null;

    //~ Methods ......................................................................................................................................

    @Override public boolean analyze(@NotNull PsiFile psiFile, boolean b, @NotNull HighlightInfoHolder infoHolder, @NotNull Runnable runnable) {
        holder = infoHolder;
        try {
            runnable.run();
        }
        finally {
            holder = null;
        }
        return true;
    }

    @NotNull @Override
    @SuppressWarnings({ "CloneDoesntCallSuperClone", "CloneDoesntDeclareCloneNotSupportedException" })
    public HighlightVisitor clone() {
        return new MMHighlightVisitor();
    }

    @Deprecated @Override public int order() {
        return 0;
    }

    @Override public boolean suitableForFile(@NotNull PsiFile psiFile) {
        return psiFile.getFileType().equals(MMFileType.INSTANCE);
    }

    @Override public void visit(@NotNull PsiElement element) {
        element.accept(this);
    }

    @Override public void visitElement(PsiElement element) {
        ProgressIndicatorProvider.checkCanceled();

        if (element instanceof MetaModelAST) {
            final MetaModelAST node      = (MetaModelAST) element;
            final Highlight    highlight = node.getType().getHighlight();
            final MMToken      type      = node.getType();
            switch (type) {
            case WIDGET_FIELD:
                highlightWidget(node);
                break;
            case WIDGET_TYPE:
                highlightWidgetType(node);
                break;
            case FIELD_REF:
                createHighlight(element.getTextRange(), highlight, 0);
                break;
            case REFERENCE:
                highlightReference(element, highlight);
                break;
            default:
                if (highlight != Highlight.PLAIN_H) {
                    PsiElement firstChild = element.getFirstChild();
                    if ((firstChild instanceof MetaModelAST) && ((MetaModelAST) firstChild).hasType(MMToken.DOCUMENTATION) &&
                        element.getChildren().length > 1) firstChild = element.getChildren()[1];
                    createHighlight(firstChild != null ? firstChild.getTextRange() : element.getTextRange(), highlight, getFontType(element));
                }
                break;
            }
        }
    }

    @Override public void visitReferenceElement(PsiMetaModelCodeReferenceElement reference)
    {
        super.visitReferenceElement(reference);
        // Add unresolved reference fixes
        holder.add(Highlights.checkReference(reference));
    }

    private void createHighlight(TextRange textRange, Highlight highlight, int fontType) {
        final TextAttributesKey textAttributesKey = MMHighlighterColors.forKind(highlight);
        final TextAttributes    attributes        = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(textAttributesKey).clone();
        if (fontType != -1) attributes.setFontType(fontType);
        final HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INJECTED_LANGUAGE_FRAGMENT)
                                              .range(textRange)
                                              .textAttributes(attributes);
        holder.add(builder.create());
    }

    private void highlightReference(PsiElement element, Highlight highlight) {
        if (element instanceof PsiMetaModelCodeReferenceElement) {
            final PsiMetaModelCodeReferenceElement e    = (PsiMetaModelCodeReferenceElement) element;
            final MetaModelReferenceKind           kind = getReferenceKind(e);
            if (kind != PACKAGE_NAME_KIND) {
                if (isNotBasicType(e)) createHighlight(element.getTextRange(), highlight, getFontType(element));
                else createHighlight(element.getTextRange(), TYPE_H, getFontType(element));
            }
        }
    }

    private void highlightWidget(MetaModelAST node) {
        final PsiWidget widget                 = cast(node);
        final int       fontStyleForWidgetType = MMHighlighterColors.getFontStyleForWidgetType(widget.getWidgetType());
        if (fontStyleForWidgetType != -1) {
            final HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INJECTED_LANGUAGE_FRAGMENT)
                                                  .range(widget.getTextRange())
                                                  .textAttributes(new TextAttributes(null, null, null, EffectType.BOXED, fontStyleForWidgetType));
            holder.add(builder.create());
        }
    }

    private void highlightWidgetType(MetaModelAST node) {
        final PsiWidgetType  widgetType = cast(node);
        final TextAttributes attributes = EditorColorsManager.getInstance()
                                          .getGlobalScheme()
                                          .getAttributes(MMHighlighterColors.forKind(node.getType().getHighlight()))
                                          .clone();
        if (widgetType.getWidgetType() == WidgetType.INTERNAL) attributes.setFontType(2);
        final HighlightInfo.Builder builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INJECTED_LANGUAGE_FRAGMENT)
                                              .range(widgetType.getTextRange())
                                              .textAttributes(attributes);
        holder.add(builder.create());
    }

    private int getFontType(PsiElement node) {
        if (node != null) {
            for (final PsiWidget widget : PsiUtils.findParentField(node).castTo(PsiWidget.class)) {
                final WidgetType widgetType = widget.getWidgetType();
                return MMHighlighterColors.getFontStyleForWidgetType(widgetType);
            }
        }
        return -1;
    }
}  // end class MMHighlightVisitor
