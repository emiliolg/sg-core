
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;

/**
 * MM Commenter.
 */
@SuppressWarnings("WeakerAccess")
public class MMCommenter implements CodeDocumentationAwareCommenter {

    //~ Methods ......................................................................................................................................

    public String getBlockCommentPrefix() {
        return "/*";
    }

    public String getBlockCommentSuffix() {
        return "*/";
    }

    public IElementType getBlockCommentTokenType() {
        return MMElementType.COMMENT;
    }

    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    public String getCommentedBlockCommentSuffix() {
        return null;
    }
    public String getDocumentationCommentLinePrefix() {
        return null;
    }

    public String getDocumentationCommentPrefix() {
        return null;
    }

    public String getDocumentationCommentSuffix() {
        return null;
    }

    public IElementType getDocumentationCommentTokenType() {
        return null;
    }
    public String getLineCommentPrefix() {
        return "//";
    }

    public IElementType getLineCommentTokenType() {
        return MMElementType.LINE_COMMENT;
    }

    public boolean isDocumentationComment(PsiComment element) {
        return false;
    }
}  // end class MMCommenter
