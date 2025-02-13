
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import tekgenesis.field.TypeField;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Strings.setterName;
import static tekgenesis.field.FieldOption.PROTECTED;
import static tekgenesis.metadata.form.widget.WidgetType.SUBFORM;
import static tekgenesis.type.Types.elementType;

/**
 * Binding interface.
 */
interface Binding {

    //~ Methods ......................................................................................................................................

    /** Return binding field (throws exception if its a placeholder binding). */
    TypeField field();

    /** Return field setter. */
    default String fieldSetter() {
        return setterName(field().getName());
    }

    /** Return field getter invocation. */
    String invokeFieldGetter(WidgetContainerClassGenerator generator, String instance);

    /** Return widget getter invocation. */
    String invokeWidgetGetter(WidgetContainerClassGenerator generator, String instance);

    /** Return true if widget is of specified widget type. */
    default boolean is(WidgetType type) {
        return widget().getWidgetType() == type;
    }

    /** Return binding widget. */
    Widget widget();

    /** Return widget setter. */
    default String widgetSetter() {
        return setterName(widget().getName());
    }

    /** Return true if widget is not calculated. */
    default boolean isNotCalculated() {
        return widget().getIsExpression().isNull();
    }

    /** Return true if field is not protected. */
    default boolean isNotProtected() {
        return !isProtected();
    }

    /** Return true if field is protected. */
    default boolean isProtected() {
        return field().hasOption(PROTECTED);
    }

    /** Return true if field is multiple and of type entity. */
    default boolean isEntityMultiple() {
        return field().isMultiple() && field().isEntity();
    }

    /** Return true if field is multiple. */
    default boolean isFieldMultiple() {
        final Type type = getFinalType();
        if (field().isMultiple() || type.isArray()) {
            final Type element = notNull(elementType(type), type);
            return element.isEntity() || element.isType();
        }
        return false;
    }

    /** Return true if field is not multiple. */
    default boolean isNotFieldMultiple() {
        return !isFieldMultiple();
    }

    /** Return true if widget is not a widget definition. */
    default boolean isNotWidgetDef() {
        return !isWidgetDef();
    }

    /** Return true if widget is a widget definition. */
    default boolean isWidgetDef() {
        return is(WidgetType.WIDGET);
    }

    /** Return field name. */
    default String getFieldName() {
        return field().getName();
    }

    /** Return field final type. */
    default Type getFinalType() {
        return field().getFinalType();
    }

    /** Return field implementation class name. */
    default String getImplementationClassName() {
        return field().getImplementationClassName();
    }

    /** Return true if widget is not a subform. */
    default boolean isNotSubform() {
        return !isSubform();
    }

    /** Return true if widget is a subform. */
    default boolean isSubform() {
        return is(SUBFORM);
    }

    /** Return widget name. */
    default String getWidgetName() {
        return widget().getName();
    }

    /** Return true if field final type is array. */
    default boolean isArray() {
        return getFinalType().isArray();
    }

    /** Return true if field is not part of primary key. */
    default boolean isNotPrimaryKey() {
        return !isPrimaryKey();
    }

    /** Return true if field is part of primary key. */
    boolean isPrimaryKey();

    /** Return true if field is read only. */
    default boolean isReadOnly() {
        return field().isReadOnly();
    }
}  // end interface Binding
