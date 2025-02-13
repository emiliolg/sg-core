
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

/**
 * User class for Form: AnotherSimpleEntityForm
 */
@Generated(value = "tekgenesis/showcase/TableShowcase.mm", date = "1369658604995")
public class AnotherSimpleEntityForm extends AnotherSimpleEntityFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void copyTo(@NotNull AnotherSimpleEntity anotherSimpleEntity) {
        super.copyTo(anotherSimpleEntity);

        if (isDefined(Field.ID)) anotherSimpleEntity.getSimpleEntities().deleteAll();

        for (SimpleEntity simpleEntity : getSimpleEntities()) {
            SimpleEntities add = anotherSimpleEntity.getSimpleEntities().add();
            add.setSimpleEntity(simpleEntity);
        }
    }

    @NotNull @Override public AnotherSimpleEntity populate() {
        AnotherSimpleEntity populate = super.populate();

        List<SimpleEntity> result = new ArrayList<SimpleEntity>();
        for (SimpleEntities simpleEntity : populate.getSimpleEntities())
            result.add(simpleEntity.getSimpleEntity());

        setSimpleEntities(result);

        return populate;
    }
}
