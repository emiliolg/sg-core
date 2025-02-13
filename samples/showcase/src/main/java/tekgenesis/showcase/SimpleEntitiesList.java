
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

import tekgenesis.form.Action;
import tekgenesis.form.MappingCallback;

/**
 * User class for Form: SimpleEntitiesList
 */
public class SimpleEntitiesList extends SimpleEntitiesListBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action createNewOne() {
        return actions.navigate(SimpleEntityForm.class).callback(SimpleEntitiesListCallback.class);
    }

    //~ Inner Classes ................................................................................................................................

    public class EntitiesRow extends EntitiesRowBase {}

    public static class SimpleEntitiesListCallback implements MappingCallback<SimpleEntityForm, SimpleEntitiesList> {
        @Override public void onSave(@NotNull SimpleEntityForm base, @NotNull SimpleEntitiesList out) {
            out.getEntities().clear();
            out.load();
        }
    }
}
