
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.QName;
import tekgenesis.type.exception.ReverseReferenceException;
import tekgenesis.type.exception.UnresolvedTypeReferenceException;

import static tekgenesis.common.core.QName.extractQualification;
import static tekgenesis.type.MetaModelKind.UNDEFINED;
import static tekgenesis.type.Types.arrayType;

/**
 * A type that References an Entity or an Enum. The reference is not yet solved.
 */
public class UnresolvedTypeReference extends ModelType implements UnresolvedReference<Type> {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private Type finalType = null;
    private boolean        multiple  = false;

    //~ Constructors .................................................................................................................................

    /** Default constructor.* */
    UnresolvedTypeReference() {
        super("", "", "");
    }

    UnresolvedTypeReference(@NotNull String defaultDomain, @NotNull String name) {
        super("", extractQualification(name, defaultDomain), QName.extractName(name));
    }

    protected UnresolvedTypeReference(@NotNull String sourceName, @NotNull String domain, @NotNull String name, boolean multiple) {
        super(sourceName, domain, name);
        this.multiple = multiple;
    }

    //~ Methods ......................................................................................................................................

    /** Retrieves an instance of the appropriate type. */
    @NotNull @Override public Type get() {
        if (finalType == null) throw new UnresolvedTypeReferenceException(getFullName());
        return finalType;
    }

    /** Report an error when a reverse reference cannot be solved. */
    @Override public void reverseError(String attributeName, int errType) {
        throw new ReverseReferenceException(attributeName, false);
    }

    /** Solve the reference to the specified type. */
    @Nullable @Override public Type solve(Type model) {
        if (model == this) error();
        else finalType = multiple ? arrayType(model) : model;
        return finalType;
    }

    @Override public boolean isUndefined() {
        return true;
    }

    @NotNull @Override public Kind getKind() {
        return Kind.ANY;
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return UNDEFINED;
    }

    /** Reports an error when the reference cannot be solved. */
    protected void error() {
        throw new UnresolvedTypeReferenceException(getFullName());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6869937127276386750L;
}  // end class UnresolvedTypeReference
