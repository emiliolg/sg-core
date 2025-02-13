
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMElementType;

import static tekgenesis.common.core.Strings.unCommentText;

/**
 * Common node for comments.
 */
public class MMComment extends MMLeafElement implements PsiComment {

    //~ Constructors .................................................................................................................................

    /** Creates a Comment node. */
    public MMComment(MMElementType type, CharSequence text) {
        super(type, text);
    }

    //~ Methods ......................................................................................................................................

    @Override public void accept(@NotNull PsiElementVisitor visitor) {
        visitor.visitComment(this);
    }

    @Override public IElementType getTokenType() {
        return getElementType();
    }

    String getUnCommentedText() {
        return unCommentText(super.getText());
    }
}
