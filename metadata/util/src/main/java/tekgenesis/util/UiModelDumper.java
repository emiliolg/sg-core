
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.IndentedWriter;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.*;
import tekgenesis.type.permission.Permission;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Constants.SEQ_ID;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.QName.removeQualification;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.metadata.form.widget.Form.DEFAULT_SCHEDULE_INTERVAL;
import static tekgenesis.metadata.form.widget.WidgetTypes.*;
import static tekgenesis.mmcompiler.ast.MMToken.COLON;
import static tekgenesis.mmcompiler.ast.MMToken.LISTING;
import static tekgenesis.mmcompiler.ast.MMToken.PERMISSIONS;
import static tekgenesis.util.CheckDefault.isDefaultWidget;

/**
 * A visitor to generate the Database Schema.
 */
class UiModelDumper extends ModelDumper {

    //~ Instance Fields ..............................................................................................................................

    private final ModelRepository repository;

    private final UiModel uiModel;

    //~ Constructors .................................................................................................................................

    UiModelDumper(UiModel uiModel, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        super(uiModel, repository, writer, preferences);
        this.repository = repository;
        this.uiModel    = uiModel;
    }

    //~ Methods ......................................................................................................................................

    @Override protected void dumpModelOptions() {
        if (uiModel instanceof WidgetDef) {
            final Option<MetaModel> binding = repository.getModel(uiModel.getBinding());
            space();
            if (binding.isPresent()) printBinding(binding, false).space();
            return;
        }
        final Form form = (Form) uiModel;
        if (printAsExternal(form)) {
            newLine().indent().print(MMToken.PROJECT).space().print(quoted(getProject())).unIndent();
            // Just print project
            return;
        }

        final Option<MetaModel> entity = repository.getModel(form.getBinding());

        if (entity.isEmpty()) space();
        else {
            if (!printAsDefault(form)) newLine().indent();
            else space();

            printBinding(entity, form.isListing());

            if (!printAsDefault(form)) unIndent();
        }

        final String primaryKeys = getNonDefaultPrimaryKeyString(form);
        if (isNotEmpty(primaryKeys)) newLine().indent().print(MMToken.PRIMARY_KEY).space().print(primaryKeys).unIndent();

        final Seq<Permission> permissions = form.getPermissions().filter(permission -> permission != null && !permission.isDefault());
        if (isNotEmpty(permissions)) newLine().print(PERMISSIONS).space().print(permissions.mkString(", "));

        final String onLoadMethodName = form.getOnLoadMethodName();
        if (isNotEmpty(onLoadMethodName)) newLine().indent().print(MMToken.ON_LOAD).space().print(onLoadMethodName).unIndent();

        final String onDisplayMethodName = form.getOnDisplayMethodName();
        if (isNotEmpty(onDisplayMethodName)) newLine().indent().print(MMToken.ON_DISPLAY).space().print(onDisplayMethodName).unIndent();

        final String onCancelMethodName = form.getOnCancelMethodName();
        if (isNotEmpty(onCancelMethodName)) newLine().indent().print(MMToken.ON_CANCEL).space().print(onCancelMethodName).unIndent();

        final String onScheduleMethodName = form.getOnScheduleMethodName();
        if (isNotEmpty(onScheduleMethodName)) {
            newLine().indent().print(MMToken.ON_SCHEDULE).space().print(onScheduleMethodName);
            if (form.getOnScheduleInterval() != DEFAULT_SCHEDULE_INTERVAL) space().print(form.getOnScheduleInterval());
            unIndent();
        }

        if (form.isUnrestricted()) newLine().indent().print(MMToken.UNRESTRICTED).unIndent();

        if (!printAsDefault(form)) newLine();
    }  // end method dumpModelOptions

    @Override protected boolean mustDumpField(ModelField w) {
        return !SEQ_ID.equals(extractName(((Widget) w).getBinding()));
    }

    @Override ModelDumper dump() {
        if (uiModel instanceof Form && (printAsDefault((Form) uiModel) || printAsExternal((Form) uiModel))) {
            beginModel().dumpModelOptions();
            print(";");
        }
        else super.dump();

        return this;
    }

