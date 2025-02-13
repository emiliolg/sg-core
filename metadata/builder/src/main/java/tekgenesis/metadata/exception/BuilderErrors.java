
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import tekgenesis.type.EnumType;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;
import tekgenesis.type.exception.ReverseReferenceException;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.type.permission.PredefinedPermission.values;

/**
 * A factory for Builder Errors.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class BuilderErrors {

    //~ Constructors .................................................................................................................................

    private BuilderErrors() {}

    //~ Methods ......................................................................................................................................

    /** Adding a Predefined permission BuilderError. */
    public static BuilderError addingPredefinedPermissionError(String permissionName) {
        return new AddingPredefinedPermissionError(permissionName);
    }

    /** Cannot resolve symbol. */
    public static BuilderError cannotResolveSymbol(String ref) {
        return new CannotResolveSymbol(ref);
    }

    /** Returns a new Default @BuilderError with the specified msg, and modelName. */
    public static BuilderError createError(String msg, String modelName) {
        return new Default(msg, modelName);
    }
    /** Illegal Argument for Widget . */
    public static IllegalArgument createIllegalArgument(String widgetName, String value, int n) {
        return new IllegalArgument(widgetName, value, n);
    }

    /** Illegal Widget. */
    public static IllegalWidgetType createIllegalWidgetTypeError(String nodeName) {
        return new IllegalWidgetType(nodeName);
    }

    /** Illegal Widget. */
    public static IllegalWidgetTypeOption createIllegalWidgetTypeOptionError(String nodeName) {
        return new IllegalWidgetTypeOption(nodeName);
    }

    /** Duplicate Unresolved reverse reference. */
    public static BuilderError duplicateReverseReference(String attributeName) {
        return createError(ReverseReferenceException.message(attributeName, true), attributeName);
    }

    /** Aggregates are empty. */
    public static BuilderError emptyAggregates(String name) {
        return createError("At least one aggregate must be defined on %s.", name);
    }

    /** Empty Domain. */
    public static BuilderError emptyDomain(String nodeName) {
        return new Default(EMPTY_DOMAIN, nodeName);
    }
    /** Table/chart is empty. */
    public static BuilderError emptyList(String name) {
        return createError("%s must have at least 1 row", name);
    }

    /** key Length is too Long. */
    public static BuilderError enumValueKeyIsTooLong(String id) {
        return new Default("Key is too long, has length of %s when max is " + EnumType.KEY_LENGTH, String.valueOf(id.length()));
    }
    /** Label Length is too Long. */
    public static BuilderError enumValueLabelIsTooLong(String label) {
        return new Default("Label is too long, has a length of %s when max is " + EnumType.LABEL_LENGTH, String.valueOf(label.length()));
    }

    /** Extra value for enum. */
    public static BuilderError extraEnumValue(String value, String modelName) {
        return new Default(String.format("Extra value  '%s' for Enum '%s'", value, modelName), modelName);
    }

    /** Illegal binding type. */
    public static BuilderError illegalBindingType(String name, MetaModel type) {
        return new IllegalBindingType(name, type);
    }

    /** Illegal Enum Value. */
    public static <T extends Enum<T>> IllegalEnumValue illegalEnumValue(Class<T> enumeration, String value) {
        return new IllegalEnumValue(enumeration.getSimpleName(), value);
    }

    /** Unresolved reference. */
    public static IllegalUniqueAttribute illegalUniqueAttribute(String nodeName) {
        return new IllegalUniqueAttribute(nodeName);
    }

    /** Image not supported for type BuilderError. */
    public static BuilderError imageNotSupported(Type widget) {
        return createError("Image not supported for '%s'", widget.toString());
    }

    /** Unresolved reference. */
    public static InvalidExpression invalidExpression(String errorDetail, String nodeName) {
        return new InvalidExpression(errorDetail, nodeName);
    }

    /** Invalid menu item form parameter. */
    public static BuilderError invalidMenuItemFormParameter(String message, String link) {
        return createError(message, link);
    }

    /** Invalid type for multiple field. */
    public static BuilderError invalidMultipleType(String modelName) {
        return createError("Invalid type for multiple field '" + modelName + "'", modelName);
    }
    /** Invalid Field Option. */
    public static BuilderError invalidOption(String nodeName) {
        return new Default(INVALID_OPTION, nodeName);
    }

    /** Invalid parameter type. */
    public static BuilderError invalidTypeForParameter(String message, String modelName) {
        return createError(message, modelName);
    }

    /** JavaReservedWordModel model. */
    public static BuilderError javaReservedWordModel(String modelName) {
        return new JavaReservedWordModel(modelName);
    }

    /** Using the listing form options with child widgets. */
    public static BuilderError listingFormWithChildWidgets(String modelName) {
        return createError("Using listing option with children", modelName);
    }

    /** Table/chart is empty. */
    public static BuilderError listNotSupported(String name) {
        return createError("List is not supported by widget %s", name);
    }

    /** LowerCase model. */
    public static BuilderError lowerCaseModel(String modelName) {
        return new LowerCaseModel(modelName);
    }

    /** Missing value for enum. */
    public static BuilderError missingEnumValue(String modelName, String fieldName) {
        return new Default(String.format("Missing value for field '%s' in Enum '%s'", fieldName, modelName), modelName);
    }

    /** Invalid parameter (not a constant). */
    public static BuilderError parameterNotAConstant(String message, String modelName) {
        return createError(message, modelName);
    }

    /** SubFormId not found. */
    public static BuilderError subFormNotDefinedError(String subFormName) {
        return new SubFormNotDefinedError(subFormName);
    }

    /** Aggregates are empty. */
    public static BuilderError undefinedAggregate(String name) {
        return createError("Aggregate function '%s' not defined.", name);
    }

    /** Undefined meta model type BuilderError. */
    public static BuilderError undefinedMetaModelType(String modelName) {
        return createError("MetaModel Type '%s' is undefined", modelName);
    }

    /** Unexpected Type. */
    public static BuilderError unexpectedType(String typeName) {
        return new Default("Unexpected type: '%s'", typeName);
    }

    /** Field reference does not exists. */
    public static UnresolvedField unresolvedField(String nodeName, String fullName) {
        return new UnresolvedField(nodeName, fullName);
    }

    /** Field type is unresolved. */
    public static BuilderError unresolvedFieldType(String nodeName) {
        return new UnresolvedFieldType(nodeName);
    }

    /** Unresolved reference. */
    public static UnresolvedReference unresolvedReference(String nodeName) {
        return new UnresolvedReference(nodeName);
    }

    /** Unresolved reverse reference. */
    public static BuilderError unresolvedReverseReference(String attributeName) {
        return createError(ReverseReferenceException.message(attributeName, false), attributeName);
    }

    /** Field reference is not searchable. */
    public static UnresolvedSearchable unresolvedSearchableField(String nodeName) {
        return new UnresolvedSearchable(nodeName);
    }

    /** Unspecified Type. */
    public static BuilderError unspecifiedType(String name) {
        return new Default("No type specified for '%s'.", name);
    }

    //~ Static Fields ................................................................................................................................

    private static final String EMPTY_DOMAIN   = "Empty domain name for : '%s'";
    private static final String INVALID_OPTION = "Invalid option: '%s'";

    //~ Inner Classes ................................................................................................................................

    public static class AddingPredefinedPermissionError implements BuilderError {
        private final String msg;
        private final String nodeName;

        private AddingPredefinedPermissionError(String permissionName) {
            msg      = format("Permission '%s' is one of the predefined permissions %s.", permissionName, mkString(asList(values())));
            nodeName = permissionName;
        }

        @Override public String getMessage() {
            return msg;
        }

        @Override public String getModelName() {
            return nodeName;
        }
    }

    public static class CannotResolveSymbol extends Default {
        private CannotResolveSymbol(String ref) {
            super("Cannot resolve symbol '%s'", ref);
        }
    }

    /**
     * A Default implementation of a Builder Error.
     */
    private static class Default implements BuilderError {
        private final String modelName;
        private final String msg;

        private Default(String msg, String modelName) {
            this.modelName = modelName;
            this.msg       = msg;
        }

        @Override public String getMessage() {
            return format(msg, modelName);
        }

        @Override public String getModelName() {
            return modelName;
        }
    }

    public static class IllegalArgument implements BuilderError {
        private final String msg;
        private final String nodeName;

        private IllegalArgument(String nodeName, String value, int n) {
            msg           = format("Illegal value '%s' for argument #%d in widget type '%s'.", value, n, nodeName);
            this.nodeName = nodeName;
        }

        @Override public String getMessage() {
            return msg;
        }

        @Override public String getModelName() {
            return nodeName;
        }
    }

    public static class IllegalBindingType extends Default {
        private IllegalBindingType(String name, MetaModel type) {
            super("Binding Form '%s' to a " + type.getName() + " is not allowed. Only entities are.", name);
        }
    }

    public static class IllegalEnumValue extends Default {
        private IllegalEnumValue(String enumeration, String value) {
            super("Cannot resolve " + enumeration + " for value '%s'.", value);
        }
    }

    public static class IllegalUniqueAttribute extends Default {
        private IllegalUniqueAttribute(String nodeName) {
            super("Attribute 'unique' is only supported for widgets inside tables, widget: '%s'.", nodeName);
        }
    }

    public static class IllegalWidgetType extends Default {
        private IllegalWidgetType(String nodeName) {
            super("Could not retrieve WidgetType for: '%s'.", nodeName);
        }
    }

    public static class IllegalWidgetTypeOption extends Default {
        private IllegalWidgetTypeOption(String nodeName) {
            super("Invalid widget option: '%s'.", nodeName);
        }
    }

    public static class InvalidExpression extends Default {
        private InvalidExpression(String errorDetail, String nodeName) {
            super(errorDetail, nodeName);
        }
    }

    public static class JavaReservedWordModel extends Default {
        private JavaReservedWordModel(String name) {
            super("Model '%s' name is a java reserved word.", name);
        }
    }

    public static class LowerCaseModel extends Default {
        private LowerCaseModel(String name) {
            super("Model '%s' name starts with lowercase character.", name);
        }
    }

    private static class SubFormNotDefinedError extends Default {
        private SubFormNotDefinedError(String subFormName) {
            super("Cannot Resolve SubFormId '%s',", subFormName);
        }
    }

    public static class UnresolvedField extends Default {
        private UnresolvedField(String nodeName, String fullName) {
            super("'%s' is not a field of '" + fullName + "'.", nodeName);
        }
    }

    public static class UnresolvedFieldType extends Default {
        private UnresolvedFieldType(String nodeName) {
            super("Field '%s' has unresolved type", nodeName);
        }
    }

    public static class UnresolvedReference extends Default {
        private UnresolvedReference(String nodeName) {
            super("Cannot resolve '%s'.", nodeName);
        }
    }

    public static class UnresolvedSearchable extends Default {
        private UnresolvedSearchable(String nodeName) {
            super("'%s' is not a searchable field.", nodeName);
        }
    }
}  // end class BuilderErrors
