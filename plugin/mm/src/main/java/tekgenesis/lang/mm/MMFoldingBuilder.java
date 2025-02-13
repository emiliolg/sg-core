
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.util.ArrayList;
import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.lang.mm.MMElementType.COMMENT;
import static tekgenesis.lang.mm.MMElementType.LIST;

/**
 * Folding manager for MM.
 */
@SuppressWarnings("WeakerAccess")
public class MMFoldingBuilder implements FoldingBuilder {

    //~ Methods ......................................................................................................................................

    @NotNull public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode astNode, @NotNull Document document) {
        final List<FoldingDescriptor> descriptors = new ArrayList<>();
        appendDescriptors(astNode, descriptors);
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    public String getPlaceholderText(@NotNull ASTNode curNode) {
        final IElementType c = curNode.getElementType();

        return LIST.equals(c) ? "{...}" : COMMENT.equals(c) ? "/*...*/" : null;
    }

    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return COMMENT.equals(astNode.getElementType());
    }

    private void appendDescriptors(ASTNode curNode, List<FoldingDescriptor> descriptors) {
        final IElementType c = curNode.getElementType();
        if ((c.equals(LIST) || c.equals(COMMENT)) && isMultipleLine(curNode)) descriptors.add(new FoldingDescriptor(curNode, curNode.getTextRange()));

        ASTNode child = curNode.getFirstChildNode();
        while (child != null) {
            appendDescriptors(child, descriptors);
            child = child.getTreeNext();
        }
    }

    private boolean isMultipleLine(ASTNode node) {
        final String text = node.getText();
        return text.contains("\n") || text.contains("\r") || text.contains("\r\n");
    }
}
