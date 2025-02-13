
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import tekgenesis.common.IndentedWriter;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.collections.Stack;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.expr.Expression;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.HasFieldOption;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.SimpleType;
import tekgenesis.metadata.entity.TypeDef;
import tekgenesis.metadata.entity.View;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetTypes;
import tekgenesis.metadata.menu.Menu;
import tekgenesis.metadata.task.Task;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.EnumType;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.QName.removeQualification;
import static tekgenesis.common.core.Strings.maxLength;
import static tekgenesis.mmcompiler.ast.MMToken.WHEN;

/**
 * Dumper common methods.
 */
@SuppressWarnings({ "UnusedReturnValue", "ClassWithTooManyMethods" })
abstract class ModelDumper {

    //~ Instance Fields ..............................................................................................................................

    int                                   maxName;
    private final Collection<FieldOption> ignoredOptions = EnumSet.of(FieldOption.NO_LABEL, FieldOption.EXPAND, FieldOption.FIELD_DOCUMENTATION);

    private final Stack<IntIntTuple> lengths        = Stack.createStack();
    private int                      listLeftMargin;
    private int                      maxLabel;

    private final MetaModel            model;
    private boolean                    multiple;
    private final MMDumper.Preferences preferences;

    private final ModelRepository repository;

    private final IndentedWriter writer;

    //~ Constructors .................................................................................................................................

    ModelDumper(ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        model            = null;
        this.repository  = repository;
        this.writer      = writer;
        this.preferences = preferences;
    }

    ModelDumper(MetaModel model, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        this.model       = model;
        this.repository  = repository;
        this.writer      = writer;
        this.preferences = preferences;
    }

    //~ Methods ......................................................................................................................................

    void advanceMargin() {
        listLeftMargin = writer.getColumn();
    }

    String argumentsToString(List<HasFieldOption> args, final FieldOptions options) {
        if (allDefaults(args, options)) return "";

        final StrBuilder b = new StrBuilder("(");
        b.startCollection(", ");
        for (final HasFieldOption arg : args) {
            final FieldOption o = arg.getFieldOption();

            final String opt = options.toString(o);
            if (opt.isEmpty()) return b.isEmpty() ? "" : b.append(")").toString();

            b.appendElement(optionToString(o, opt));
        }
        b.append(")");
        return b.toString();
    }

    String asString(Type type) {
        return model == null ? type.toString() : removeQualification(type.toString(), model.getDomain());
    }

    ModelDumper beginModel() {
        dumpMetaModelDocumentation();
        dumpModifiers();

        if (model != null) print(model.getMetaModelKind().name().toLowerCase()).space().print(model.getName());

        if (multiple) print("*");

        dumpModelLabel();
        return this;
    }

    ModelDumper colon() {
        return print(" : ");
    }

    ModelDumper comma() {
        return print(MMToken.COMMA).space();
    }

    void computeLengths(Seq<? extends ModelField> fields) {
        lengths.push(Tuple.tuple(maxName, maxLabel));
        maxName  = maxLength(fieldNames(fields), preferences.nameWrapLength);
        maxLabel = maxLength(fieldLabels(fields), preferences.labelWrapLength);
    }

    ModelDumper createModelDumper(MetaModel metaModel) {
        if (metaModel instanceof EnumType) return new EnumDumper((EnumType) metaModel, repository, writer, preferences);
        if (metaModel instanceof UiModel) return new UiModelDumper((UiModel) metaModel, repository, writer, preferences);
        if (metaModel instanceof Entity) return new EntityDumper((Entity) metaModel, repository, writer, preferences);
        if (metaModel instanceof View) return new ViewDumper((View) metaModel, repository, writer, preferences);
        if (metaModel instanceof SimpleType) return new AliasDumper((SimpleType) metaModel, repository, writer, preferences);
        if (metaModel instanceof Menu) return new MenuDumper((Menu) metaModel, repository, writer, preferences);
        if (metaModel instanceof Task) return new TaskDumper((Task) metaModel, repository, writer, preferences);
        if (metaModel instanceof TypeDef) return new TypeDumper((TypeDef) metaModel, repository, writer, preferences);
        throw new IllegalArgumentException("Unknown model: " + metaModel);
    }

