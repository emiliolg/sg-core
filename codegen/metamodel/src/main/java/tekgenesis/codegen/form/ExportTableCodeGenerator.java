
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import java.io.IOException;
import java.io.OutputStream;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.metadata.form.widget.ExportType;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.LABEL;
import static tekgenesis.codegen.form.FormBaseCodeGenerator.getEnumField;
import static tekgenesis.codegen.type.StructTypeCodeGenerator.STREAM;
import static tekgenesis.common.core.Constants.STRING;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.core.Strings.getterName;
import static tekgenesis.metadata.form.widget.WidgetType.*;
import static tekgenesis.metadata.form.widget.WidgetTypes.isGroup;

class ExportTableCodeGenerator extends ClassGenerator {

    //~ Constructors .................................................................................................................................

    ExportTableCodeGenerator(UiModelBaseCodeGenerator<?> parent, String name, MultipleWidget tableWidget, ExportType type) {
        super(parent, name);

        withInterfaces(extractStaticImport(createQName("tekgenesis.form.Download.DownloadWriter")));

        implementInto(type, tableWidget.getName());
        implementGetRows(tableWidget.getName());
        implementWriteHeader(tableWidget);
        implementWriteRows(tableWidget);
    }

    //~ Methods ......................................................................................................................................

    private String getterFormTable(String tableName) {
        return getterName(tableName, STRING);
    }

    private void implementGetRows(String tableName) {
        method(getTableRowsMethodName(tableName), generic(Iterable.class, getTableRowClassName(tableName))).asProtected()
            .notNull()
            .return_(invoke(getterFormTable(tableName)));
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private void implementInto(ExportType type, String tableName) {
        final Method into = method("into").override().asPublic().throws_(IOException.class);
        into.arg(STREAM, OutputStream.class);
        into.declareNew("tekgenesis.form.export.FormTableExporter", extractImport(type.getClassName()), EXPORTER, new String[] { STREAM });
        into.blankLine();

        into.statement(invoke(EXPORTER, TITLE, invoke("", LABEL, getEnumField(tableName))));
        into.blankLine();

        into.statement(invoke("", getWriteHeaderMethodName(tableName), invoke(EXPORTER, "header")));
        into.blankLine();

        into.startForEach(getTableRowClassName(tableName), "row", invoke(getTableRowsMethodName(tableName)));

        into.statement(invoke("", getWriteRowsMethodName(tableName), invoke(EXPORTER, "row"), "row"));

        into.endFor();
        into.blankLine();

        into.statement(invoke(EXPORTER, BUILD_METHOD));
        into.statement(invoke(STREAM, FLUSH_METHOD));
    }

    private void implementWriteHeader(MultipleWidget table) {
        final Method writeHeader = method(getWriteHeaderMethodName(table.getName())).asProtected();
        writeHeader.arg("e", "tekgenesis.form.export.FormTableExporter.FormTableHeaderExporter").asFinal().notNull();

        for (final Widget col : table) {
            if (isNotAnSkippedWidget(col.getWidgetType()))
                writeHeader.statement((invoke("e", ADD_CONTENT_METHOD, invoke("", LABEL, getEnumField(col.getName())))));
        }
    }

    private void implementWriteRows(MultipleWidget table) {
        final String tableName = table.getName();
        final Method writeRows = method(getWriteRowsMethodName(table.getName())).asProtected();
        writeRows.arg("e", "tekgenesis.form.export.FormTableExporter.FormTableRowExporter").asFinal().notNull();
        writeRows.arg("row", getTableRowClassName(tableName)).asFinal().notNull();

        table.forEach(col -> {
            if (isNotAnSkippedWidget(col.getWidgetType()))
                writeRows.statement(
                    invoke("e", ADD_CONTENT_METHOD, isGroup(col.getWidgetType()) ? getGetterForGroups(col, true) : invokeWidgetGetter(col)));
        });
    }

    private String invokeWidgetGetter(Widget col) {
        return invoke("row", getterName(col.getName(), col.getType().getImplementationClassName()));
    }

    private String getGetterForGroups(Widget group, boolean firstWidget) {
        boolean             initialFirstWidget = firstWidget;
        final StringBuilder stringBuilder      = new StringBuilder();

        for (final Widget widget : group) {
            if (isGroup(widget.getWidgetType())) stringBuilder.append(getGetterForGroups(widget, initialFirstWidget));
            else {
                if (!initialFirstWidget) stringBuilder.append(" + " + "\" \"" + " + ");
                stringBuilder.append(invokeWidgetGetter(widget));
                initialFirstWidget = false;
            }
        }
        return stringBuilder.toString();
    }  // end method getGetterForGroups

    private boolean isNotAnSkippedWidget(WidgetType widgetType) {
        return !widgetType.isGroup() && widgetType != INTERNAL && widgetType != BUTTON && widgetType != WidgetType.LABEL && widgetType != DROPDOWN;
    }

    private String getTableRowClassName(String tableName) {
        return capitalizeFirst(tableName) + "Row";
    }

    private String getTableRowsMethodName(String tableName) {
        return getterFormTable(tableName) + "Rows";
    }

    private String getWriteHeaderMethodName(String tableName) {
        return WRITE + capitalizeFirst(tableName) + "Header";
    }

    private String getWriteRowsMethodName(String tableName) {
        return WRITE + getTableRowClassName(tableName);
    }

    //~ Static Fields ................................................................................................................................

    private static final String EXPORTER           = "exporter";
    private static final String WRITE              = "write";
    private static final String ADD_CONTENT_METHOD = "addContent";
}  // end class ExportTableCodeGenerator
