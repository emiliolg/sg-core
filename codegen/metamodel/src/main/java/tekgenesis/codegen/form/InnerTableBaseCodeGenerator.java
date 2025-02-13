
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.transaction.Transaction;
import tekgenesis.type.EntityReference;
import tekgenesis.type.ModelType;

import static java.lang.String.format;

import static tekgenesis.codegen.CodeGeneratorConstants.CAT;
import static tekgenesis.codegen.CodeGeneratorConstants.NOT_EQ_NULL;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.form.DbObjectMethodsCodeGenerator.ACTION_DEFAULT;
import static tekgenesis.codegen.form.UiModelBaseCodeGenerator.createUiModelAccessorField;
import static tekgenesis.common.core.Strings.getterName;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.util.JavaReservedWords.THIS;

/**
 * Generates a Table Inner class.
 */
class InnerTableBaseCodeGenerator extends WidgetContainerClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final MultipleWidget              multipleWidget;
    private final UiModelBaseCodeGenerator<?> parent;
    private final String                      tableName;

    //~ Constructors .................................................................................................................................

    InnerTableBaseCodeGenerator(UiModelBaseCodeGenerator<?> parent, String tableClassName, MultipleWidget multipleWidget,
                                Option<? extends ModelType> binding) {
        super(parent, tableClassName + BASE, multipleWidget.getTableElements(), binding);
        withInterfaces(generic(FORM_ROW_INSTANCE, tableClassName));
        this.parent         = parent;
        this.multipleWidget = multipleWidget;
        tableName           = tableClassName;
    }

    //~ Methods ......................................................................................................................................

    public void generation() {
        createUiModelAccessorField(this);

        traverse(multipleWidget);

        generateMessageMethod();
        generateIsDefinedMethod();
        generateResetMethod();
        generateFocusMethod();
        generateTableMethod();
        generateRemoveMethod();
        generateConfigureMethod();

        generatePopulateMethod(true);
        generateCopyToMethod();

        generateCreateOrUpdateSubforms();

        if (getBindings().isPrimaryKeyBound()) generateKeyAsString();
    }

    void addOptionSetter(ElementAccessorCodeGenerator g) {
        parent.addOptionSetter(g);
        g.addOptions(this);
    }

    @Override void delegateRemoveRowExecution(Method m, String table) {
        // Delegate execution to 'remove' method
        m.return_(invoke(REMOVE_METHOD));
    }

    @Override void generateExportOnClick(Widget button, String methodName) {
        parent.generateExportOnClick(button, methodName);
    }

    String getTableClassName() {
        return tableName;
    }

    @Override String getThisClass() {
        return tableName;
    }

    private void generateKeyAsString() {
        getBinding().castTo(DbObject.class).ifPresent(binding -> {
            final String keys   = quoted("") + CAT + createKeyArguments(getBindings(), "", CAT + quoted(":") + CAT);
            final Method method = method(KEY_AS_STRING_METHOD, String.class).asPublic();
            method.withComments("Return primary key of bound {@link " + extractImport(binding.getImplementationClassName()) + "}");
            method.notNull().return_(keys);
        });
    }

    private void generateRemoveMethod() {
        if (!hasMethod(REMOVE_METHOD)) {
            final StringBuilder message = new StringBuilder("Remove row from table");
            final Method        m       = method(REMOVE_METHOD, extractImport(FORM_ACTION)).notNull();

            if (isEntity() && isPrimaryKeyBound()) {
                m.startLambda(invokeStatic(Transaction.class, "runInTransaction"));
                final EntityReference reference = (EntityReference) multipleWidget.getType();
                final String          type      = extractImport(reference.getFullName());
                message.append(format(" and delete associated %s instance", reference.getName()));

                final String keyFields   = createKeyFields();
                final String keyAsString = invoke(KEY_AS_STRING_METHOD);
                m.declare(type, INSTANCE, invoke("", IS_DEFINED, keyFields) + " ? " + invoke(type, FIND_METHOD, keyAsString) + " : null");
                m.singleStatementIf(INSTANCE + NOT_EQ_NULL, invoke(INSTANCE, DELETE_METHOD));
                m.endLambda();
            }

            m.withComments(message + ".");
            m.statement(invoke(invoke(TABLE), REMOVE_METHOD, cast(tableName, THIS)));
            m.return_(ACTION_DEFAULT);
        }
    }

    private void generateTableMethod() {
        final Method m = method(TABLE, generic(FORM_TABLE, tableName)).asFinal().notNull();
        m.withComments("Return associated table.");
        m.return_(invoke(getterName(multipleWidget.getName(), "")));
    }
}  // end class InnerTableBaseCodeGenerator
