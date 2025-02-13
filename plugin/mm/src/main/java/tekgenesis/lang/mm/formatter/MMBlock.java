
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.formatter;

import java.util.List;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleSettings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.mmcompiler.ast.MMToken;

import static tekgenesis.lang.mm.MMElementType.typeOf;

/**
 * A MetaModel block.
 */
public class MMBlock implements Block {

    //~ Instance Fields ..............................................................................................................................

    private final Alignment myAlignment;
    private final Indent    myIndent;
    private final ASTNode   myNode;

    private final CodeStyleSettings mySettings;
    private List<Block>             mySubBlocks = null;
    private final Wrap              myWrap;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives a ASTNode and the CodeStyleSettings. */
    public MMBlock(ASTNode node, CodeStyleSettings settings) {
        this(node, Alignment.createAlignment(), Indent.getNoneIndent(), null, settings);
    }

    /** Constructor. Receives a ASTNode and the Alignment, Indent, Wrap and CodeStyleSettings. */
    public MMBlock(final ASTNode node, final Alignment alignment, final Indent indent, @Nullable final Wrap wrap, final CodeStyleSettings settings) {
        myAlignment = alignment;
        myIndent    = indent;
        myNode      = node;
        myWrap      = wrap;
        mySettings  = settings;
    }

    //~ Methods ......................................................................................................................................

    @Nullable public Alignment getAlignment() {
        return myAlignment;
    }

    @NotNull public ChildAttributes getChildAttributes(final int newChildIndex) {
        Indent        indent = null;
        final MMToken t      = typeOf(myNode);
        if (t == MMToken.FIELD) indent = Indent.getNormalIndent();
        else if (t == MMToken.FILE) indent = Indent.getNoneIndent();

        Alignment         alignment = null;
        final List<Block> subBlocks = getSubBlocks();
        for (int i = 0; i < newChildIndex; i++) {
            final Alignment childAlignment = subBlocks.get(i).getAlignment();
            if (childAlignment != null) {
                alignment = childAlignment;
                break;
            }
        }

        // in for loops, alignment is required only for items within parentheses
        return new ChildAttributes(indent, alignment);
    }

    public boolean isIncomplete() {
        return isIncomplete(myNode);
    }

    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }

    @Nullable public Indent getIndent() {
        return myIndent;
    }

    @Nullable public Spacing getSpacing(Block child1, @NotNull Block child2) {
        return null;
    }

    @NotNull public List<Block> getSubBlocks() {
        if (mySubBlocks == null) {
            final SubBlockVisitor visitor = new SubBlockVisitor(getSettings());
            visitor.visitElement(myNode.getPsi());
            mySubBlocks = visitor.getBlocks();
        }
        return mySubBlocks;
    }

    @NotNull public TextRange getTextRange() {
        return myNode.getTextRange();
    }

    @Nullable public Wrap getWrap() {
        return myWrap;
    }

    private boolean isIncomplete(ASTNode node) {
        ASTNode lastChild = node.getLastChildNode();
        while (lastChild != null && lastChild.getPsi() instanceof PsiWhiteSpace)
            lastChild = lastChild.getTreePrev();
        return lastChild != null && (lastChild.getPsi() instanceof PsiErrorElement || isIncomplete(lastChild));
    }

    private CodeStyleSettings getSettings() {
        return mySettings;
    }
}  // end class MMBlock
