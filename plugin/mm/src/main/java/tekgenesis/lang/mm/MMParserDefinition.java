
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.intellij.LexerAdapter;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.mmcompiler.ast.MMToken;

/**
 * Implementation of the {@link ParserDefinition } for the EDL.
 */
@SuppressWarnings("WeakerAccess")
public class MMParserDefinition implements ParserDefinition {

    //~ Methods ......................................................................................................................................

    @NotNull public PsiElement createElement(ASTNode node) {
        throw new IllegalStateException("Incorrect node for EntityParserDefinition: " + node + " (" + node.getElementType() + ")");
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new MMFile(viewProvider);
    }
    @NotNull public Lexer createLexer(Project project) {
        return new LexerAdapter<>(MMToken.lexer(), MMElementType::forToken);
    }

    public PsiParser createParser(Project project) {
        return new MMParserAdapter();
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull public TokenSet getCommentTokens() {
        return MMElementType.COMMENTS;
    }

    public IFileElementType getFileNodeType() {
        return MMElementType.FILE;
    }

    @NotNull public TokenSet getStringLiteralElements() {
        return MMElementType.LITERALS;
    }

    @NotNull public TokenSet getWhitespaceTokens() {
        return MMElementType.WHITE_SPACES;
    }
}
