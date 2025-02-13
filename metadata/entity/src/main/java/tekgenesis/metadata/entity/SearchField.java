
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.type.Kind;

import static java.util.EnumSet.of;

import static tekgenesis.type.FieldReference.unresolvedFieldRef;

/**
 * Searchable field composed by entity field reference and searchable options.
 */
public class SearchField {

    //~ Instance Fields ..............................................................................................................................

    private final ModelField      field;
    @NotNull private final String fieldName;

    private final String                id;
    @NotNull private final FieldOptions options;

    //~ Constructors .................................................................................................................................

    /** Search field constructor with field reference and options for that field. */
    private SearchField(String id, @NotNull final ModelField field, @NotNull final FieldOptions options) {
        this.id      = id;
        this.field   = field;
        fieldName    = field.getName();
        this.options = options;
    }

    //~ Methods ......................................................................................................................................

    /** FieldOptions string representation. */
    public String optionsString() {
        return options.toString();
    }

    @Override public String toString() {
        return fieldName + ":" + field.getFinalType().getKind();
    }

    /** Check that type is valid to search in db. */
    public boolean isValidTypeForDb() {
        return validDatabaseTypes.contains(getKind());
    }

    /** Return 'boost' option value for field. */
    public int getBoost() {
        return options.getInt(FieldOption.BOOST, 1);
    }

    /** Return if it has 'analyzed' option. */
    public boolean isAnalyzed() {
        return options.hasOption(FieldOption.ANALYZED);
    }

    /** Return if entity field is an array type or a multiple reference. */
    public boolean isMultiple() {
        return field.getType().isArray() || (field instanceof TypeField && ((TypeField) field).isMultiple());
    }

    /** Return entity field reference. */
    @NotNull public ModelField getField() {
        return field;
    }

    /** Return entity field reference name. */
    @NotNull public String getFieldName() {
        return fieldName;
    }

    /** Return 'fuzzy' option value for field. */
    public int getFuzzy() {
        return options.getInt(FieldOption.FUZZY);
    }

    /** Get field id. */
    public String getId() {
        return id;
    }

    /** Get search field kind. */
    public Kind getKind() {
        return field.getType().getKind();
    }

    /** Return options for search field. */
    @NotNull public FieldOptions getOptions() {
        return options;
    }

    /** Return if it has 'search_filter' option. */
    public boolean isSearchFilter() {
        return options.hasOption(FieldOption.FILTER_ONLY);
    }

    /** Return 'slop' option value for field. */
    public int getSlop() {
        return options.getInt(FieldOption.SLOP);
    }

    /** Return if it has 'prefix' option. */
    public boolean isPrefix() {
        return options.hasOption(FieldOption.PREFIX);
    }

    //~ Methods ......................................................................................................................................

    /** Static constructor. */
    @NotNull static SearchField searchField(String id, @NotNull final ModelField f, @NotNull final FieldOptions o) {
        return new SearchField(id, f, o);
    }

    /** Static constructor for unresolved reference of model field. */
    @NotNull static SearchField unresolvedSearchField(@NotNull final ModelField f) {
        return searchField(f.getName(), unresolvedFieldRef(f.getName()), FieldOptions.EMPTY);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumSet<Kind> validDatabaseTypes = of(Kind.STRING, Kind.ENUM, Kind.DECIMAL, Kind.REAL, Kind.INT);
}  // end class SearchField
