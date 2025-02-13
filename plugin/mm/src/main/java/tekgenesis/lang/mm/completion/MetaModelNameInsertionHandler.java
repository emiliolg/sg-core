
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import com.intellij.codeInsight.CodeInsightUtilCore;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.diagnostic.AttachmentFactory;
import com.intellij.diagnostic.LogMessageEx;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.ReferenceAdjuster;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.impl.source.PostprocessReformattingAspect;
import com.intellij.psi.impl.source.SourceTreeToPsiMap;
import com.intellij.util.IncorrectOperationException;
import com.jgoodies.common.base.Strings;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.logging.Logger;
import tekgenesis.lang.mm.MMLanguage;
import tekgenesis.lang.mm.psi.*;

import static tekgenesis.lang.mm.style.MetaModelReferenceAdjuster.resolve;

/**
 * MetaModel insertion handler for reference adjusting.
 */
public class MetaModelNameInsertionHandler implements InsertHandler<LookupElement> {

    //~ Methods ......................................................................................................................................

    @Override public void handleInsert(InsertionContext context, LookupElement item) {
        final PsiFile                          file     = context.getFile();
        final int                              offset   = context.getTailOffset() - 1;
        final PsiElement                       position = file.findElementAt(offset);
        final PsiMetaModelCodeReferenceElement ref      = position != null && position.getParent() instanceof PsiMetaModelCodeReferenceElement
                                                          ? (PsiMetaModelCodeReferenceElement) position.getParent()
                                                          : null;

        final PsiMetaModel<?> model = (PsiMetaModel<?>) item.getObject();

        if (ref == null || !ref.isQualified()) addImportForMetaModel(context, model);
        System.out.println("MetaModelNameInsertionHandler.handleInsert");
    }

    //~ Methods ......................................................................................................................................

    private static void addImportForMetaModel(InsertionContext context, PsiMetaModel<?> model) {
        final PsiFile file = context.getFile();

        final int startOffset = context.getStartOffset();
        final int beforeTail  = context.getTailOffset();
        final int afterTail   = insertMetaModelReference(model, file, startOffset, beforeTail);
        if (afterTail > context.getDocument().getTextLength() || afterTail < 0) {
            logger.error(
                LogMessageEx.createEvent("Invalid offset after insertion ",
                                "offset=" + afterTail + "\n" +
                                "start=" + startOffset + "\n" +
                                "tail=" + beforeTail + "\n" +
                                "file.length=" + file.getTextLength() + "\n" +
                                "document=" + context.getDocument() + "\n" + DebugUtil.currentStackTrace(),
                                AttachmentFactory.createAttachment(context.getDocument()))
                            .getMessage());
            return;
        }
        context.setTailOffset(afterTail);
        shortenReference(file, context.getStartOffset());
        PostprocessReformattingAspect.getInstance(context.getProject()).doPostponedFormatting();
    }

