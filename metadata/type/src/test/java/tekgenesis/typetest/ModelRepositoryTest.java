
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.typetest;

import org.junit.Test;

import test.TestMetaModel;

import tekgenesis.repository.ModelRepository;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.core.QName.createQName;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ModelRepositoryTest {

    //~ Instance Fields ..............................................................................................................................

    final String file1 = "file1";
    final String file2 = "file2";
    final String file3 = "file3";

    final String pkg1 = "tekgenesis.test";
    final String pkg2 = "tekgenesis.test2";

    //~ Methods ......................................................................................................................................

    @Test public void creation() {
        final ModelRepository rep = createRepository();

        assertThat(rep.getModelsByFile(file1).toString()).isEqualTo("(tekgenesis.test.a, tekgenesis.test.b)");
        assertThat(rep.getModelsByFile(file3).toString()).isEqualTo("(tekgenesis.test2.a, tekgenesis.test2.b)");

        assertThat(rep.getModels().toString()).isEqualTo(
            "(tekgenesis.test.a, tekgenesis.test.b, tekgenesis.test.c, tekgenesis.test.d, tekgenesis.test2.a, tekgenesis.test2.b)");

        assertThat(rep.getModel(createQName(pkg2, "a")).get().getSourceName()).isEqualTo(file3);
        assertThat(rep.getModel(createQName(pkg2, "a"), TestMetaModel.class).get().test()).isEqualTo("TEST");

        assertThat(rep.getModel(pkg1, "c").get().getName()).isEqualTo("c");
        assertThat(rep.getModel(pkg2, "c").isPresent()).isFalse();

        assertThat(rep.getDomains().toString()).isEqualTo("(tekgenesis.test, tekgenesis.test2)");

        assertThat(rep.getModels(pkg2).toString()).isEqualTo("(tekgenesis.test2.a, tekgenesis.test2.b)");
    }

    @Test public void update() {
        final ModelRepository rep = createRepository();

        rep.deleteAll(file2);

        assertThat(rep.getModelsByFile(file2).isEmpty()).isTrue();

        assertThat(rep.getModelsByFile(file3).toString()).isEqualTo("(tekgenesis.test2.a, tekgenesis.test2.b)");

        assertThat(rep.getModels().toString()).isEqualTo("(tekgenesis.test.a, tekgenesis.test.b, tekgenesis.test2.a, tekgenesis.test2.b)");

        assertThat(rep.getDomains().toString()).isEqualTo("(tekgenesis.test, tekgenesis.test2)");

        rep.delete(createQName(pkg1, "a"));
        rep.delete(createQName(pkg1, "b"));

        assertThat(rep.getDomains().toString()).isEqualTo("(tekgenesis.test2)");

        assertThat(rep.getModelsByFile(file1).isEmpty()).isTrue();
    }

    private ModelRepository createRepository() {
        final ModelRepository rep = new ModelRepository();

        rep.add(new TestMetaModel(file1, pkg1, "a"));
        rep.add(new TestMetaModel(file1, pkg1, "b"));

        rep.add(new TestMetaModel(file2, pkg1, "c"));
        rep.add(new TestMetaModel(file2, pkg1, "d"));

        rep.add(new TestMetaModel(file3, pkg2, "a"));
        rep.add(new TestMetaModel(file3, pkg2, "b"));
        return rep;
    }
}  // end class ModelRepositoryTest