    @Override void dumpModelLabel() {
        if (uiModel instanceof Form && printAsDefault((Form) uiModel)) {
            final String label = uiModel.getRawLabel();
            if (!isEmpty(label)) space().print(quoted(label));
        }
        else super.dumpModelLabel();
    }

    @Override void dumpModifiers() {
        if (uiModel instanceof Form && printAsExternal((Form) uiModel)) print(Modifier.EXTERNAL.getId()).space();
        else super.dumpModifiers();
    }

    @Override void dumpType(ModelField field) {
        final Widget     widget     = (Widget) field;
        final WidgetType widgetType = widget.getWidgetType();

        if (!isMultiple(widgetType) && widget.hasChildren()) {
            // Not sure why....
            printElement(widgetType.getId());
            return;
        }

        final String  boundField = widget.getBinding();
        final Type    type       = widget.getOriginalType();
        final boolean isDefault  = !isFull() && isDefaultWidget(widget, repository);
        if (widget.isPlaceholderBinding()) printElement("_");
        else if (!boundField.isEmpty()) printElement(removeQualification(boundField, getBoundModel(widget)));
        else if (isDefault || !type.isNull() && !getTypeFor(widgetType).equivalent(type) && !isTypeInferred(field))
            printElement(removeQualification(type.toString(), uiModel.getDomain()));

        if (!isDefault) printElement(widgetType.toString() + argumentsToString(getArguments(widgetType), widget.getOptions()));
    }

    @Override void printNameAndLabel(ModelField w) {
        final String b = extractName(((Widget) w).getBinding());
        if (equal(w.getName(), b)) printNameAndLabel(w, false);
        else super.printNameAndLabel(w, true);
    }

    @Override boolean isInferred(FieldOption option, FieldOptions allOptions) {
        switch (option) {
        case DISABLE:
            return allOptions.containsKey(FieldOption.IS);

        default:
            return false;
        }
    }

    @Override boolean isTypeInferred(ModelField field) {
        if (field instanceof MultipleWidget)
            return (((MultipleWidget) field).getWidgetType() == WidgetType.TABLE) && (field.getType() == Types.stringType());
        return super.isTypeInferred(field);
    }

    private boolean printAsDefault(final Form form) {
        return form.isGenerated() && !isFull();
    }

    private boolean printAsExternal(final Form form) {
        return isPrintRemoteAsExternal() && form.isRemote();
    }

    private ModelDumper printBinding(Option<MetaModel> binding, final boolean listing) {
        final MetaModel metaModel     = binding.get();
        final String    metaModelName = uiModel.getDomain().equals(metaModel.getDomain()) ? metaModel.getName() : metaModel.getFullName();
        return print(listing ? LISTING : COLON).space().print(metaModelName);
    }

    private String getBoundModel(Widget widget) {
        final Option<MultipleWidget> tab = widget.getMultiple();
        return tab.isEmpty() ? uiModel.getBinding().getFullName() : tab.get().getType().toString();
    }

    @Nullable private String getNonDefaultPrimaryKeyString(final Form form) {
        ImmutableList<String> boundPKeys  = Colls.emptyList();
        final Seq<String>     primaryKeys = form.getPrimaryKeyAsStrings();
        final MetaModel       binding     = repository.getModel(form.getBinding()).getOrNull();
        if (binding != null && binding.getMetaModelKind() == MetaModelKind.ENTITY)
            boundPKeys = ((Entity) binding).getPrimaryKey().toStrings().toList();
        if (!primaryKeys.isEmpty()) {
            final StringBuilder pkBuilder = new StringBuilder();
            for (final String pk : primaryKeys)
                if (!boundPKeys.contains(pk)) pkBuilder.append(pk).append(",");
            if (pkBuilder.lastIndexOf(",") != -1) pkBuilder.deleteCharAt(pkBuilder.lastIndexOf(","));
            return pkBuilder.toString();
        }
        return null;
    }
}  // end class UiModelDumper
