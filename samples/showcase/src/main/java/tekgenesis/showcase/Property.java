
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import tekgenesis.form.configuration.DynamicTypeConfiguration;
import tekgenesis.model.KeyMap;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.showcase.g.PropertyBase;
import tekgenesis.type.DynamicTypeConverter;

import static tekgenesis.form.configuration.DynamicConfiguration.DynamicWidget;

/**
 * User class for Entity: Property
 */
@Generated(value = "tekgenesis/showcase/DynamicShowcase.mm", date = "1371478429662")
public class Property extends PropertyBase {

    //~ Methods ......................................................................................................................................

    /**
     * Set dynamic type.
     *
     * @param  typeConfiguration
     */
    public void setDynamicType(DynamicTypeConfiguration typeConfiguration) {
        switch (getType()) {
        case BOOLEAN:
            typeConfiguration.booleanType();
            break;
        case DATE:
            typeConfiguration.dateType();
            break;
        case INT:
            typeConfiguration.intType();
            break;
        case REAL:
            typeConfiguration.realType();
            break;
        default:
            typeConfiguration.stringType();
        }
    }

    /**
     * Return property expected widget or none to keep default.
     *
     * @param  type
     */
    public DynamicWidget getDynamicWidget(PropertyType type) {
        if (getValues().isEmpty()) {
            if (isMultiple() && type == PropertyType.STRING) return DynamicWidget.TAGS;
        }
        else {
            if (isMultiple()) {
                if (type == PropertyType.STRING) return DynamicWidget.TAGS_COMBO_BOX;
                else return DynamicWidget.CHECK_BOX_GROUP;
            }
            else return DynamicWidget.RADIO_GROUP;
        }
        return DynamicWidget.NONE;
    }

    /**
     * Return property typed options.
     *
     * @param  typeConverter
     */
    public KeyMap getOptions(DynamicTypeConverter typeConverter) {
        final InnerEntitySeq<ValidValue> values = getValues();

        if (!values.isEmpty()) {
            final KeyMap options = KeyMap.create();
            for (ValidValue value : values)
                options.put(typeConverter.fromString(value.getValue()), null);  // Delegate value formatting
            return options;
        }
        return KeyMap.EMPTY;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1449310910991872227L;
}