    /** Insert meta model reference. */
    @SuppressWarnings("OverlyNestedMethod")
    private static int insertMetaModelReference(@NotNull PsiMetaModel<?> model, @NotNull PsiFile file, int startOffset, int endOffset) {
        final Project            project  = file.getProject();
        final PsiDocumentManager instance = PsiDocumentManager.getInstance(project);
        instance.commitAllDocuments();

        final PsiManager manager = file.getManager();

        final Document document = FileDocumentManager.getInstance().getDocument(file.getViewProvider().getVirtualFile());
        if (document == null) throw new IllegalStateException();

        final PsiReference reference = file.findReferenceAt(startOffset);
        if (reference != null) {
            final PsiElement resolved = reference.resolve();
            if (resolved instanceof PsiMetaModel) {
                if (manager.areElementsEquivalent(model, resolved)) return endOffset;
            }
        }

        final String name = model.getName();
        if (Strings.isEmpty(name)) return endOffset;

        document.replaceString(startOffset, endOffset, name);

        int               newEndOffset = startOffset + name.length();
        final RangeMarker toDelete     = insertTemporary(newEndOffset, document, " ");

        instance.commitAllDocuments();

        final PsiElement element = file.findElementAt(startOffset);
        if (element instanceof MMIdentifier) {
            final PsiElement parent = element.getParent();
            if (parent instanceof PsiMetaModelCodeReferenceElement && !((PsiMetaModelCodeReferenceElement) parent).isQualified() &&
                !(parent.getParent() instanceof PsiDomain))
            {
                final PsiMetaModelCodeReferenceElement ref = (PsiMetaModelCodeReferenceElement) parent;

                final MetaModelReference r = (MetaModelReference) ref.getReference();
                if (model.isValid() && r != null && !model.getManager().areElementsEquivalent(model, r.resolve())) {
                    final PsiMetaModelCodeReferenceElement bind = r.bindToElement(model);

                    final RangeMarker rangeMarker = document.createRangeMarker(bind.getTextRange());
                    instance.doPostponedOperationsAndUnblockDocument(document);
                    instance.commitDocument(document);

                    final PsiMetaModelCodeReferenceElement e = CodeInsightUtilCore.findElementInRange(file,
                            rangeMarker.getStartOffset(),
                            rangeMarker.getEndOffset(),
                            PsiMetaModelCodeReferenceElement.class,
                            MMLanguage.INSTANCE);

                    rangeMarker.dispose();

                    if (e != null) {
                        newEndOffset = e.getTextRange().getEndOffset();
                        if (!model.getManager().areElementsEquivalent(model, resolve(e))) {
                            final String qName = model.getFullName();
                            if (qName != null) {
                                document.replaceString(e.getTextRange().getStartOffset(), newEndOffset, qName);
                                newEndOffset = e.getTextRange().getStartOffset() + qName.length();
                            }
                        }
                    }
                }
            }
        }

        if (toDelete.isValid()) document.deleteString(toDelete.getStartOffset(), toDelete.getEndOffset());

        return newEndOffset;
    }  // end method insertMetaModelReference

    private static RangeMarker insertTemporary(final int endOffset, final Document document, final String temporary) {
        final CharSequence chars    = document.getCharsSequence();
        final int          length   = chars.length();
        final RangeMarker  toDelete;
        if (endOffset < length && Character.isJavaIdentifierPart(chars.charAt(endOffset))) {
            document.insertString(endOffset, temporary);
            toDelete = document.createRangeMarker(endOffset, endOffset + 1);
        }
        else if (endOffset >= length) toDelete = document.createRangeMarker(length, length);
        else toDelete = document.createRangeMarker(endOffset, endOffset);
        toDelete.setGreedyToLeft(true);
        toDelete.setGreedyToRight(true);
        return toDelete;
    }

    private static PsiElement shortenReference(final PsiFile file, final int offset)
        throws IncorrectOperationException
    {
        final Project            project  = file.getProject();
        final PsiDocumentManager manager  = PsiDocumentManager.getInstance(project);
        final Document           document = manager.getDocument(file);
        if (document != null) {
            manager.commitDocument(document);
            final PsiReference ref = file.findReferenceAt(offset);
            if (ref != null) {
                final PsiElement element = ref.getElement();
                if (element != null) {
                    final ReferenceAdjuster adjuster = ReferenceAdjuster.Extension.getReferenceAdjuster(element.getLanguage());
                    if (adjuster != null) {
                        final ASTNode reference = adjuster.process(element.getNode(), true, false, project);
                        return SourceTreeToPsiMap.treeToPsiNotNull(reference);
                    }
                    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
                }
                return element;
            }
        }
        return null;
    }

    //~ Static Fields ................................................................................................................................

    public static final Logger                logger                   = Logger.getLogger(MetaModelNameInsertionHandler.class);
    static final InsertHandler<LookupElement> METAMODEL_INSERT_HANDLER = new MetaModelNameInsertionHandler();
}  // end class MetaModelNameInsertionHandler
