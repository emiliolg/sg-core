
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import java.util.EnumSet;

import tekgenesis.mmcompiler.ast.MMToken;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * ExpressionAST Types for the Parser.
 */
public enum ExpressionType {

    //~ Enum constants ...............................................................................................................................

    IF {
        @Override public ExpressionType inner() { return OR; }

        @Override public boolean handles(MMToken t) { return t == MMToken.IF; }

        @Override public void parseExpression(MetaModelParser p) {
            p.beginTree();
            OR.parseExpression(p);
            if (p.getCurrent() != MMToken.IF) p.dropTree();
            else {
                p.discard();
                parseExpression(p);      // recursive invocation
                if (p.getCurrent() != MMToken.COLON) p.dropTree();
                else {
                    p.discard();
                    parseExpression(p);  // recursive invocation
                    p.endTree(MMToken.IF);
                }
            }
        }},
    OR {
        @Override public ExpressionType inner() { return AND; }

        @Override public boolean handles(MMToken t) { return t == MMToken.OR; }},
    AND {
        @Override public ExpressionType inner() { return EQUAL; }

        @Override public boolean handles(MMToken t) { return t == MMToken.AND; }},
    EQUAL {
        @Override public ExpressionType inner() { return RELATIONAL; }

        @Override public boolean handles(MMToken t) { return t == MMToken.EQ_EQ || t == MMToken.NE; }},
    RELATIONAL {
        @Override public ExpressionType inner() { return ADDITIVE; }

        @Override public boolean handles(MMToken t) { return EnumSet.of(GE, GT, LE, LT).contains(t); }},
    ADDITIVE {
        @Override public ExpressionType inner() { return MULTIPLICATIVE; }

        @Override public boolean handles(MMToken t) { return t == PLUS || t == MINUS; }},
    MULTIPLICATIVE {
        @Override public ExpressionType inner() { return UNARY; }

        @Override public boolean handles(MMToken t) { return t == ASTERISK || t == SLASH; }},
    UNARY {
        @Override public ExpressionType inner() { return ELEMENT; }

        @Override public boolean handles(MMToken t) { return t == NOT || t == PLUS || t == MINUS || t == FORBIDDEN; }

        @Override public void parseExpression(MetaModelParser p) {
            final MMToken operator = p.getCurrent();
            if (handles(operator)) {
                p.beginTree();
                p.discard();
                parseExpression(p);  // recursive invocation
                p.endTree(operator == MINUS ? UNARY_MINUS : operator);
            }
            else inner().parseExpression(p);
        }},
    ELEMENT {
        @Override public ExpressionType inner() { return null; }

        @Override public void parseExpression(MetaModelParser p) { p.parseExpressionElement(); }

        @Override public boolean handles(MMToken t) { return false; }};

    //~ Methods ......................................................................................................................................

    /** Parse a Given ExpressionAST. (Binary Operators) */
    public void parseExpression(MetaModelParser p) {
        p.beginTree();
        inner().parseExpression(p);
        MMToken operator = p.getCurrent();

        boolean match = handles(operator);
        if (!match) p.dropTree();
        else
            do {
                p.discard();
                inner().parseExpression(p);
                match = handles(p.getCurrent());
                if (match) p.endAndPushTree(operator);
                else p.endTree(operator);
                operator = p.getCurrent();
            }
            while (match);
    }

    /** Returns true if the given token is an operator for handled by this expression type. */
    protected abstract boolean handles(MMToken t);

    /** Returns the inner ExpressionAST type level for this ExpressionType. */
    protected abstract ExpressionType inner();
}
