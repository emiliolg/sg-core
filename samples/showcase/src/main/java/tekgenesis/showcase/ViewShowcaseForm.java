
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;
import tekgenesis.form.configuration.DynamicConfiguration;
import tekgenesis.form.configuration.DynamicTypeConfiguration;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;

/**
 * User class for Form: ViewShowcaseForm
 */
@Generated(value = "tekgenesis/showcase/TextFieldShowcase.mm", date = "1362151611073")
@SuppressWarnings("WeakerAccess")
public class ViewShowcaseForm extends ViewShowcaseFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void onLoad() {
        setMultipleLon(ImmutableList.of("Item 01234567890132456789", "Item 2", "Item 3"));
        setMultipleReal(ImmutableList.of(1.2, 45.6));
        final ImmutableList<SimpleEntity> entities = selectFrom(SIMPLE_ENTITY).list();
        setMultipleEntity(entities);
        setMultipleEntityWithImage(entities);

        for (SimpleEntity entity : entities.getFirst())
            setEntity(entity);
    }

    private void setDynamicType(PropertyType type, DynamicTypeConfiguration typeConfiguration) {
        switch (type) {
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
            break;
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class PropsRow extends PropsRowBase {
        @Override public void copyTo(@NotNull MyProp myProp) {
            super.copyTo(myProp);
            final DynamicConfiguration configuration = configuration(Field.VALUE);
            myProp.setValue(configuration.getTypeConverter().toString(getValue()));
        }

        @Override public void populate(@NotNull MyProp myProp) {
            super.populate(myProp);
            final DynamicConfiguration configuration = configuration(Field.VALUE);
            setValue(configuration.getTypeConverter().fromString(myProp.getValue()));
        }

        @NotNull @Override public Action render() {
            final PropsRow property = getProps().getCurrent();
            property.reset(Field.VALUE);  // Reset old value to prevent incompatible class casts!
            final DynamicConfiguration configuration = property.configuration(Field.VALUE);
            setDynamicType(property.getType(), configuration.getTypeConfiguration());
            return actions.getDefault();
        }
    }
}  // end class ViewShowcaseForm
