
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

import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.*;

import static tekgenesis.metadata.exception.BuilderErrors.duplicateReverseReference;
import static tekgenesis.metadata.exception.BuilderErrors.unresolvedReference;

/**
 * This class represents a ModelField reference.
 */
public class ASTFieldReference extends FieldReference {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Maker        maker;
    @NotNull private final MetaModelAST typeNode;

    //~ Constructors .................................................................................................................................

    /**  */
    public ASTFieldReference(@NotNull String name, @NotNull Maker maker, @NotNull MetaModelAST typeNode) {
        super(name);
        this.maker    = maker;
        this.typeNode = typeNode;
    }

    //~ Methods ......................................................................................................................................

    @Override public void error() {
        maker.error(typeNode, unresolvedReference(getName()));
    }

    @Override public void reverseError(String attributeName, int errType) {
        maker.error(typeNode, errType == NOT_FOUND ? unresolvedReference(attributeName) : duplicateReverseReference(attributeName));
    }
}
