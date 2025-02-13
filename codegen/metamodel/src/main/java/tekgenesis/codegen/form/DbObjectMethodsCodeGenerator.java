
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.CodeGeneratorConstants;
import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.codegen.impl.java.JavaElement;
import tekgenesis.common.core.Constants;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.form.FormBaseCodeGenerator.isMutableReference;
import static tekgenesis.common.core.Constants.BOOLEAN;
import static tekgenesis.common.core.Constants.DEPRECATED;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.metadata.form.model.FormConstants.CREATE_OR_UPDATE_METHOD_NAME;
import static tekgenesis.metadata.form.model.FormConstants.FIND_METHOD_NAME;

class DbObjectMethodsCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final WidgetContainerClassGenerator baseClass;
    @NotNull private final DbObject                      binding;
    @NotNull private final Bindings                      bindings;

    //~ Constructors .................................................................................................................................

    DbObjectMethodsCodeGenerator(@NotNull final WidgetContainerClassGenerator baseClass, @NotNull final DbObject binding,
                                 @NotNull final Bindings bindings) {
        this.baseClass = baseClass;
        this.binding   = binding;
        this.bindings  = bindings;
    }

    //~ Methods ......................................................................................................................................

    void generate() {
        // Add create method
        generateCreateMethod();

        // Add update method
        generateUpdateMethod();

        // Add delete method
        generateDeleteMethod();

        // Add deprecate method
        generateDeprecateMethod();

        // Add find method
        generateFindMethod();
    }

    private JavaElement.Method actionMethod(String name, String comment) {
        return baseClass.method(name, FORM_ACTION).withComments(comment).notNull();
    }

    /** Set method as abstract and add it to the list. */
    private void asAbstract(JavaElement.Method method) {
        baseClass.addAbstractMethod(method.asAbstract());
    }

    /** Generate create method. */
    private void generateCreateMethod() {
        final JavaElement.Method method = actionMethod(CREATE_METHOD, CREATE_FORM_INSTANCE);

        if (binding.isEntity()) {
            if (binding.hasDefaultPrimaryKey() || bindings.isPrimaryKeyBound()) {
                final String args       = baseClass.createKeyArguments(bindings, "", ", ");
                final String entityName = binding.getName();
                final String instance   = deCapitalizeFirst(entityName);
                final String invoke     = binding.hasDefaultPrimaryKey() ? invoke(entityName, CREATE_METHOD)
                                                                         : invoke(entityName, CREATE_METHOD, args);
                method.declare(entityName, instance, invoke);
                method.statement(invoke("", COPY_TO_METHOD_NAME, instance));
                invokeCreateOrUpdateSubForms(method, instance, false);
                invokeCreateOrUpdateForTables(method, instance, this::multipleInnerWidgets);
                method.statement(invoke(instance, INSERT_METHOD));
                if (binding.hasDefaultPrimaryKey()) method.statement("setId(" + instance + ".getId())");

                invokeCreateOrUpdateForTables(method, instance, this::multipleNotInnerWidgets);
            }
            else asAbstract(method);
        }
        else {
            method.throwNew(UnsupportedOperationException.class);
            return;
        }

        method.return_(ACTION_DEFAULT);
    }

    private void generateDeleteMethod() {
        final JavaElement.Method method = actionMethod(DELETE_METHOD, "Invoked when deleting a form instance");

        if (binding.isUpdatable()) method.statement(invoke(invoke("", FIND_METHOD_NAME), DELETE_METHOD));
        else {
            method.throwNew(UnsupportedOperationException.class);
            return;
        }
        method.return_(ACTION_DEFAULT);
    }  // end method generateDeleteMethod

    private void generateDeprecateMethod() {
        if (binding.isDeprecable()) {
            final JavaElement.Method method = actionMethod(DEPRECATE_METHOD, "Invoked to change the deprecation status of a form instance");
            method.arg(STATUS_ARG, BOOLEAN);
            method.declare(binding.getImplementationClassName(), DEPRECABLE_VAR, invoke("", FIND_METHOD_NAME));
            method.startIf(invoke(DEPRECABLE_VAR, getterName(DEPRECATED, BOOLEAN)) + NOT_EQ + STATUS_ARG);
            method.statement(invoke(DEPRECABLE_VAR, DEPRECATE_METHOD, STATUS_ARG));
            method.statement(invoke(DEPRECABLE_VAR, Constants.UPDATE, ""));
            method.endIf();
            method.return_(ACTION_DEFAULT);
        }
    }

    /** Generate find method, as abstract if primary key is not bound. */
    private void generateFindMethod() {
        final String             name   = binding.getName();
        final JavaElement.Method method = baseClass.method(FIND_METHOD_NAME, binding.getImplementationClassName()).notNull();
        method.withComments("Invoked to find an entity instance");
        if (bindings.isPrimaryKeyBound()) {
            method.declare(name, CodeGeneratorConstants.VALUE, baseClass.invokeStatic(name, FIND_METHOD_NAME, invoke("", KEY_AS_STRING_METHOD)));
            method.startIf(CodeGeneratorConstants.VALUE + EQ_NULL);
            method.throwNew("tekgenesis.form.exception.EntityInstanceNotFoundException", invoke(F_VAR, TITLE), invoke("", KEY_AS_STRING_METHOD));
            method.endIf();
            method.return_(CodeGeneratorConstants.VALUE);
        }
        else asAbstract(method);
    }

    private void generateUpdateMethod() {
        final JavaElement.Method method = actionMethod(MMCodeGenConstants.UPDATE_METHOD, "Invoked when updating a form instance");
        if (binding.isUpdatable()) {
            final String instance = deCapitalizeFirst(binding.getName());
            method.declare(binding.getName(), instance, FIND_METHOD_NAME + "()");
            method.statement(COPY_TO_METHOD_NAME + "(" + instance + ")");
            invokeCreateOrUpdateSubForms(method, instance, true);
            invokeCreateOrUpdateForTables(method, instance, this::multipleInnerWidgets);
            method.statement(instance + "." + MMCodeGenConstants.UPDATE_METHOD + "()");
            invokeCreateOrUpdateForTables(method, instance, this::multipleNotInnerWidgets);
        }
        else {
            method.throwNew(UnsupportedOperationException.class);
            return;
        }
        method.return_(ACTION_DEFAULT);
    }

    private String invoke(String target, String method, String... args) {
        return baseClass.invoke(target, method, args);
    }

    private void invokeCreateOrUpdateForTables(JavaElement.Method method, String instance, Predicate<Binding> multiples) {
        bindings.filter(multiples).forEach(multiple -> {
            final DbObject e = (DbObject) multiple.getFinalType();
            if (e.isInner() || isMutableReference(e, ((Attribute) multiple.field()).getReverseReference()))
                method.statement(baseClass.invoke("", CREATE_OR_UPDATE_METHOD_NAME + capitalizeFirst(multiple.getWidgetName()), instance));
            else method.statement(baseClass.invoke("", CREATE_OR_UPDATE_METHOD_NAME + capitalizeFirst(multiple.getWidgetName())));
        });
    }

    private void invokeCreateOrUpdateSubForms(JavaElement.Method method, String instance, boolean update) {
        bindings.filter(Binding::isSubform).forEach(b ->
                method.statement(
                    baseClass.invoke("", CREATE_OR_UPDATE_METHOD_NAME + capitalizeFirst(b.getWidgetName()), instance, String.valueOf(update))));
    }

    private boolean multipleInnerWidgets(@NotNull final Binding b) {
        final Attribute a = (Attribute) b.field();
        return a.isMultiple() && a.isInner() && a.isEntity();
    }

    private boolean multipleNotInnerWidgets(@NotNull final Binding b) {
        final Attribute a = (Attribute) b.field();
        return a.isMultiple() && !a.isInner() && a.isEntity();
    }

    //~ Static Fields ................................................................................................................................

    private static final String CREATE_FORM_INSTANCE = "Invoked when creating a form instance";

    static final String ACTION_DEFAULT = "actions().getDefault()";
}  // end class DbObjectMethodsCodeGenerator
