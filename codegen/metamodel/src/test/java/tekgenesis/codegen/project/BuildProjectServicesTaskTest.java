
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.project;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.QName;
import tekgenesis.common.service.Method;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.entity.StructBuilder;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.entity.TypeFieldBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.HandlerBuilder;
import tekgenesis.metadata.handler.RouteBuilder;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Type;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.type.Modifier.FINAL;
import static tekgenesis.type.Types.*;

/**
 * Test Services task generation. Handlers marked as remote and referenced types.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class BuildProjectServicesTaskTest {

    //~ Instance Fields ..............................................................................................................................

    private final File generatedSourcesDir = new File("target/codegen/metamodel/test-output/src_managed");
    private final File outputDir           = new File("target/codegen/metamodel/test-output/build/classes");

    //~ Methods ......................................................................................................................................

    @Test public void testServicesRemoteGeneration()
        throws BuilderException, IOException
    {
        final ModelRepository repository = new ModelRepository();

        final QName name = QName.createQName("tekgenesis.test.HandlerWithReferences");

        final StructType productType = createProductTypeWithSuperType(repository);

        final HandlerBuilder builder = new HandlerBuilder("", name.getQualification(), name.getName()).label(name.getName());

        builder.addRoute(
            new RouteBuilder(name.getFullName()).withPath("/product/create").withHttpMethod(Method.POST).with(FieldOption.BODY, productType));

        final Handler handler = builder.asRemote().build();
        repository.add(handler);

        final ProjectBuilder projectBuilder = new ProjectBuilder(repository, new File(""), outputDir, generatedSourcesDir, Colls.emptyIterable());
        projectBuilder.withRemoteServices();
        final List<File> files = projectBuilder.buildProject();
        assertThat(files).hasSize(4);
        assertThat(files).extracting("name")
            .containsOnly("HandlerWithReferencesRemote.java", "Product.java", "Salable.java", "handler-with-references-module.js");
    }

    private StructType createProductTypeWithSuperType(ModelRepository repository)
        throws BuilderException
    {
        final QName         superName    = createQName("tekgenesis.test.Salable");
        final StructBuilder superBuilder = StructBuilder.struct(superName);
        superBuilder.addField(field("salableId", stringType(8), "Super Id"));
        final StructType superType = superBuilder.build();
        repository.add(superType);

        final QName         name    = createQName("tekgenesis.test.Product");
        final StructBuilder builder = StructBuilder.struct(name).withModifiers(FINAL);
        builder.withSuperType(superType);
        builder.addField(field("productId", stringType(8), "Id"));
        builder.addField(field("price", decimalType(10, 2), "Price"));
        builder.addField(field("created", dateTimeType(), "Created"));

        final StructType type = builder.build();
        repository.add(type);

        return type;
    }

    private TypeFieldBuilder field(@NotNull String id, @NotNull Type type, @NotNull String label) {
        return new TypeFieldBuilder(id, type).label(label);
    }
}  // end class BuildProjectServicesTaskTest
