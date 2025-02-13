
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.etl;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.util.Conversions;
import tekgenesis.form.FieldAccessor;
import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityTable;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.metadata.form.InstanceReference.REFERENCE_PATH_SEPARATOR;
import static tekgenesis.metadata.form.InstanceReference.createInstanceReference;
import static tekgenesis.metadata.form.widget.Widget.ElemType.ARRAY;
import static tekgenesis.metadata.form.widget.Widget.ElemType.COLUMN;
import static tekgenesis.metadata.form.widget.Widget.ElemType.SCALAR;

/**
 * Field Accessor utility class.
 */
public class FieldAccessors {

    //~ Constructors .................................................................................................................................

    private FieldAccessors() {}

    //~ Methods ......................................................................................................................................

    /** Field Accessor widget deserialization for parameters. */
    public static void deserializeWidget(@NotNull Widget widget, @NotNull FieldAccessor accessor, @NotNull Collection<String> values) {
        if (widget.getElemType() == SCALAR || widget.getElemType() == COLUMN)
        // collection will have one value or the first one will be used
        deserializeWidget(widget, accessor, values.iterator().next());

        if (widget.getElemType() == ARRAY) accessor.setField(widget, map(values, s -> getObjectValue(widget, s)));
    }

    /** Field Accessor widget deserialization. */
    public static void deserializeWidget(@NotNull Widget widget, @NotNull FieldAccessor accessor, @Nullable String value) {
        // There is not set for null expressions
        if (!widget.getIsExpression().isNull()) return;

        if (widget.getElemType() == SCALAR || widget.getElemType() == COLUMN) accessor.setField(widget, getObjectValue(widget, value));
    }

    @Nullable static EntityInstance<?, ?> findAssociatedInstance(@NotNull Widget widget, @Nullable String reference) {
        if (isEmpty(reference)) return null;
        final Type              type = widget.getType();
        final InstanceReference r    = reference.contains(REFERENCE_PATH_SEPARATOR)
                                       ? createInstanceReference(reference)
                                       : createInstanceReference(
                createQName(type.isArray() ? ((ArrayType) type).getElementType().getImplementationClassName() : type.getImplementationClassName()),
                reference);
        return EntityTable.forName(r.getFqn().getFullName()).findByString(r.getKey());
    }

    @Nullable private static Object getObjectValue(@NotNull Widget widget, @Nullable String value) {
        @Nullable final Object v;

        if (value == null) v = null;
        else {
            final Type type = widget.getType();

            if (type.isDatabaseObject()) v = findAssociatedInstance(widget, value);
            else {
                final Kind kind = type.getKind();
                switch (kind) {
                case DATE:
                    v = DateOnly.fromMilliseconds(Conversions.toLong(value));
                    break;
                case DATE_TIME:
                    v = DateTime.fromMilliseconds(Conversions.toLong(value));
                    break;
                case REAL:
                    v = Conversions.toDecimal(value);
                    break;
                case ARRAY:
                    final Type arrayType = ((ArrayType) type).getElementType();
                    if (arrayType.isDatabaseObject()) v = findAssociatedInstance(widget, value);
                    else v = arrayType.valueOf(value);
                    break;
                default:
                    v = type.valueOf(value);
                }
            }
        }
        return v;
    }
}  // end class FieldAccessors
