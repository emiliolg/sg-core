
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.assignment;

import java.io.Serializable;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.collections.Colls.toList;
import static tekgenesis.common.collections.ImmutableList.empty;
import static tekgenesis.common.collections.ImmutableList.fromIterable;
import static tekgenesis.common.core.Strings.toStringIterable;

/**
 * Assignment inside field option values.
 */
public class AssignmentType implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String       field;
    private boolean      isEquals;
    private List<String> value;
    private boolean      when;

    //~ Constructors .................................................................................................................................

    /** Empty constructor. */
    AssignmentType() {
        this("", empty(), true, false);
    }

    /** Assignment type. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    private AssignmentType(String field, Iterable<Object> value, boolean isEquals, boolean when) {
        this.field    = field;
        this.value    = fromIterable(toStringIterable(value));
        this.isEquals = isEquals;
        this.when     = when;
    }

    //~ Methods ......................................................................................................................................

    /** Assignment can be added as part of a query or not. */
    public boolean canAddAssignment() {
        return isWhen() && hasNonEmptyValue();
    }

    /** Equals setter. */
    public void setEquals(boolean equals) {
        isEquals = equals;
    }

    /** Field getter. */
    public String getField() {
        return field;
    }

    /** Field setter. */
    public void setField(String field) {
        this.field = field;
    }

    /** Value list filtered to get non-empty strings only. */
    public List<String> getFilteredValue() {
        return filter(value, v -> !isEmpty(v)).toList();
    }

    /** When getter. */
    public boolean isWhen() {
        return when;
    }

    /** Equals getter. */
    public boolean isEquals() {
        return isEquals;
    }

    /** Value getter. */
    public List<String> getValue() {
        return value;
    }

    /** Value setter. */
    public void setValue(List<String> value) {
        this.value = value;
    }

    /** When setter. */
    public void setWhen(boolean when) {
        this.when = when;
    }

    private boolean hasNonEmptyValue() {
        return toList(value).getFirst(v -> !isEmpty(v)).isPresent();
    }

    //~ Methods ......................................................................................................................................

    /** Create metadata assignment. */
    @NotNull public static AssignmentType assignment(String f, Iterable<Object> v, boolean eq, boolean w) {
        return new AssignmentType(f, v, eq, w);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8948610255528681509L;
}  // end class AssignmentType