    ModelDumper dump() {
        beginModel().dumpModelOptions();
        dumpFields();
        return this;
    }

    void dumpFieldDocumentation(String documentation) {
        if (!documentation.isEmpty()) space().print("//-").space().print(documentation);
    }

    void dumpFields() {
        if (model == null) return;
        print("{").indent();

        computeLengths(model.getChildren());

        for (final ModelField field : model.getChildren()) {
            if (mustDumpField(field)) dumpField(field);
        }
        unIndent().newLine().print("}");
    }

    void dumpModelLabel() {
        if (model == null) return;
        final String label = model.getLabel();
        if (!isEmpty(label)) space().print(Strings.quoted(label));
    }

    void dumpModelOptions() {
        space();
    }

    void dumpModifiers() {
        if (model == null) return;
        for (final Modifier m : Modifier.values()) {
            if (model.hasModifier(m) && m.isPrefix()) print(m.getId()).space();
        }
    }

    void dumpOptions(ModelField field) {
        final FieldOptions options = field.getOptions();
        // Use the order in FieldOption
        for (final FieldOption o : FieldOption.keywords()) {
            if (options.hasOption(o) && !ignoredOptions.contains(o) && !(o == FieldOption.SIGNED && field.getType() instanceof SimpleType) &&
                isMultipleOptionSupported(field, o))
            {
                final String opt = options.toString(o);
                if (!opt.isEmpty() && !isInferred(o, options)) {
                    final String optionToString = optionToString(o, opt);
                    printElement(o.getId() + (isEmpty(optionToString) ? "" : " " + optionToString));
                }
            }
        }
    }

    void dumpType(ModelField field) {
        printElement(asString(field.getType()));
    }

    ModelDumper indent() {
        writer.indent();
        return this;
    }

    /** Mark the Model as a reference to a Multiple Entity. */
    ModelDumper multiple(boolean b) {
        multiple = b;
        return this;
    }

    boolean mustDumpField(ModelField field) {
        return true;
    }

    ModelDumper newLine() {
        writer.newLine();
        return this;
    }

    ModelDumper print(Type type) {
        return print(type.toString());
    }

    ModelDumper print(String str) {
        writer.print(str);
        return this;
    }

    ModelDumper print(int val) {
        writer.print(String.valueOf(val));
        return this;
    }

    ModelDumper print(MMToken token) {
        return print(token.getText());
    }

    ModelDumper print(Expression expression) {
        return print(expression.toString());
    }

    ModelDumper printElement(String str) {
        if (listLeftMargin == -1) advanceMargin();
        else {
            print(MMToken.COMMA);
            final int length = str.length();
            if (writer.getColumn() + length <= preferences.wrapColumn) writer.print(" ");
            else writer.newLine(listLeftMargin);
        }
        return print(str);
    }  // end method printElement

    ModelDumper printf(String format, Object... args) {
        return print(String.format(format, args));
    }

    void printNameAndLabel(ModelField field) {
        printNameAndLabel(field, true);
    }  // end method printNameAndLabel

    void printNameAndLabel(ModelField field, boolean printName) {
        final String name  = getName(field);
        final String label = getLabel(field);
        printNameAndLabel(printName, true, name, label);
    }

    void printNameAndLabel(ModelField field, boolean printName, boolean printColon) {
        final String name  = getName(field);
        final String label = getLabel(field);
        printNameAndLabel(printName, printColon, name, label);
    }

    ModelDumper semicolon() {
        return print(MMToken.SEMICOLON);
    }

    ModelDumper space() {
        return print(" ");
    }

    ModelDumper spaces(int n) {
        for (int i = 0; i < n; i++)
            writer.print(' ');
        return this;
    }

    ModelDumper startList() {
        listLeftMargin = -1;
        return this;
    }

    ModelDumper unIndent() {
        writer.unIndent();
        return this;
    }

