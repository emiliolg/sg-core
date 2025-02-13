
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.MetaModelReference;
import tekgenesis.mmcompiler.ast.MetaModelAST;

import static tekgenesis.metadata.exception.BuilderErrors.unresolvedReference;

/**
 * Wrapper to MetaModelReference when running from the compiler.
 */
public class ASTMetaModelReference extends MetaModelReference {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final transient Maker        maker;
    @NotNull private final transient MetaModelAST referenceNode;

    //~ Constructors .................................................................................................................................

    /** Create reference with given key. */
    private ASTMetaModelReference(@NotNull Maker maker, @NotNull MetaModelAST referenceNode, @NotNull QContext context, @NotNull String name) {
        super(context.extractQualification(name), context.extractName(name));
        this.maker         = maker;
        this.referenceNode = referenceNode;
    }

    //~ Methods ......................................................................................................................................

    @Override public void error() {
        maker.error(referenceNode, unresolvedReference(getFullName()));
    }

    //~ Methods ......................................................................................................................................

    /** Return new MetaModel reference to be resolved. */
    static MetaModelReference unresolvedMetaModel(@NotNull Maker m, @NotNull MetaModelAST n, @NotNull QContext context, @NotNull String name) {
        return new ASTMetaModelReference(m, n, context, name);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 2295585145359149743L;
}
