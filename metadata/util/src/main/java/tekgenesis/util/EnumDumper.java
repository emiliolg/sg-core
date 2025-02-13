
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.IndentedWriter;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.EnumType;
import tekgenesis.type.EnumValue;

import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * A visitor to generate the Database Schema.
 */
class EnumDumper extends ModelDumper {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final EnumType enumType;

    //~ Constructors .................................................................................................................................

    EnumDumper(@NotNull EnumType enumType, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        super(enumType, repository, writer, preferences);
        this.enumType = enumType;
    }

    //~ Methods ......................................................................................................................................

    @Override void dumpFields() {
        if (enumType.hasFields()) {
            super.dumpFields();
            newLine();
        }
        dumpValues();
    }

    @Override void dumpModelOptions() {
        final String defaultForm = enumType.getDefaultForm();
        if (defaultForm.isEmpty() && !enumType.hasFields()) space();
        else {
            newLine();
            indent();
            dumpDefaultForm(defaultForm);
            dumpPrimaryKey();
            dumpIndex();
            unIndent();
            if (enumType.hasFields()) print(WITH).space();
        }
    }  // end method dumpModelOptions

    private void dumpDefaultForm(String defaultForm) {
        if (!defaultForm.isEmpty()) print(FORM).space().print(defaultForm).newLine();
    }

    private void dumpIndex() {                           //
        enumType.getField(enumType.getIndexFieldName())  //
        .ifPresent(f -> print(INDEX).space().print(f.getName()).newLine());
    }

    private void dumpPrimaryKey() {                   //
        enumType.getField(enumType.getPkFieldName())  //
        .ifPresent(f -> print(PRIMARY_KEY).space().print(f.getName()).newLine());
    }

    private void dumpValue(EnumValue value) {
        newLine();
        final String name = value.getName();
        print(name);
        final String   label  = getLabel(value);
        final Object[] values = value.getValues();
        if (value.hasLabel() || values.length != 0 || isFull()) {
            final int n = maxName - name.length();
            if (n >= 0) spaces(n);
            else newLine().spaces(maxName);
            colon().startList().printElement(label);
            int i = 0;
            for (final Object o : values) {
                if (enumType.getExtraFieldsTypes()[i].getFinalType().isString() && o != null) printElement(quoted(o.toString()));
                else if (o != null) printElement(String.valueOf(o));
                i++;
            }
        }
        semicolon();
        dumpFieldDocumentation(value.getFieldDocumentation());
    }

    private void dumpValues() {
        print("{").indent();
        computeLengths(enumType.getValues());

        for (final EnumValue value : enumType.getValues())
            dumpValue(value);

        unIndent().newLine().print("}");
    }
}
