
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.test;

import org.junit.Test;

import tekgenesis.metadata.common.ModelSetBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.repository.ModelRepository;
import tekgenesis.util.diff.ChangeSet;
import tekgenesis.util.diff.ModelRepositoryDiffer;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.metadata.entity.EntityBuilder.*;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ModelDifferTest {

    //~ Methods ......................................................................................................................................

    @Test public void entityModelDiffer()
        throws BuilderException
    {
        final ModelRepository left = ModelSetBuilder.buildAll(
                entity("Package", "Entity1").label("description")
                                            .primaryKey("attr1")
                                            .describedBy("attr1")
                                            .fields(integer("attr1"), string("attr2", 20), reference("attr3", "Package.Entity2")),

                entity("Package", "Entity2").label("description").primaryKey("attr1").describedBy("attr1").fields(integer("attr1"), string("attr2")));

        final ModelRepository right = ModelSetBuilder.buildAll(
                entity("Package", "Entity1").label("description")
                                            .primaryKey("attr1")
                                            .describedBy("attr1")
                                            .fields(integer("attr1"), integer("attr4"), string("attr2", 30), reference("attr3", "Package.Entity2")),
                entity("Package", "Entity2").label("description").primaryKey("attr1").describedBy("attr1").fields(integer("attr1"), string("attr2")));

        final ChangeSet diff = ModelRepositoryDiffer.diff(left, right);
        assertThat(diff.isEmpty()).isFalse();
    }  // end method entityModelDiffer
}  // end class ModelDifferTest
