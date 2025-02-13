
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.field;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.QName;
import tekgenesis.type.MetaModel;
import tekgenesis.type.UnresolvedReference;
import tekgenesis.type.exception.UnresolvedTypeReferenceException;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.QName.extractQualification;

/**
 * MetaModel reference to be resolved.
 */
public class MetaModelReference implements UnresolvedReference<MetaModel>, Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final QName key;

    @Nullable private transient MetaModel model = null;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    private MetaModelReference() {
        key = QName.EMPTY;
    }

    /** Create reference with given key. */
    protected MetaModelReference(@NotNull QName key) {
        this.key = key;
    }

    /** Create reference with given key. */
    protected MetaModelReference(@NotNull String defaultDomain, @NotNull String name) {
        key = createQName(extractQualification(name, defaultDomain), extractName(name));
    }

    //~ Methods ......................................................................................................................................

    /** Reports an error when the reference cannot be solved. */
    public void error() {
        throw new UnresolvedTypeReferenceException(getFullName());
    }

    /** Retrieves an instance of the metamodel type. */
    @NotNull @Override public MetaModel get() {
        if (model == null) throw new IllegalStateException("Reference to model '" + getFullName() + "' is unresolved or does not exist");
        return model;
    }

    @Override public void reverseError(String attributeName, int errType) {}

    @Override public MetaModel solve(MetaModel m) {
        model = m;
        return model;
    }

    @Override public String toString() {
        return getFullName();
    }

    /** Return reference full name. */
    public String getFullName() {
        return key.getFullName();
    }

    /** Return true is reference is the default one. */
    public boolean isEmpty() {
        return this == EMPTY;
    }

    /** Return true is reference is not the default one. */
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    //~ Methods ......................................................................................................................................

    /** MetaModel reference to be resolved. */
    public static MetaModelReference referenceMetaModel(@NotNull final String fqn) {
        return referenceMetaModel("", fqn);
    }

    /** MetaModel reference to be resolved. */
    public static MetaModelReference referenceMetaModel(@NotNull final String defaultDomain, @NotNull final String name) {
        return new MetaModelReference(defaultDomain, name);
    }

    //~ Static Fields ................................................................................................................................

    static final MetaModelReference EMPTY = new MetaModelReference() {
            @Override public String getFullName() {
                return "";
            }

            private static final long serialVersionUID = 5259900984430091984L;
        };

    private static final long serialVersionUID = 1826131533301041984L;
}  // end class MetaModelReference