    boolean isInferred(FieldOption option, FieldOptions allOptions) {
        return false;
    }

    boolean isTypeInferred(ModelField field) {
        return false;
    }

    boolean isIncludePackage() {
        return preferences.includePackage;
    }

    boolean isFull() {
        return preferences.full;
    }

    boolean isPrintRemoteAsExternal() {
        return preferences.printRemoteAsExternal;
    }

    String getLabel(ModelField field) {
        return isEmpty(field.getLabel()) ? "" : Strings.quoted(field.getLabel());
    }

    String getProject() {
        return preferences.project;
    }

    private void dumpField(ModelField field) {
        newLine();
        printNameAndLabel(field);

        startList();

        dumpType(field);

        dumpOptions(field);

        if (field.hasChildren()) {
            space().print("{").indent();
            final Seq<? extends ModelField> fields = field.getChildren();
            computeLengths(fields);
            for (final ModelField f : fields)
                dumpField(f);
            popLengths();
            unIndent().newLine().print("}");
        }
        semicolon();
        dumpFieldDocumentation(field.getFieldDocumentation());
    }

    private void dumpMetaModelDocumentation() {
        if (model == null || model.getDocumentation().isEmpty()) return;
        print("/**");
        final String[] docs = model.getDocumentation().split("\n");
        for (final String doc : docs)
            newLine().print("*").space().print(doc.trim());
        newLine().print("*/").newLine();
    }

    private String optionToString(FieldOption option, String str) {
        final StringBuilder result = new StringBuilder();

        switch (option.getType()) {
        case STRING_T:
            result.append(Strings.quoted(str));
            break;
        case BOOLEAN_T:
            break;
        case BOOLEAN_EXPR_T:
            if (!str.equals(Boolean.TRUE.toString())) result.append(WHEN.getText()).append(' ').append(str);
            break;
        case METAMODEL_REFERENCE_T:
            if (model != null) result.append(removeQualification(str, model.getDomain()));
            break;
        default:
            result.append(str);
            break;
        }
        return result.toString();
    }

    private void popLengths() {
        final IntIntTuple t = lengths.pop();
        maxName  = t.first();
        maxLabel = t.second();
    }

    private void printLabel(String label, boolean includeSpace) {
        if (includeSpace) space();
        print(label);
        final int labelLength = label.length();
        if (labelLength <= maxLabel) spaces(maxLabel - labelLength);
        else newLine().spaces(maxName + 1 + maxLabel);
    }

    private void printNameAndLabel(boolean printName, boolean printColon, String name, String label) {
        if (name.isEmpty() && label.isEmpty()) return;

        if (printName) {
            print(name);
            final int nameLength = name.length();
            if (nameLength <= maxName) {
                spaces(maxName - nameLength);
                printLabel(label, true);
            }
            else {
                final int s = maxName + 1 + maxLabel - nameLength;
                if (label.isEmpty() && s >= 0) spaces(s);
                else {
                    newLine().spaces(maxName);
                    printLabel(label, true);
                }
            }
        }
        else printLabel(label, false);
        if (printColon) colon();
    }

    private boolean isMultipleOptionSupported(ModelField field, FieldOption o) {
        return !(o == FieldOption.MULTIPLE && field instanceof Widget && WidgetTypes.isArrayValued(((Widget) field).getWidgetType()));
    }

    //~ Methods ......................................................................................................................................

    static Seq<String> fieldNames(Seq<? extends ModelField> fields) {
        return fields.map(ModelDumper::getName);
    }

    private static boolean allDefaults(List<HasFieldOption> args, FieldOptions options) {
        for (final HasFieldOption arg : args) {
            if (!options.equal(arg.getFieldOption(), arg.getDefaultValue())) return false;
        }
        return true;
    }

    private static Seq<String> fieldLabels(Seq<? extends ModelField> attributes) {
        return attributes.map(f -> Strings.quoted(f.getLabel()));
    }

    private static String getName(ModelField field) {
        if (field instanceof Widget && ((Widget) field).hasGeneratedName()) return "";
        return field.getName();
    }
}  // end class ModelDumper
