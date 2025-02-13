
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

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.codegen.context.InterfaceTypeCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.impl.java.JavaItemGenerator;
import tekgenesis.codegen.type.StructTypeCodeGenerator;
import tekgenesis.codegen.type.UserStructTypeClassGenerator;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.common.ModelLinkerImpl;
import tekgenesis.metadata.entity.*;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.exception.inheritance.ExtendsFromFinalException;
import tekgenesis.metadata.exception.inheritance.InterfaceExtendsOnlyException;
import tekgenesis.metadata.exception.inheritance.MultipleInheritanceException;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.tools.test.Tests.checkDiff;
import static tekgenesis.common.util.Files.changeExtension;
import static tekgenesis.expr.ExpressionFactory.integer;
import static tekgenesis.expr.ExpressionFactory.str;
import static tekgenesis.type.Modifier.FINAL;
import static tekgenesis.type.Types.*;

/**
 * Test Struct Type Code Generator;
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class StructTypeGenerationTest {

    //~ Instance Fields ..............................................................................................................................

    private final File goldenDir = new File("codegen/metamodel/src/test/data");

    private final File outputDir = new File("target/codegen/metamodel/test-output");

    //~ Methods ......................................................................................................................................

    @Test public void compositeStructType()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.ProductComparison");

        final Type productType = createSimpleProductType();

        final StructBuilder builder = StructBuilder.struct(name).withModifiers(FINAL);
        builder.addField(field("left", productType, "Left product"));
        builder.addField(field("right", productType, "Right product"));

        final StructType type = builder.build();
        assertTypeGeneration(type);
        assertUserClass(type);
    }

    @Test public void compositeStructTypewithArgs()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.ProductCompositeWithArgs");

        final Type productType = createSimpleProductType2();

        final StructBuilder builder = StructBuilder.struct(name).withModifiers(FINAL);
        builder.addField(field("left", productType, "Left product"));
        builder.addField(field("right", productType, "Right product"));

        final StructType type = builder.build();
        assertTypeGeneration(type);
        assertUserClass(type);
    }

    @Test public void compositeWithOptionalTypes()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.OptionalTypeComposition");

        final Type productType = createSimpleProductType();

        final StructBuilder builder = StructBuilder.struct(name);
        builder.addField(field("left", productType, "Left product").optional());
        builder.addField(field("right", productType, "Right product").optional());

        final StructType type = builder.build();
        assertTypeGeneration(type);
        assertUserClass(type);
    }

    @Test public void compositeWithSimpleType()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.ComposedType");

        final TypeDefBuilder simpleType = TypeDefBuilder.typeDef(name, stringType(8));

        final StructBuilder builder = StructBuilder.struct(name);

        builder.addField(field("left", simpleType.build(), "Left product"));

        final StructType type = builder.build();
        assertTypeGeneration(type);
        assertUserClass(type);
    }

    @Test public void compositionWithSimpleType()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.SimpleType");

        final TypeDefBuilder simpleType = TypeDefBuilder.typeDef(name, stringType(8));

        final QName         compositeTypeQname = createQName("tekgenesis.test.CompositeFromSimpleType");
        final StructBuilder builder            = StructBuilder.struct(compositeTypeQname).withModifiers(FINAL);
        builder.addField(field("left", simpleType.build(), "Left product"));

        final StructType type = builder.build();
        assertTypeGeneration(type);
        assertUserClass(type);
    }

    @Test public void emptyType() {
        final QName name = createQName("tekgenesis.test.EmptyType");

        final StructBuilder builder = StructBuilder.struct(name);

        final StructType type = builder.build();
        assertTypeGeneration(type);
        assertUserClass(type);
    }

    @Test(expected = ExtendsFromFinalException.class)
    public void extendsFromFinalType()
        throws DuplicateFieldException, InterfaceExtendsOnlyException, MultipleInheritanceException, ExtendsFromFinalException
    {
        final StructBuilder aBuilder = StructBuilder.struct(createQName("tekgenesis.test.OriginType"));
        aBuilder.withModifier(FINAL).addArgument(field("id", intType(), "id"));
        final StructType type = aBuilder.build();
        assertTypeGeneration(type);
        assertUserClass(type);

        final StructBuilder builder = StructBuilder.struct(createQName("tekgenesis.test.ExtendsFromFinalType"));
        builder.withSuperType(type);
        final StructType structType = builder.build();
        assertTypeGeneration(structType);
        assertUserClass(structType);
    }

    @Test public void extendsType()
        throws BuilderException
    {
        final StructBuilder aBuilder = StructBuilder.struct(createQName("tekgenesis.test.AType"));
        aBuilder.asInterface().addArgument(field("id", intType(), "id"));
        final StructType type = aBuilder.build();
        assertInterfaceTypeGeneration(type);

        final StructBuilder builder = StructBuilder.struct(createQName("tekgenesis.test.BType"));
        builder.withSuperType(type);
        final StructType structType = builder.build();
        assertTypeGeneration(structType);
        assertUserClass(structType);
    }

    @Test public void extendTypeWithArgs()
        throws BuilderException
    {
        final StructBuilder aBuilder = StructBuilder.struct(createQName("tekgenesis.test.AParentType"));
        aBuilder.addArgument(field("id", intType(), "id"));
        final StructType type = aBuilder.build();
        assertTypeGeneration(type);
        assertUserClass(type);

        final StructBuilder bBuilder = StructBuilder.struct(createQName("tekgenesis.test.BExtendedType"));
        bBuilder.withSuperType(type);
        final StructType bType = bBuilder.build();
        assertTypeGeneration(bType);
        assertUserClass(bType);
        assertUserClass(bType);

        final StructBuilder cBuilder = StructBuilder.struct(createQName("tekgenesis.test.CExtendedType"));
        cBuilder.addArgument(field("cId", intType(), "cId"));
        cBuilder.withSuperType(type);
        final StructType cType = cBuilder.build();
        assertTypeGeneration(cType);
        assertUserClass(cType);

        final StructBuilder dBuilder = StructBuilder.struct(createQName("tekgenesis.test.DExtendedType"));
        dBuilder.addArgument(field("dId", intType(), "dId"));
        dBuilder.addArgument(field("dni", intType(), "dni").optional());
        dBuilder.withSuperType(type);
        final StructType dType = dBuilder.build();
        assertTypeGeneration(dType);
        assertUserClass(dType);
    }

    @Test public void multipleStructType()
        throws BuilderException
    {
        assertTypeGeneration(createMultipleProductType());
    }

    @Test public void simpleStructType()
        throws BuilderException
    {
        assertTypeGeneration(createSimpleProductType());
    }

    @Test public void simpleStructType2()
        throws BuilderException
    {
        assertTypeGeneration(createSimpleProductType2());
    }

    @Test public void structTypeWithArrayBasicTypes()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.TypeWithArrayBasicTypes");

        final StructBuilder builder = StructBuilder.struct(name).withModifiers(FINAL);
        builder.addField(field("productIds", arrayType(stringType(8)), "Ids"));
        builder.addField(field("models", arrayType(realType()), "Models."));
        builder.addField(field("prices", arrayType(decimalType(10, 2)), "Prices"));
        builder.addField(field("amounts", arrayType(intType(9)), "Amounts"));
        builder.addField(field("creations", arrayType(dateTimeType()), "Creations"));

        final StructType type = builder.build();
        assertTypeGeneration(type);
        assertUserClass(type);
    }

    @Test public void structWithArguments()
        throws BuilderException
    {
        final QName argName = createQName("tekgenesis.test.ArgumentsType");

        final StructBuilder argBuilder = StructBuilder.struct(argName);
        argBuilder.addArgument(field("name", stringType(8), "Name").withFieldDocumentation("name field doc"));
        argBuilder.addArgument(field("last", stringType(30), "Lastname").withFieldDocumentation("last field doc"));

        final StructType argType = argBuilder.build();
        assertUserClass(argType);
    }

    @Test public void structWithExtendsMatchingFields()
        throws BuilderException
    {
        final QName         baseName    = createQName("tekgenesis.test.BaseStruct");
        final StructBuilder baseBuilder = StructBuilder.struct(baseName);
        baseBuilder.addField(field("productId", stringType(8), "Id"));
        baseBuilder.addField(field("model", stringType(30), "Model No."));

        final StructType baseType = baseBuilder.build();
        assertTypeGeneration(baseType);
        assertUserClass(baseType);

        final QName extendsName = createQName("tekgenesis.test.StructWithExtendsMatchingFields");

        final StructBuilder extendsBuilder = StructBuilder.struct(extendsName).withModifiers(FINAL);

        extendsBuilder.withSuperType(baseType);

        extendsBuilder.addField(field("productId", stringType(8), "Id"));
        extendsBuilder.addField(field("model", stringType(30), "Model No."));
        extendsBuilder.addField(field("description", stringType(100), "Description").optional());
        extendsBuilder.addField(field("price", decimalType(10, 2), "Price"));
        extendsBuilder.addField(field("created", dateTimeType(), "Created"));

        final StructType type = extendsBuilder.build();
        assertTypeGeneration(type);
        assertUserClass(type);
    }

    @Test public void structWithSimpleExtends()
        throws BuilderException
    {
        final QName baseName = createQName("tekgenesis.test.SimpleBaseStruct");

        final StructBuilder baseBuilder = StructBuilder.struct(baseName);
        baseBuilder.addField(field("productId", stringType(8), "Id"));
        baseBuilder.addField(field("model", stringType(30), "Model No."));

        final StructType baseType = baseBuilder.build();
        assertTypeGeneration(baseType);
        assertUserClass(baseType);

        final QName extendsName = createQName("tekgenesis.test.StructWithExtends");

        final StructBuilder extendsBuilder = StructBuilder.struct(extendsName).withModifiers(FINAL).withSuperType(baseType);

        extendsBuilder.addField(field("description", stringType(100), "Description").optional());
        extendsBuilder.addField(field("price", decimalType(10, 2), "Price"));
        extendsBuilder.addField(field("created", dateTimeType(), "Created"));

        final StructType type = extendsBuilder.build();
        assertTypeGeneration(type);
        assertUserClass(type);
    }

    private void assertInterfaceTypeGeneration(@NotNull StructType type) {
        final JavaCodeGenerator          cg   = new JavaCodeGenerator(outputDir, type.getDomain());
        final InterfaceTypeCodeGenerator base = new InterfaceTypeCodeGenerator(cg, type);
        base.generate();

        checkFile(base);
    }

    private void assertTypeGeneration(@NotNull StructType type) {
        final JavaCodeGenerator       cg   = new JavaCodeGenerator(outputDir, type.getDomain());
        final StructTypeCodeGenerator base = new StructTypeCodeGenerator(cg, type);
        base.generate();
        checkFile(base);
    }

    private void assertUserClass(@NotNull StructType type) {
        assertTypeGeneration(type);
        final JavaCodeGenerator cg = new JavaCodeGenerator(outputDir, type.getDomain());

        if (!type.hasModifier(FINAL)) {
            final ClassGenerator user = new UserStructTypeClassGenerator(cg, type, type.getDomain() + ".g").asSerializable();
            user.generate();

            final File userFile = new File(user.getPackageName().replace('.', '/'), changeExtension(user.getTargetFile(), ".j").getName());
            checkDiff(user.getTargetFile(), new File(goldenDir, userFile.getPath()));
        }
    }

    private void checkFile(@NotNull JavaItemGenerator<?> base) {
        final File fileName = new File(base.getPackageName().replace('.', '/'), changeExtension(base.getTargetFile(), ".j").getName());
        checkDiff(base.getTargetFile(), new File(goldenDir, fileName.getPath()));
    }

    //~ Methods ......................................................................................................................................

    static StructType createMultipleProductType()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.ProductList");

        final Type productTypeArray = Types.arrayType(createSimpleProductType());

        final StructBuilder builder = StructBuilder.struct(name).withModifiers(FINAL);
        builder.addField(field("id", stringType(), "List Id"));
        builder.addField(field("products", productTypeArray, "Products"));

        return builder.build();
    }

    static EnumType createSimpleErrorType() {
        final EnumBuilder builder = EnumBuilder.enumType("", "tekgenesis.test", "ErrorEnum").label("Product");
        builder.value("NOT_FOUND", "Some Application Not found error");
        builder.value("ERROR", "Some Application Error");
        return builder.build();
    }

    static StructType createSimpleProductType()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.Product");

        final StructBuilder builder = StructBuilder.struct(name).label("Product");
        builder.addField(field("productId", stringType(8), "Id").defaultValue(str("xyz").createExpression()));
        builder.addField(field("model", stringType(30), "Model No."));
        builder.addField(field("description", stringType(100), "Description").optional());
        builder.addField(field("price", decimalType(10, 2), "Price"));
        builder.addField(field("serial", intType(), "Serial No.").defaultValue(integer(2000000).createExpression()));
        builder.addField(field("created", dateTimeType(), "Created"));

        final StructType result = builder.build();
        new ModelLinkerImpl(new ModelRepository()).link(result);
        return result;
    }

    static TypeFieldBuilder field(@NotNull String id, @NotNull Type type, @NotNull String label) {
        return new TypeFieldBuilder(id, type).label(label);
    }

    private static StructType createSimpleProductType2()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.Product2");

        final StructBuilder builder = StructBuilder.struct(name).withModifiers(FINAL);
        builder.addArgument(field("productId", stringType(8), "Id"));
        builder.addField(field("model", stringType(30), "Model No."));
        builder.addField(field("description", stringType(100), "Description").optional());
        builder.addField(field("price", decimalType(10, 2), "Price"));
        builder.addField(field("created", dateTimeType(), "Created"));

        return builder.build();
    }
}  // end class StructTypeGenerationTest
