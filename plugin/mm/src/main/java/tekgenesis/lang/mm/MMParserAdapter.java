
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import tekgenesis.intellij.ParserAdapter;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.parser.MetaModelParser;

import static tekgenesis.mmcompiler.ast.MMToken.EMPTY_TOKEN;

class MMParserAdapter extends ParserAdapter<MMToken> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final MetaModelParser parser;

    //~ Constructors .................................................................................................................................

    MMParserAdapter() {
        parser = new MetaModelParser(this, this);
    }

    //~ Methods ......................................................................................................................................

    @Override public void discard() {
        getBuilder().remapCurrentToken(MMElementType.IGNORABLE);
        getBuilder().advanceLexer();
    }

    protected MMToken adapt(IElementType t) {
        return t == null ? EMPTY_TOKEN : ((MMElementType) t).getTokenType();
    }

    @Override protected IElementType adapt(MMToken token) {
        return token.isEmpty() ? MMElementType.EMPTY : MMElementType.forToken(token);
    }

    @Override protected void parseRoot() {
        parser.parse();
    }
}
