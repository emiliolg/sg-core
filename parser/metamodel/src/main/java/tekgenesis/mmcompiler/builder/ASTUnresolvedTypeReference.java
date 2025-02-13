
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

import tekgenesis.metadata.exception.BuilderErrors;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.UnresolvedTypeReference;

import static tekgenesis.metadata.exception.BuilderErrors.duplicateReverseReference;
import static tekgenesis.metadata.exception.BuilderErrors.unresolvedReference;
import static tekgenesis.metadata.exception.BuilderErrors.unresolvedReverseReference;

/**
 * Wrapper to UnresolvedReference when running from the compiler.
 */
class ASTUnresolvedTypeReference extends UnresolvedTypeReference {

    //~ Instance Fields ..............................................................................................................................

    private final Maker        maker;
    private final MetaModelAST typeNode;

    //~ Constructors .................................................................................................................................

    ASTUnresolvedTypeReference(Maker maker, @NotNull String sourceName, @NotNull QContext context, @NotNull String name, MetaModelAST typeNode) {
        this(maker, sourceName, context, name, typeNode, false);
    }

    ASTUnresolvedTypeReference(Maker maker, @NotNull String sourceName, @NotNull QContext context, @NotNull String name, MetaModelAST typeNode,
                               boolean multiple) {
        super(sourceName, context.extractQualification(name), context.extractName(name), multiple);
        this.maker = maker;
        if (getDomain().isEmpty()) maker.error(typeNode, BuilderErrors.emptyDomain(name));
        this.typeNode = typeNode;
    }

    //~ Methods ......................................................................................................................................

    public void error() {
        maker.error(typeNode, unresolvedReference(getFullName()));
    }

    @Override public void reverseError(String attributeName, int errType) {
        maker.error(typeNode, errType == NOT_FOUND ? unresolvedReverseReference(attributeName) : duplicateReverseReference(attributeName));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3795938061658954363L;
}
