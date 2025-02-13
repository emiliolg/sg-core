
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.DynamicConfiguration;
import tekgenesis.form.configuration.DynamicTypeConfiguration;
import tekgenesis.model.KeyMap;
import tekgenesis.type.DynamicTypeConverter;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.form.configuration.DynamicConfiguration.DynamicWidget;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.showcase.g.PropertyTable.PROPERTY;

/**
 * User class for Form: DynamicForm
 */
public class DynamicForm extends DynamicFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changeMailType() {
        final DynamicConfiguration mailConfig = configuration(Field.MAIL);
        mailConfig.getTypeConfiguration().stringType();
        if (isMailInternal()) mailConfig.setWidget(DynamicWidget.TEXT_FIELD);
        else mailConfig.setWidget(DynamicWidget.MAIL_FIELD);
        setMailInternal(!isMailInternal());
        return actions.getDefault();
    }

    @NotNull @Override public Action create() {
        for (final WidgetsRow row : getWidgets()) {
            final Integer         id       = row.getId();
            final DynamicProperty property = id != null ? DynamicProperty.find(id) : DynamicProperty.create();
            if (property != null) {
                row.copyTo(property);
                if (id == null) property.insert();
                else property.update();
            }
        }
        return super.create();
    }

    @Override public void load() {
        setPropertyOptions(selectFrom(PROPERTY).toList());
        final DynamicConfiguration  configuration = configuration(Field.VALUE);
        final FormTable<WidgetsRow> widgets       = getWidgets();
        DynamicProperty.forEach(property -> {
            final WidgetsRow row = widgets.add();
            row.populate(property);
            row.configure();
            row.setValue(property.getObjectValues(configuration.getTypeConverter()));
        });
        final DynamicConfiguration mailConfig = configuration(Field.MAIL);
        mailConfig.getTypeConfiguration().stringType();
        mailConfig.setWidget(DynamicWidget.MAIL_FIELD);

        final DynamicConfiguration radioConfig = configuration(Field.RADIO_DYNAMIC);
        radioConfig.getTypeConfiguration().stringType();
        radioConfig.setWidget(DynamicWidget.RADIO_GROUP);
        setRadioDynamicOptions(KeyMap.fromValues(listOf("one", "two")));

        final DynamicConfiguration rangeConfig = configuration(Field.RANGE);
        rangeConfig.getTypeConfiguration().realType();
        rangeConfig.setWidget(DynamicWidget.RANGE);

        final DynamicConfiguration rangeValueConfig = configuration(Field.RANGE_VALUE);
        rangeValueConfig.getTypeConfiguration().realType();
        rangeValueConfig.setWidget(DynamicWidget.RANGE_VALUE);
    }

    @NotNull @Override public Action onValidate() {
        System.out.println("actions = " + actions);
        return actions.getDefault();
    }

    @NotNull @Override public Action rangeChanged() {
        final KeyMap options = KeyMap.create();
        for (final Object range : getRange())
            options.put(range, null);
        setRangeValueOptions(options);

        return actions.getDefault();
    }

    @NotNull @Override public Action updateDyn() {
        final DynOptions               dynamicType       = getCombo();
        final DynamicConfiguration     configuration     = configuration(Field.DYNIMIC_VALUE);
        final DynamicTypeConfiguration typeConfiguration = configuration.getTypeConfiguration();
        configuration.setSecret(false);
        reset(Field.DYNIMIC_VALUE);

        switch (dynamicType) {
        case STRING:
            typeConfiguration.stringType();
            break;
        case STRING5:
            typeConfiguration.stringType(5);
            break;
        case INT:
            typeConfiguration.intType();
            break;
        case INT5:
            typeConfiguration.intType(5);
            break;
        case REAL:
            typeConfiguration.realType();
            break;
        case DECIMAL:
            typeConfiguration.decimalType();
            break;
        case DECIMAL42:
            typeConfiguration.decimalType(4, 2);
            break;
        case DATE:
            typeConfiguration.dateType();
            break;
        case STRING_SECRET:
            typeConfiguration.stringType();
            configuration.setSecret(true);
            break;
        }

        return actions.getDefault();
    }  // end method updateDyn

    //~ Inner Classes ................................................................................................................................

    public class WidgetsRow extends WidgetsRowBase {
        @NotNull @Override public Action clear() {
            setValue(Colls.emptyIterable());
            return actions.getDefault();
        }

        /** Configure dynamic row. */
        public void configure() {
            reset(Field.VALUE);
            final DynamicConfiguration configuration = configuration(Field.VALUE);
            final Property             property      = getProperty();
            property.setDynamicType(configuration.getTypeConfiguration());
            final DynamicWidget widget = property.getDynamicWidget(property.getType());
            if (widget != DynamicWidget.NONE) configuration.setWidget(widget);
            setValueOptions(property.getOptions(configuration.getTypeConverter()));
        }

        @Override public void copyTo(@NotNull DynamicProperty property) {
            super.copyTo(property);
            final DynamicConfiguration configuration = configuration(Field.VALUE);
            property.setObjectValues(getValue(), configuration.getTypeConverter());
        }

        @Override public void populate(@NotNull DynamicProperty property) {
            super.populate(property);
            final DynamicConfiguration configuration = configuration(Field.VALUE);
            final DynamicTypeConverter typeConverter = configuration.getTypeConverter();
            setValue(property.getObjectValues(typeConverter));
            property.setObjectValues(getValue(), typeConverter);
        }

        @NotNull @Override public Action updateDynamic() {
            configure();
            return actions.getDefault();
        }
    }
}  // end class DynamicForm
