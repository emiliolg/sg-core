
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import java.util.ArrayList;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.common.core.Strings;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;

import static tekgenesis.codegen.CodeGeneratorConstants.GET_SINGLETON_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.core.Strings.quoted;

class FormFilterTableCodeGenerator extends ClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Constructor constructor;

    //~ Constructors .................................................................................................................................

    FormFilterTableCodeGenerator(@NotNull ClassGenerator parent, @NotNull String filterName, @NotNull MultipleWidget multiple,
                                 @NotNull String rowClassName) {
        super(parent, getFilterClass(filterName));
        asStatic();
        constructor = constructor().asPrivate();
        addAttributes(multiple);
        addFactoryField();
        addAllMethod();
        addCustomMethod(rowClassName);
    }

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("EmptyMethod")
    private void addAllMethod() {
        /* Return all out-of-the box filters for table columns. */
        /* public Seq<Filter> all() { return Colls.<Filter>list(make, model, year, engine, price, mileage, transmission); } */
        // todo implement
    }

    private void addAttributes(@NotNull final MultipleWidget multiple) {
        for (final Widget column : multiple.getTableElements()) {
            if (!column.hasGeneratedName() && column.hasValue())  // Skip nameless & valueless widgets :)
                addColumnFilterField(column);
        }
    }

    private void addColumnFilterField(@NotNull Widget columnField)
    {
        final Type type = columnField.getType().getFinalType();
        switch (type.getKind()) {
        case INT:
            addTableField(columnField, INT_FILTER, "intFilter");
            break;
        case REAL:
            addTableField(columnField, REAL_FILTER, "realFilter");
            break;
        case DECIMAL:
            addTableField(columnField, DECIMAL_FILTER, "decimalFilter");
            break;
        case STRING:
            addTableField(columnField, STRING_FILTER, "strFilter");
            break;
        case BOOLEAN:
            addTableField(columnField, BOOLEAN_FILTER, "boolFilter");
            break;
        case DATE:
            addTableField(columnField, DATE_FILTER, "dateFilter");
            break;
        case DATE_TIME:
            addTableField(columnField, DATE_TIME_FILTER, "dateTimeFilter");
            break;
        case ENUM:
            final EnumType et       = (EnumType) columnField.getType();
            final String   enumType = et.getImplementationClassName();
            addTableField(columnField,
                generic(ENUMERATION_FILTER, enumType, et.getPkType().getImplementationClassName()),
                "enumFilter",
                classOf(enumType));
            break;
        case REFERENCE:
            final String entityType = columnField.getType().getImplementationClassName();
            addTableField(columnField, generic(ENTITY_FILTER, entityType), "entityFilter", classOf(entityType));
            break;
        default:
            // Do nothing for other types! :)
        }
    }  // end method addColumnFilterField

    private void addCustomMethod(@NotNull final String rowClassName) {
        /* @NotNull public <U> Filter.CustomFilterOptions<CarsRow, U> createFilter(@NotNull final String title,
         *                                                          @NotNull final Options<? super CarsRow, U> options)
         * { return factory.customFilter(title, options); }
         */
        final Method custom = method("createFilter", generic(FILTER_OPTIONS, rowClassName, "F")).withGenerics("F").notNull();
        custom.suppressWarnings(quoted("unchecked"));
        custom.arg(TITLE, String.class).notNull().asFinal();
        final String optionsArg = OPTIONS_ARGS;
        custom.arg(optionsArg, generic(OPTIONS, wildcardSuper(rowClassName), "F")).notNull().asFinal();
        custom.return_(invoke(FACTORY, "customFilter", TITLE, optionsArg));
        custom.withComments("Return a new filter options implementation.");
    }

    private void addFactoryField() {
        /* @NotNull private final FilterFactory factory = Context.getSingleton(FilterFactory.class); */
        final String context = extractImport("tekgenesis.common.env.context.Context");
        final String factory = extractImport("tekgenesis.form.filter.FilterFactory");
        field(FACTORY, factory, invoke(context, GET_SINGLETON_METHOD, classOf(factory))).asPrivate().asFinal().notNull();
    }

    private void addTableField(Widget columnField, String fieldType, String createMethod, Object... extraArgs) {
        final String fieldName = columnField.getName();

        final Collection<String> args = new ArrayList<>();
        args.add(FIELDS_ENUM + "." + Strings.fromCamelCase(fieldName));
        for (final Object arg : extraArgs)
            args.add(String.valueOf(arg));
        args.add(quoted(columnField.getLabel()));

        constructor.assign(fieldName, invoke(FACTORY, createMethod, args));

        field(fieldName, fieldType).asPublic().asFinal().notNull();
    }

    //~ Methods ......................................................................................................................................

    static String getFilterClass(@NotNull String filterName) {
        return filterName + MMCodeGenConstants.FILTERS;
    }
}  // end class FormFilterTableCodeGenerator
