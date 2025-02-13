
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.codegen.handler.HandlerBaseCodeGenerator;
import tekgenesis.codegen.handler.HandlerCodeGenerator;
import tekgenesis.codegen.handler.RemoteHandlerCodeGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.swagger.SwaggerGenerator;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.QName;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.service.Method;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.entity.StructBuilder;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.entity.TypeDefBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.HandlerBuilder;
import tekgenesis.metadata.handler.ParameterBuilder;
import tekgenesis.metadata.handler.RouteBuilder;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;

import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.codegen.common.MMCodeGenConstants.BASE;
import static tekgenesis.codegen.common.MMCodeGenConstants.REMOTE;
import static tekgenesis.codegen.project.StructTypeGenerationTest.*;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;
import static tekgenesis.common.tools.test.Tests.checkDiff;
import static tekgenesis.type.Types.*;

/**
 * Test Java Handler Code Generator;
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "ClassWithTooManyMethods" })
public class HandlerGenerationTest {

    //~ Instance Fields ..............................................................................................................................

    private final File goldenDir = new File("codegen/metamodel/src/test/data");

    private final File outputDir = new File("target/codegen/metamodel/test-output");

    //~ Methods ......................................................................................................................................

    @Test public void aliasOverloadingRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAliasOverloadingRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void aliasRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAliasRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void anyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAnyRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void htmlRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForHtmlRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void htmlRoutingWithParameters()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForHtmlRoutingWithParameters();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void postWithBodyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForPostWithBodyRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void postWithMultipleBodyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForPostWithMultipleBodyRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void remoteAliasOverloadingRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAliasOverloadingRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteAliasRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAliasRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteAnyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAnyRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteHtmlRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForHtmlRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteHtmlRoutingWithParameters()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForHtmlRoutingWithParameters();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remotePostWithBodyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForPostWithBodyRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remotePostWithMultipleBodyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForPostWithMultipleBodyRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteServiceRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForServiceRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteSpecialCharactersRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForSpecialCharactersRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteTypeAliasOnRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForTypeAliasOnRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteTypedRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForTypedRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void remoteVoidRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForVoidRouting();
        checkHandlerRemoteFileGeneration(builder.asRemote().build());
    }

    @Test public void reouteWithKey()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForRoutesWithKey();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void serviceRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForServiceRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void specialCharactersRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForSpecialCharactersRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void swaggerAnyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForAnyRouting();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerDocumentation()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForDocumentedRouting();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerHtmlRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForHtmlRouting();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerHtmlRoutingWithParameters()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForHtmlRoutingWithParameters();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerInheritance()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForDocumentedInherit();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerPostWithBodyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForPostWithBodyRouting();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerPostWithMultipleBodyRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForPostWithMultipleBodyRouting();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerServiceRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForServiceRouting();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerTypeAliasOnRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForTypeAliasOnRouting();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerTypedRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForTypedRouting();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void swaggerVoidRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForVoidRouting();
        checkHandlerSwaggerFileGeneration(builder.build());
    }

    @Test public void typeAliasOnRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForTypeAliasOnRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void typedRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForTypedRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void validPartsIdsRoutingRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForValidPartsIdsRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    @Test public void voidRouting()
        throws BuilderException
    {
        final HandlerBuilder builder = builderForVoidRouting();
        checkHandlerBaseFileGeneration(builder.build());
    }

    private HandlerBuilder builderForDocumentedInherit()
        throws BuilderException
    {
        // Build Pet type
        final StructBuilder petBuilder = StructBuilder.struct(createQName("tekgenesis.test.Pet")).label("Pet");
        petBuilder.addField(field("petId", stringType(8), "Id"));
        petBuilder.addField(field("name", stringType(20), "Name"));
        final StructType pet = petBuilder.build();

        // Build Cat type with Pet as super type
        final StructBuilder catBuilder = StructBuilder.struct(createQName("tekgenesis.test.Cat")).label("Cat");
        catBuilder.withSuperType(pet);
        catBuilder.addField(field("huntingSkill", stringType(8), "Hunting Skill"));
        final StructType cat = catBuilder.build();

        // Build Handler
        final QName name = createQName("tekgenesis.test.DocumentedInheritHandler");
        return new HandlerBuilder("", name.getQualification(), name.getName()).label("Handler Type Inheritance Documentation")
               .addRoute(route(name, "/home", "home", cat, GET));
    }

    private HandlerBuilder builderForDocumentedRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.DocumentedRoutingHandler");

        final StructBuilder builder = StructBuilder.struct(createQName("tekgenesis.test.Product")).label("Product");
        builder.addField(field("productId", stringType(8), "Id"));
        builder.addField(field("model", stringType(30), "Model No."));
        builder.addField(field("description", stringType(100), "Description").optional());
        builder.addField(field("price", decimalType(10, 2), "Price").defaultValue(Expression.createConstant("0.0")));
        builder.addField(field("created", dateTimeType(), "Created"));
        builder.addField(field("hasSales", booleanType(), "Has Sales").defaultValue(Expression.createConstant("false")));

        final StructType type = builder.build();

        final EnumType errorType = createSimpleErrorType();

        return new HandlerBuilder("", name.getQualification(), name.getName()).label("Handler Documentation")
               .withRaiseEnum(errorType)
               .addRoute(
                route(name, "/home", "home", type, GET).withParameter(parameter("param", stringType(), "Param Documentation"))
                                                       .withBody(type)
                                                       .withSummary("Route Documentation"));
    }

    private void checkFile(Handler handler, String suffix) {
        final String fileName = qualify(handler.getDomain(), handler.getImplementationClassName()).replaceAll("\\.", File.separator) + suffix;
        checkDiff(new File(outputDir, fileName + Constants.JAVA_EXT), new File(goldenDir, fileName + ".j"));
    }

    private void checkHandlerBaseFileGeneration(@NotNull Handler handler) {
        final JavaCodeGenerator        cg   = new JavaCodeGenerator(outputDir, handler.getDomain());
        final HandlerBaseCodeGenerator base = new HandlerBaseCodeGenerator(cg, handler);
        base.generate();
        final HandlerCodeGenerator user = new HandlerCodeGenerator(cg, handler, base);
        user.generate();

        checkFile(handler, BASE);
    }

    private void checkHandlerRemoteFileGeneration(@NotNull Handler handler) {
        final JavaCodeGenerator          cg   = new JavaCodeGenerator(outputDir, handler.getDomain());
        final RemoteHandlerCodeGenerator base = new RemoteHandlerCodeGenerator(cg, handler);
        base.generate();

        checkFile(handler, REMOTE);
    }

    private void checkHandlerSwaggerFileGeneration(@NotNull Handler handler) {
        final String fileName   = qualify(handler.getDomain(), handler.getImplementationClassName()).replaceAll("\\.", File.separator) + SWAGGER +
                                  JSON_EXT;
        final File   outputFile = new File(outputDir, fileName);

        final SwaggerGenerator swagger = SwaggerGenerator.createLocal(Colls.listOf(handler));
        final ObjectMapper     mapper  = JsonMapping.shared();
        final ObjectNode       result  = swagger.json(mapper.createObjectNode());
        try {
            mapper.writer().withDefaultPrettyPrinter().writeValue(outputFile, result);

            final File goldenFile = new File(goldenDir, fileName);
            checkDiff(outputFile, goldenFile);
        }
        catch (final IOException e) {
            fail("Failed to write json in " + outputFile.getAbsolutePath(), e);
        }
    }

    private ParameterBuilder parameter(String name, Type type, String label) {
        return new ParameterBuilder(name, type).description(label);
    }

    //~ Methods ......................................................................................................................................

    static HandlerBuilder builderForAliasOverloadingRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.AliasOverloadingHandler");

        return new HandlerBuilder("", name.getQualification(), name.getName())  //
               .addRoute(route(name, "/$a", "target", htmlType(), GET))      //
               .addRoute(route(name, "$a/$b", "target", htmlType(), GET))    //
               .addRoute(route(name, "$a/$b/$c", "target", htmlType(), GET));
    }

    static HandlerBuilder builderForAliasRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.AliasHandler");

        return new HandlerBuilder("", name.getQualification(), name.getName())                   //
               .addRoute(route(name, "/a/$id", "target", htmlType(), GET))                       //
               .addRoute(route(name, "/b/$id", "target", htmlType(), GET))                       //
               .addRoute(route(name, "/c/$id", "target", htmlType(), POST).withBody(stringType()))  //
               .addRoute(route(name, "/failure", "failure", stringType(), GET))                  //
               .addRoute(route(name, "/failure", "failure", stringType(), POST).withBody(stringType()));
    }

    static HandlerBuilder builderForAnyRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.AnyHandler");

        return new HandlerBuilder("", name.getQualification(), name.getName())  //
               .addRoute(route(name, "/a/$id:String", "str", anyType(), POST));
    }

    static HandlerBuilder builderForHtmlRoutingWithParameters()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.HtmlRoutingHandlerWithParameters");

        final RouteBuilder nothing = route(name, "/nothing", "nothing", htmlType(), GET);

        final RouteBuilder home = route(name, "/home", "home", htmlType(), GET)  //
                                  .withParameter(parameter("from", stringType()))  //
                                  .withParameter(parameter("to", stringType()));

        final RouteBuilder someId = route(name, "/some/$id", "someId", htmlType(), GET)  //
                                    .withParameter(parameter("scheme", stringType()))  //
                                    .withParameter(parameter("type", stringType()));

        final Type productType = createSimpleProductType();

        final RouteBuilder create = route(name, "/product/$id", "create", voidType(), POST).withBody(productType)
                                    .withParameter(parameter("a", stringType()))
                                    .withParameter(parameter("b", intType()))
                                    .withParameter(parameter("c", booleanType()))
                                    .withParameter(parameter("d", intType(3)));

        final RouteBuilder returning = route(name, "/returning/$id", "create", htmlType(), POST)  //
                                       .withBody(productType).withParameter(parameter("x", stringType()));

        return new HandlerBuilder("", name.getQualification(), name.getName())  //
               .addRoute(nothing).addRoute(home).addRoute(someId).addRoute(create).addRoute(returning);
    }

    static HandlerBuilder builderForPostWithBodyRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.PostWithBodyHandler");

        final Type productType = createSimpleProductType();

        return new HandlerBuilder("", name.getQualification(), name.getName())  //
               .addRoute(route(name, "/product/$id", "create", voidType(), POST).withBody(productType));
    }

    static HandlerBuilder builderForPostWithMultipleBodyRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.PostWithMultipleBodyHandler");

        final Type productType = createSimpleProductType();

        return new HandlerBuilder("", name.getQualification(), name.getName())  //
               .addRoute(route(name, "/products", "create", voidType(), POST).withBody(arrayType(productType)));
    }

    static HandlerBuilder builderForRoutesWithKey()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.RoutesWithKey");

        return new HandlerBuilder("", name.getQualification(), name.getName())                         //
               .addRoute(route(name, "/a/$id:String", "first", voidType(), GET).withKey("RouteWithKey"))  //
               .addRoute(route(name, "/a/$id:String", "second", voidType(), POST));
    }

    static HandlerBuilder builderForServiceRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.ServiceHandler");

        final Type product  = createSimpleProductType();
        final Type products = createMultipleProductType();

        return new HandlerBuilder("", name.getQualification(), name.getName())  //
               .addRoute(route(name, "/", "all", arrayType(product), GET))   //
               .addRoute(route(name, "/", "create", product, POST))          //
               .addRoute(route(name, "/$id", "get", product, GET))           //
               .addRoute(route(name, "/$id", "update", product, POST))       //
               .addRoute(route(name, "/list/$id", "list", products, GET));
    }

    static HandlerBuilder builderForTypeAliasOnRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.TypeAliasOnHandler");

        final Type stringAlias      = TypeDefBuilder.typeDef(name, stringType()).build();
        final Type stringAliasArray = arrayType(stringAlias);

        return new HandlerBuilder("", name.getQualification(), name.getName()).addRoute(
                route(name, "/stringAlias", "stringAlias", stringAlias, POST).withBody(stringAlias))
               .addRoute(route(name, "/stringAliasArray", "stringAliasArray", stringAliasArray, POST).withBody(stringAliasArray));
    }

    static HandlerBuilder builderForTypedRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.TypedHandler");

        return new HandlerBuilder("", name.getQualification(), name.getName())  //
               .addRoute(route(name, "/a/$id:String", "str", htmlType(), GET))  //
               .addRoute(route(name, "/b/$id:Real", "real", htmlType(), GET))  //
               .addRoute(route(name, "/c/$id:Date", "date", htmlType(), GET))  //
               .addRoute(route(name, "/d/$id:DateTime", "time", htmlType(), GET));
    }

    static HandlerBuilder builderForValidPartsIdsRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.ValidPartsIdsRoutingHandler");

        return new HandlerBuilder("", name.getQualification(), name.getName())  //
               .addRoute(route(name, "/a/$id", "someA", htmlType(), GET))    //
               .addRoute(route(name, "/b/$id2", "someB", htmlType(), GET))   //
               .addRoute(route(name, "/c/$id2and3", "someC", htmlType(), GET))  //
               .addRoute(route(name, "/d/$iso2", "someD", htmlType(), GET));
    }

    static HandlerBuilder builderForVoidRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.VoidHandler");

        return new HandlerBuilder("", name.getQualification(), name.getName())  //
               .addRoute(route(name, "/a/$id:String", "str", voidType(), POST));
    }

    private static HandlerBuilder builderForHtmlRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.HtmlRoutingHandler");

        return new HandlerBuilder("", name.getQualification(), name.getName())   //
               .addRoute(route(name, "/home", "home", htmlType(), GET))          //
               .addRoute(route(name, "/some", "some", htmlType(), GET))          //
               .addRoute(route(name, "/some/$id", "someId", htmlType(), GET))    //
               .addRoute(route(name, "/some/$path*", "somePath", htmlType(), GET))  //
               .addRoute(route(name, "/example/$with/multiple/$params", "multiple", htmlType(), GET));
    }

    private static HandlerBuilder builderForSpecialCharactersRouting()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.SpecialCharactersHandler");

        return new HandlerBuilder("", name.getQualification(), name.getName()).addRoute(
            route(name, "/$i/some-path_with.special/characters/$spanning*", "target", htmlType(), GET));
    }

    private static ParameterBuilder parameter(String name, Type type) {
        return new ParameterBuilder(name, type);
    }

    private static RouteBuilder route(@NotNull QName name, @NotNull String path, @NotNull String method, @NotNull Type type, Method httpMethod)
        throws BuilderException
    {
        return new RouteBuilder(name.getFullName()).withPath(path).withClassMethod(method).withType(type).withHttpMethod(httpMethod);
    }

    //~ Static Fields ................................................................................................................................

    private static final String JSON_EXT = ".json";
    private static final String SWAGGER  = "Swagger";
}  // end class HandlerGenerationTest
