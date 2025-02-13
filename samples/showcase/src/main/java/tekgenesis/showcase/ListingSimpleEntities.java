
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

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.showcase.g.SimpleEntityTable.SIMPLE_ENTITY;

/**
 * User class for Form: ListingSimpleEntities
 */
@Generated(value = "tekgenesis/showcase/TextFieldShowcase.mm", date = "1372774912213")
public class ListingSimpleEntities extends ListingSimpleEntitiesBase {

    //~ Methods ......................................................................................................................................

    @Override public void loadSimpleEntities() {
        final FormTable<EntitiesRow> list = getEntities();
        selectFrom(SIMPLE_ENTITY).orderBy(SIMPLE_ENTITY.NAME).forEach(currentSimpleEntity -> list.add().populate(currentSimpleEntity));
    }

    @NotNull @Override public Action reload() {
        return actions.navigate(ListingSimpleEntities.class);
    }

    @NotNull @Override public Action removeSimpleEntity() {
        final EntitiesRow  row                 = getEntities().getCurrent();
        final SimpleEntity currentSimpleEntity = row.isDefined(Field.NAME) ? SimpleEntity.find(row.getName()) : null;
        if (currentSimpleEntity != null) currentSimpleEntity.delete();
        getEntities().removeCurrent();
        return actions.getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class EntitiesRow extends EntitiesRowBase {}
}
