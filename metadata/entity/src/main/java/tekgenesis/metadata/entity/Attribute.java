
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.TypeField;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;

import static tekgenesis.common.core.Constants.MAX_DB_ID_LENGTH;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.truncate;
import static tekgenesis.field.FieldOption.START_WITH;

/**
 * This class represents an @Entity attribute. It has a name, description, type
 */
public class Attribute extends TypeField {

    //~ Instance Fields ..............................................................................................................................

    boolean inner;
    boolean multiple;

    @NotNull private final DbObject dbObject;
    @NotNull private final String   label;

    @NotNull private String reverseReference;
    private final String    serialSequenceName;
    private final boolean   synthesized;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("ConstructorWithTooManyParameters")
    // It is used from a builder
    Attribute(@NotNull DbObject dbObject, @NotNull String name, @NotNull Type type, @NotNull String label, FieldOptions options, boolean inner,
              boolean multiple, @NotNull String reverseReference, boolean synthesized, String seq) {
        super(name, type, options);
        this.dbObject         = dbObject;
        this.multiple         = multiple;
        serialSequenceName    = seq != null && seq.isEmpty() ? truncate(dbObject.getTableName().getName(), "SEQ", "_", MAX_DB_ID_LENGTH)
                                                               .toUpperCase()
                                                             : seq;
        this.reverseReference = reverseReference;
        this.label            = label;
        this.inner            = inner;
        this.synthesized      = synthesized;
    }

    //~ Methods ......................................................................................................................................

    /**
     * If the type of the attribute is a DatabaseObject return an Option to the DatabaseObject if
     * not return {@link Option#empty()} }.
     */
    public Option<DbObject> asDatabaseObject() {
        final Type t = getFinalType();
        return t instanceof DbObject ? some((DbObject) t) : Option.empty();
    }

    /**
     * If the type of the attribute is an Entity return an Option to the Entity if not return
     * {@link Option#empty()}}.
     */
    public Option<Entity> asEntity() {
        final Type t = getFinalType();
        return t instanceof Entity ? some((Entity) t) : Option.empty();
    }

    /**
     * If the type of the attribute is an Enum return an Option to the Enum if not return
     * {@link Option#empty()}}.
     */
    public Option<EnumType> asEnum() {
        final Type t = getFinalType();
        return t instanceof EnumType ? some((EnumType) t) : Option.empty();
    }

    @Override public boolean equals(Object obj) {
        return obj instanceof Attribute && getFullName().equals(((Attribute) obj).getFullName());
    }

    /** Returns <code>true</code> if the attribute has a column in the database. */
    public boolean hasColumn() {
        return !isMultipleReference() && !getOptions().hasOption(FieldOption.ABSTRACT);
    }

    @Override public int hashCode() {
        return getFullName().hashCode();
    }

    /** Returns the attribute type. Do not validate */
    public boolean hasUndefinedType() {
        return getFinalType().isUndefined();
    }

    @NotNull public ImmutableList<TypeField> retrieveSimpleFields() {
        if (isMultipleReference()) return Colls.emptyList();
        return super.retrieveSimpleFields();
    }

    /** Indicates whether the attribute is synthesized or not. */
    @Override public boolean isSynthesized() {
        return synthesized;
    }

    /** Returns the Attribute entity. */
    @NotNull public DbObject getDbObject() {
        return dbObject;
    }

    @Override public boolean isMultiple() {
        return multiple;
    }

    /** Returns the name of the Element type when an attribute is multiple. */
    public String getElementClassName() {
        return isMultiple() ? super.getImplementationClassName() : "";
    }

    /** Returns the attribute full name. */
    public String getFullName() {
        return dbObject.getFullName() + "." + getName();
    }

    /** returns the Class that implements the field type. */
    @NotNull public String getImplementationClassName() {
        if (isMultipleReference()) {
            final Type type = getFinalType();
            return Seq.class.getName() + "<" + type.getImplementationClassName() + ">";
        }
        return super.getImplementationClassName();
    }

    /**
     * Returns <code>true</code> if the attribute is generated by a sequence (default primary key).
     */
    public boolean isSerial() {
        return serialSequenceName != null;
    }

    /** Returns the Field label. */
    @NotNull public String getLabel() {
        return label;
    }

    /** Returns true if the Attribute references an Inner Entity. */
    public boolean isInner() {
        return inner;
    }

    /** Return the attribute id of the Reverse reference. */
    @NotNull public String getReverseReference() {
        return reverseReference;
    }

    /** Sets the reverse reference. */
    public void setReverseReference(@NotNull String reverseReference) {
        this.reverseReference = reverseReference;
    }

    /** If the attribute is a serial return the SequenceName. */
    @NotNull public String getSequenceName() {
        return serialSequenceName;
    }

    /** If the attribute is a serial return the Sequence Start Value. */
    public int getSequenceStart() {
        return getOptions().getInt(START_WITH, 1);
    }

    private boolean isMultipleReference() {
        return isMultiple() && getFinalType() instanceof DbObject;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4966202528356787587L;
}  // end class Attribute
