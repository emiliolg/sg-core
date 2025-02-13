
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.exception.inheritance.FieldClashesWithUnrelatedTypeException;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.type.Types.*;

/**
 * Test Type Builder;
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class TypeDefBuilderTest {

    //~ Methods ......................................................................................................................................

    @Test public void documentedSimpleStruct()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.SimpleStruct");

        final StructBuilder builder = StructBuilder.struct(name).withDocumentation("struct type doc");
        builder.addField(field("productId", stringType(8), "Id").withFieldDocumentation("productId doc"));
        builder.addField(field("model", stringType(30), "Model No."));
        builder.addField(field("description", stringType(100), "Description").optional().withFieldDocumentation("description doc"));
        builder.addField(field("price", decimalType(10, 2), "Price").withFieldDocumentation("price doc"));
        builder.addField(field("created", dateTimeType(), "Created"));

        final StructType type = builder.build();

        assertThat(type.getChildren().size()).isEqualTo(5);
        assertThat(type.isInterface()).isFalse();
        assertThat(type.getFullName()).isEqualTo("tekgenesis.test.SimpleStruct");

        assertThat(type.getDocumentation()).isEqualTo("struct type doc");
        assertThat(type.getField("productId").getOrFail("Product id should exist").getFieldDocumentation()).isEqualTo("productId doc");
        assertThat(type.getField("description").getOrFail("description should exist").getFieldDocumentation()).isEqualTo("description doc");
        assertThat(type.getField("price").getOrFail("price should exist").getFieldDocumentation()).isEqualTo("price doc");
    }

    @Test public void interfaceWithExtendsIncompatibleTypes()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.InterfaceWithExtendsIncompatibleTypes");

        final StructBuilder builder = StructBuilder.struct(name);

        builder.withSuperType(createSimpleInterfaceType(QName.createQName("tekgenesis.test.ImaginaryA"), "a"))
            .withSuperType(createSimpleInterfaceType(QName.createQName("tekgenesis.test.ImaginaryB"), "b"));

        try {
            builder.addField(field("a", Types.intType(), "Field " + "a"));
            failBecauseExceptionWasNotThrown(FieldClashesWithUnrelatedTypeException.class);
        }
        catch (final DuplicateFieldException exception) {
            assertThat(exception.getMessage()).isEqualTo(
                "" +
                "'a' in 'tekgenesis.test.InterfaceWithExtendsIncompatibleTypes' clashes with " +
                "'a' in 'tekgenesis.test.ImaginaryA'; fields have unrelated types");
        }
    }

    @Test public void interfaceWithIncompatibleExtendsTypes()
        throws BuilderException
    {
        final StructBuilder a = StructBuilder.struct(createQName("tekgenesis.test.InterfaceA"));
        a.addField(field("a", Types.stringType(), "Field a"));

        final StructType typeA = a.asInterface().build();

        final StructBuilder b = StructBuilder.struct(createQName("tekgenesis.test.InterfaceB"));
        b.addField(field("a", Types.intType(), "Field a"));
        final StructType typeB = b.asInterface().build();

        a.withSuperType(typeA);

        try {
            a.withSuperType(typeB);
            failBecauseExceptionWasNotThrown(FieldClashesWithUnrelatedTypeException.class);
        }
        catch (final DuplicateFieldException exception) {
            assertThat(exception.getMessage()).isEqualTo(
                "" +
                "'a' in 'tekgenesis.test.InterfaceB' clashes with " +
                "'a' in 'tekgenesis.test.InterfaceA'; fields have unrelated types");
        }
    }

    @Test public void simpleStruct()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.SimpleStruct");

        final StructBuilder builder = StructBuilder.struct(name);
        builder.addField(field("productId", stringType(8), "Id"));
        builder.addField(field("model", stringType(30), "Model No."));
        builder.addField(field("description", stringType(100), "Description").optional());
        builder.addField(field("price", decimalType(10, 2), "Price"));
        builder.addField(field("created", dateTimeType(), "Created"));

        final StructType type = builder.build();

        assertThat(type.getChildren().size()).isEqualTo(5);
        assertThat(type.isInterface()).isFalse();
        assertThat(type.getFullName()).isEqualTo("tekgenesis.test.SimpleStruct");
    }

    @Test public void structTypeWithDuplicateFields()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.DuplicateFieldsType");

        final StructBuilder builder = StructBuilder.struct(name);
        builder.addField(field("left", Types.intType(), "Left"));

        try {
            builder.addField(field("left", Types.intType(), "Left"));
            failBecauseExceptionWasNotThrown(DuplicateFieldException.class);
        }
        catch (final DuplicateFieldException e) {
            assertThat(e.getMessage()).isEqualTo("Adding duplicate field left to type DuplicateFieldsType");
        }
    }

    @Test public void structWithExtendsClashingFields()
        throws BuilderException
    {
        final QName         baseName    = createQName("tekgenesis.test.BaseStruct");
        final StructBuilder baseBuilder = StructBuilder.struct(baseName);
        baseBuilder.addField(field("productId", stringType(8), "Id"));
        baseBuilder.addField(field("model", stringType(30), "Model No."));

        final StructType baseType = baseBuilder.build();

        final QName         extendsName    = createQName("tekgenesis.test.StructWithExtendsMatchingFields");
        final StructBuilder extendsBuilder = StructBuilder.struct(extendsName).withSuperType(baseType);

        try {
            extendsBuilder.addField(field("model", intType(), "Model No."));
            failBecauseExceptionWasNotThrown(FieldClashesWithUnrelatedTypeException.class);
        }
        catch (final DuplicateFieldException e) {
            assertThat(e.getMessage()).isEqualTo(
                "" +
                "'model' in 'tekgenesis.test.StructWithExtendsMatchingFields' clashes with " +
                "'model' in 'tekgenesis.test.BaseStruct'; fields have unrelated types");
        }
    }

    @Test public void structWithSimpleExtends()
        throws BuilderException
    {
        final QName baseName = createQName("tekgenesis.test.BaseStruct");

        final StructBuilder baseBuilder = StructBuilder.struct(baseName);
        baseBuilder.addField(field("productId", stringType(8), "Id"));
        baseBuilder.addField(field("model", stringType(30), "Model No."));

        final StructType baseType = baseBuilder.build();

        assertThat(baseType.getChildren().size()).isEqualTo(2);

        final QName         extendsName    = createQName("tekgenesis.test.StructWithExtends");
        final StructBuilder extendsBuilder = StructBuilder.struct(extendsName).withSuperType(baseType);

        extendsBuilder.addField(field("description", stringType(100), "Description").optional());
        extendsBuilder.addField(field("price", decimalType(10, 2), "Price"));
        extendsBuilder.addField(field("created", dateTimeType(), "Created"));

        final StructType extendsType = extendsBuilder.build();

        assertThat(extendsType.getChildren().size()).isEqualTo(3);
    }

    @Test public void typeWithArgs()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.SimpleStruct");

        final StructBuilder builder = StructBuilder.struct(name);

        builder.addArgument(field("productId", stringType(8), "Id"));
        builder.addArgument(field("model", stringType(30), "Model No."));
        builder.addArgument(field("description", stringType(100), "Description").optional());
        builder.addArgument(field("price", decimalType(10, 2), "Price"));
        builder.addArgument(field("created", dateTimeType(), "Created"));

        final StructType type = builder.build();

        assertThat(type.getChildren().size()).isEqualTo(5);
        assertThat(type.isInterface()).isFalse();
        assertThat(type.getFullName()).isEqualTo("tekgenesis.test.SimpleStruct");
    }

    @Test public void typeWithArgsAndFieldDoc()
        throws BuilderException
    {
        final QName argName = createQName("tekgenesis.test.ArgumentsType");

        final StructBuilder argBuilder = StructBuilder.struct(argName);
        argBuilder.addArgument(field("name", stringType(8), "Name").withFieldDocumentation("name field doc"));
        argBuilder.addArgument(field("last", stringType(30), "Lastname").withFieldDocumentation("last field doc"));

        final StructType argType = argBuilder.build();

        final Option<TypeField> name = argType.getField("name");
        Assertions.assertThat(name.isPresent()).isEqualTo(true);
        Assertions.assertThat(name.get().getFieldDocumentation()).isEqualTo("name field doc");

        final Option<TypeField> last = argType.getField("last");
        Assertions.assertThat(last.isPresent()).isEqualTo(true);
        Assertions.assertThat(last.get().getFieldDocumentation()).isEqualTo("last field doc");
    }

    private void addField(StructBuilder builder, String field)
        throws BuilderException
    {
        builder.addField(field(field, Types.stringType(), "Field " + field));
    }

    private StructType createSimpleInterfaceType(QName name, String... fields)
        throws BuilderException
    {
        final StructBuilder builder = StructBuilder.struct(name);
        for (final String field : fields)
            addField(builder, field);
        return builder.asInterface().build();
    }

    //~ Methods ......................................................................................................................................

    private static TypeFieldBuilder field(@NotNull String id, @NotNull Type type, @NotNull String label) {
        return new TypeFieldBuilder(id, type).label(label);
    }
}  // end class TypeDefBuilderTest
