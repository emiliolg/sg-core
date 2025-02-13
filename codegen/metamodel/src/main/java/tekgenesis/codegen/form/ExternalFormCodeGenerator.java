
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
import java.util.List;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Strings;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.field.Signed;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Type;

import static tekgenesis.codegen.CodeGeneratorConstants.*;
import static tekgenesis.codegen.common.Generators.verifyField;
import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.core.Strings.quoted;

/**
 * Generator for external form interfaces.
 */
public class ExternalFormCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Form            form;
    @NotNull private final String          project;
    @NotNull private final ModelRepository repository;

    //~ Constructors .................................................................................................................................

    /** Create an ExternalFormCodeGenerator. */
    public ExternalFormCodeGenerator(JavaCodeGenerator cg, @NotNull final Form form, @NotNull final String project,
                                     @NotNull final ModelRepository repository) {
        super(cg, form.getName());
        this.form       = form;
        this.project    = project;
        this.repository = repository;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return form.getSourceName();
    }

    protected void populate() {
        withComments("Generated external form reference to " + form.getName() + " on project " + project + ".");
        withSuperclass(generic("tekgenesis.external.ExternalInstance", form.getName()));

        constructor().asPrivate().invokeSuper(quoted(project), classOf(form.getName()));

        createNavigateMethods();

        field(INSTANCE, form.getName(), new_(form.getName())).asStatic().asFinal().asPrivate().notNull();
    }

    private void createNavigateMethods() {
        // Create keyless navigate method
        navigate().return_(invoke(INSTANCE, NAVIGATION)).withComments("Return external navigate action.");

        final List<Key> keys = flattenKeys();
        if (!keys.isEmpty()) {
            // Create typed flattened key navigate method
            if (keys.size() != 1) {
                final Method navigate = navigate();
                navigate.assign("String pk", EMPTY);
                for (final Key key : keys) {
                    navigate.arg(key.getName(), key.getType().getImplementationClassName()).notNull();
                    navigate.assign(PK, PK + CAT + verified(key));
                }
                navigate.return_(invoke("", NAVIGATE, PK)).withComments(RETURN_NAVIGATE);
            }
            else {
                final Key pk = keys.get(0);
                if (!pk.getType().isString())
                    // Special inline implementation
                    navigate().return_(invoke("", NAVIGATE, EMPTY + CAT + verified(pk)))
                        .withComments(RETURN_NAVIGATE)
                        .arg(pk.getName(), pk.getType().getImplementationClassName())
                        .notNull();
            }

            // Create string navigate method
            navigate().return_(invoke(INSTANCE, NAVIGATION, KEY_ARG)).withComments(RETURN_NAVIGATE).arg("key", String.class).notNull();
        }
    }

    private void flattenDbFields(@NotNull DbObject dbObject, @NotNull List<Key> result, @NotNull String prefix) {
        for (final Attribute field : dbObject.getPrimaryKey()) {
            final Key e = new Key(field, prefix);
            if (!field.getType().isDatabaseObject()) result.add(e);
            else flattenDbFields((DbObject) field.getType(), result, e.getName());
        }
    }

    private List<Key> flattenKeys() {
        final List<Key> result = new ArrayList<>();
        for (final ModelField key : form.getPrimaryKey()) {
            final Key e = new Key(key);
            if (!key.getType().isDatabaseObject()) result.add(e);
            else flattenDbFields(repository.getModel(key.getType().getImplementationClassName(), DbObject.class).get(), result, e.getName());
        }
        return result;
    }

    private Method navigate() {
        return method(NAVIGATE, generic("tekgenesis.form.ExternalNavigate", form.getName())).asStatic().notNull();
    }

    private String verified(@NotNull Key key) {
        return key.getType().isString() ? invokeStatic(Strings.class, ESCAPE_CHAR_ON, key.getName(), "':'") : verifyField(this, key.getName(), key);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String RETURN_NAVIGATE = "Return external navigate action with specified primary key.";

    //~ Inner Classes ................................................................................................................................

    private static class Key implements ModelField, Signed {
        private final ModelField field;
        private final String     scope;

        private Key(ModelField field) {
            this(field, "");
        }

        private Key(ModelField field, String scope) {
            this.field = field;
            this.scope = scope;
        }

        @Override public boolean hasChildren() {
            return field.hasChildren();
        }

        @NotNull @Override public ImmutableList<? extends ModelField> getChildren() {
            return field.getChildren();
        }

        @Override public boolean isSigned() {
            return field instanceof Signed && ((Signed) field).isSigned();
        }

        @NotNull @Override public String getLabel() {
            return field.getLabel();
        }

        @NotNull @Override public String getName() {
            return isEmpty(scope) ? field.getName() : scope + capitalizeFirst(field.getName());
        }

        @NotNull @Override public FieldOptions getOptions() {
            return field.getOptions();
        }

        @NotNull @Override public Type getType() {
            return field.getType();
        }

        @Override public void setType(@NotNull Type type) {
            field.setType(type);
        }
    }  // end class Key
}  // end class ExternalFormCodeGenerator
