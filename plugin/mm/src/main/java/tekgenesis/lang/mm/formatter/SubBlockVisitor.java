
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.formatter;

import java.util.ArrayList;
import java.util.List;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.EntityVisitor;
import tekgenesis.mmcompiler.ast.MMToken;

/**
 * SubBlock Visitor.
 */
class SubBlockVisitor extends EntityVisitor {

    //~ Instance Fields ..............................................................................................................................

    private final List<Block>       myBlocks   = new ArrayList<>();
    private final CodeStyleSettings mySettings;

    //~ Constructors .................................................................................................................................

    /** SubBlockVisitor constructor. */
    public SubBlockVisitor(CodeStyleSettings settings) {
        mySettings = settings;
    }

    //~ Methods ......................................................................................................................................

    /** visit element. */
    public void visitElement(final ASTNode node, final MMToken type) {
        // final Alignment alignment = getDefaultAlignment(type);

        ASTNode child = node.getFirstChildNode();
        while (child != null) {
            if (type != MMToken.WHITE_SPACE && child.getTextRange().getLength() > 0) {
                final MMToken childType = MMElementType.typeOf(child);
                // final Wrap      wrap           = getWrap(type, childType);
                // final Alignment childAlignment = alignmentProjection(alignment, node, child);
                final Indent childIndent = getIndent(type, childType);
                myBlocks.add(new MMBlock(child, null, childIndent, null, mySettings));
            }
            child = child.getTreeNext();
        }
    }

    /** return blocks. */
    public List<Block> getBlocks() {
        return myBlocks;
    }

    private Indent getIndent(final MMToken node, final MMToken child) {
        if (node == MMToken.FILE) return Indent.getNoneIndent();

        if (child == MMToken.PRIMARY_KEY || child == MMToken.DESCRIBED_BY || child == MMToken.IMAGE || child == MMToken.PERMISSIONS ||
            child == MMToken.INDEX) return Indent.getNoneIndent();

        if (child == MMToken.FIELD) return Indent.getNormalIndent();

        if (child == MMToken.COMMENT || child == MMToken.LINE_COMMENT) return Indent.getNoneIndent();

        return null;
    }

    //~ Methods ......................................................................................................................................

    static Alignment getDefaultAlignment(final MMToken node) {
        switch (node) {
        // case FIELD_OPTIONS:
        case IDENTIFIER:
            return Alignment.createAlignment();
        default:
            return null;
        }
    }
}  // end class SubBlockVisitor
