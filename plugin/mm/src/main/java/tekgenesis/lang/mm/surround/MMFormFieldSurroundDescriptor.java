
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.surround;

import java.util.ArrayList;
import java.util.Collections;

import com.intellij.lang.surroundWith.SurroundDescriptor;
import com.intellij.lang.surroundWith.Surrounder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.psi.PsiForm;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.metadata.form.widget.WidgetType;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

/**
 * MMExpressionSurroundDescriptor.
 */
public class MMFormFieldSurroundDescriptor implements SurroundDescriptor {

    //~ Methods ......................................................................................................................................

    @Override public boolean isExclusive() {
        return true;
    }

    @NotNull @Override public PsiWidget[] getElementsToSurround(PsiFile file, int startOffset, int endOffset) {
        if (startOffset >= endOffset - 1) return EMPTY_ARRAY;

        final PsiElement firstElement = firstNonWhiteElement(file.findElementAt(startOffset), true);
        final PsiElement lastElement  = firstNonWhiteElement(file.findElementAt(endOffset), false);

        final PsiWidget firstMember = getParentOfType(firstElement, PsiWidget.class, false);
        final PsiWidget lastMember  = getParentOfType(lastElement, PsiWidget.class, false);

        if (firstMember == null || lastMember == null) return EMPTY_ARRAY;

        final PsiForm firstParent = getParentOfType(firstMember, PsiForm.class, true);
        final PsiForm lastParent  = getParentOfType(lastMember, PsiForm.class, true);

        if (firstParent != lastParent) return EMPTY_ARRAY;

        return new PsiWidget[] { firstMember, lastMember };
    }

    @NotNull @Override public Surrounder[] getSurrounders() {
        final ArrayList<Surrounder> list = new ArrayList<>();
        Collections.addAll(list, SURROUNDERS);
        return list.toArray(new Surrounder[list.size()]);
    }

    //~ Methods ......................................................................................................................................

    @Nullable protected static PsiElement firstNonWhiteElement(PsiElement element, final boolean lookRight) {
        PsiElement result = element;
        if (result instanceof PsiWhiteSpace) result = lookRight ? result.getNextSibling() : result.getPrevSibling();
        return result;
    }

    //~ Static Fields ................................................................................................................................

    private static final Surrounder[] SURROUNDERS = {
        new MMFormFieldSurrounder(WidgetType.HORIZONTAL),
        new MMFormFieldSurrounder(WidgetType.VERTICAL),
    };

    protected static final PsiWidget[] EMPTY_ARRAY = new PsiWidget[0];
}  // end class MMFormFieldSurroundDescriptor
