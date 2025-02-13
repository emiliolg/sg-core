
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import java.util.List;

import org.assertj.core.api.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.service.Method;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.entity.*;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.CollidingPathException;
import tekgenesis.metadata.exception.IncompatiblePathException;
import tekgenesis.type.*;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.type.Types.*;

/**
 * Handler builder exceptions test.
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class HandlerBuilderTest {

    //~ Methods ......................................................................................................................................

    @Test public void testHandlerCollidingRoutes()
        throws BuilderException
    {
        final QName          name    = QName.createQName("test.CollidingRoutesHandler");
        final HandlerBuilder builder = new HandlerBuilder("", name.getQualification(), name.getName());
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a/"));

        try {
            builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$b/"));
            Assertions.failBecauseExceptionWasNotThrown(CollidingPathException.class);
        }
        catch (final CollidingPathException e) {
            assertThat(e).hasMessage("Path '/some/$b' collides with '/some/$a'");
        }

        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some"));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a/details"));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a/details/$b"));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/any/$a/details/$b"));

        try {
            builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/any/$var/details/$xyz"));
            Assertions.failBecauseExceptionWasNotThrown(CollidingPathException.class);
        }
        catch (final CollidingPathException e) {
            assertThat(e).hasMessage("Path '/any/$var/details/$xyz' collides with '/any/$a/details/$b'");
        }
    }

    @Test public void testHandlerCollidingRoutesWithIncompatibleTypes()
        throws BuilderException
    {
        final QName          name    = QName.createQName("test.CollidingRoutesHandlerWithIncompatibleTypes");
        final HandlerBuilder builder = new HandlerBuilder("", name.getQualification(), name.getName());
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a:Int/"));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a:Int/other"));

        try {
            builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a:String/any"));
            failBecauseExceptionWasNotThrown(IncompatiblePathException.class);
        }
        catch (final IncompatiblePathException e) {
            assertThat(e).hasMessage(
                "Incompatible route '/some/$a:String/any'. " +
                "Variable 'a' of type String found for already existing 'a' of type Int.");
        }
    }

    @Test public void testHandlerInternal()
        throws BuilderException
    {
        final QName nameInt = QName.createQName("test.HandlerInternal");

        final HandlerBuilder internal = new HandlerBuilder("", nameInt.getQualification(), nameInt.getName());
        internal.asInternal();
        internal.addRoute(new RouteBuilder(nameInt.getFullName()).withPath("/private/some"));
        internal.addRoute(new RouteBuilder(nameInt.getFullName()).withPath("/private/some/$a:Int/"));
        internal.addRoute(new RouteBuilder(nameInt.getFullName()).withPath("/private/some/$a:Int/value"));

        for (final Route route : internal.build().getChildren())
            assertThat(route.isInternal()).isTrue();

        final QName nameExt = QName.createQName("test.HandlerExternal");

        final HandlerBuilder external = new HandlerBuilder("", nameExt.getQualification(), nameExt.getName());
        external.addRoute(new RouteBuilder(nameExt.getFullName()).withPath("/some"));

        for (final Route route : external.build().getChildren())
            assertThat(route.isInternal()).isFalse();
    }

    @Test public void testHandlerKeyOption()
        throws BuilderException
    {
        final QName name = QName.createQName("test.HandlerKeyOption");

        final HandlerBuilder builder = new HandlerBuilder("", name.getQualification(), name.getName());
        builder.label("Handler Label");

        final String myKey = "ThisIsMyKey";

        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/key/$id").withKey(myKey));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$id").withHttpMethod(Method.DELETE));

        final Handler handler = builder.build();
        assertThat(handler.getLabel()).isEqualTo("Handler Label");

        final ImmutableList<Route> routes = handler.getRoutes().toList();
        assertThat(routes.size()).isEqualTo(2);

        assertThat(routes.get(0).getPath()).isEqualTo("/some/key/$id");
        assertThat(routes.get(0).getKey()).isEqualTo(myKey);

        assertThat(routes.get(1).getPath()).isEqualTo("/some/$id");
        assertThat(routes.get(1).getKey()).isEqualTo("SomeId");
    }

    @Test public void testHandlerReferences()
        throws BuilderException
    {
        final QName name = QName.createQName("test.References");

        final Type listType    = createProductListType();
        final Type productType = createProductType();

        final HandlerBuilder builder = new HandlerBuilder("", name.getQualification(), name.getName()).label(name.getName());

        final TypeDef typeAlias = createEnumTypeAlias();

        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/enum/").withType(typeAlias));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/products/").withType(listType));
        builder.addRoute(
            new RouteBuilder(name.getFullName()).withPath("/product/create").withHttpMethod(Method.POST).with(FieldOption.BODY, productType));

        final Handler handler = builder.build();

        final Seq<MetaModel> references = handler.getReferences();
        assertThat(references).hasSize(3);
        assertThat(references).extracting("name").containsOnly("Product", "ProductList", "EnumTypeMetaModel");

        // Extra test for all handlers
        assertThat(handler.getLabel()).isEqualTo(name.getName());
        assertThat(handler.hasModifier(Modifier.REMOTE)).isFalse();
        assertThat(handler.isInner()).isFalse();
        assertThat(handler.getUsages()).isEmpty();
        assertThat(handler.getForm().isEmpty()).isTrue();
    }

    @Test public void testHandlerSecurity()
        throws BuilderException
    {
        final QName name = QName.createQName("test.HandlerSecurity");

        final HandlerBuilder builder = new HandlerBuilder("", name.getQualification(), name.getName());
        builder.withSecureMethod(SecureMethod.APPLICATIONTOKEN);
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some"));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a:Int/"));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a:Int/value"));

        for (final Route route : builder.build().getChildren())
            assertThat(route.getSecureMethod()).isEqualTo(SecureMethod.APPLICATIONTOKEN);
    }

    @Test public void testHandlerSecurityWithRouteOverriding()
        throws BuilderException
    {
        final QName name = QName.createQName("test.HandlerSecurity");

        final HandlerBuilder builder = new HandlerBuilder("", name.getQualification(), name.getName());
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some"));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a:Int/"));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/some/$a:Int/value"));
        builder.addRoute(new RouteBuilder(name.getFullName()).withPath("/").with(FieldOption.UNRESTRICTED));

        for (final Route route : builder.build().getChildren()) {
            if (!"/".equals(route.getPath())) assertThat(route.getSecureMethod()).isEqualTo(SecureMethod.USERNAMEPASSWORD);
            else assertThat(route.getSecureMethod()).isEqualTo(SecureMethod.UNRESTRICTED);
        }
    }

    @Test public void testHandlerSummary()
        throws BuilderException
    {
        final QName name = QName.createQName("test.HandlerSummary");

        final HandlerBuilder internal = new HandlerBuilder("", name.getQualification(), name.getName());
        internal.label("Handler Label");
        internal.addRoute(new RouteBuilder(name.getFullName()).withPath("/some").withSummary("Route Summary"));

        final Handler handler = internal.build();
        assertThat(handler.getLabel()).isEqualTo("Handler Label");
        for (final Route route : handler.getChildren()) {
            assertThat(route.getSummary()).isEqualTo("Route Summary");
            assertThat(route.getLabel()).isEqualTo(route.getSummary());
        }
    }

    @Test public void testRouteCollidingParts() {
        try {
            new RouteBuilder("test.Handler").withPath("/some/$a/$a/$a");
            failBecauseExceptionWasNotThrown(BuilderException.class);
        }
        catch (final BuilderException e) {
            assertThat(e).hasMessageContaining("Dynamic part 'a' already defined on route");
        }
    }

    @Test public void testRouteDynamicTypes()
        throws BuilderException
    {
        final Route builder = new RouteBuilder("test.Handler").withPath(
                "/$a:Int/$b:Real/$c:Decimal(10,2)/$d:Boolean/$e:Date/$f:DateTime/$g:String(15)")
                              .build("");

        final List<Part> parts = builder.getParts().toList();
        assertThat(parts).hasSize(7);
        assertPartType(parts.get(0), Types.intType());
        assertPartType(parts.get(1), Types.realType());
        assertPartType(parts.get(2), Types.decimalType(10, 2));
        assertPartType(parts.get(3), Types.booleanType());
        assertPartType(parts.get(4), Types.dateType());
        assertPartType(parts.get(5), Types.dateTimeType());
        assertPartType(parts.get(6), Types.stringType());
    }

    @Test public void testRouteInvalidDynamicTypes() {
        try {
            new RouteBuilder("test.Handler").withPath("/some/$a:Resource");
            failBecauseExceptionWasNotThrown(BuilderException.class);
        }
        catch (final BuilderException e) {
            assertThat(e).hasMessageContaining("Invalid type 'Resource' specified on dynamic part 'a'");
        }

        try {
            new RouteBuilder("test.Handler").withPath("/some/$a:XYZ");
            failBecauseExceptionWasNotThrown(BuilderException.class);
        }
        catch (final BuilderException e) {
            assertThat(e).hasMessageContaining("Invalid type 'XYZ' specified on dynamic part 'a'");
        }
    }

    @Test public void testRouteParameterCollidingParameterException() {
        try {
            new RouteBuilder("test.Handler").withPath("/some/$a/$b/$c")
                .withParameter(new ParameterBuilder("d", Types.stringType()))
                .withParameter(new ParameterBuilder("e", Types.stringType()))
                .withParameter(new ParameterBuilder("f", Types.stringType()))
                .withParameter(new ParameterBuilder("d", Types.stringType()));
            failBecauseExceptionWasNotThrown(BuilderException.class);
        }
        catch (final BuilderException e) {
            assertThat(e).hasMessageContaining("Parameter 'd' already defined");
        }
    }

    @Test public void testRouteParameterCollidingPathException() {
        try {
            new RouteBuilder("test.Handler").withPath("/some/$a/$b/$c").withParameter(new ParameterBuilder("c", Types.stringType()));
            failBecauseExceptionWasNotThrown(BuilderException.class);
        }
        catch (final BuilderException e) {
            assertThat(e).hasMessageContaining("Parameter 'c' collides with route section");
        }
    }

    @Test public void testRouteWithParameters()
        throws BuilderException
    {
        final RouteBuilder builder = new RouteBuilder("test.Handler").withPath("/some/$a/$b/$c")
                                     .withParameter(new ParameterBuilder("d", Types.stringType()).description("Parameter D"))
                                     .withParameter(new ParameterBuilder("e", Types.intType()).description("Parameter E"));

        final Route           route      = builder.build("letters");
        final List<Parameter> parameters = route.getParameters().toList();
        assertThat(parameters).hasSize(2);
        assertThat(parameters).extracting("label").containsOnly("Parameter D", "Parameter E");
        assertThat(parameters).extracting("name").containsOnly("d", "e");
        assertThat(parameters).extracting("type").extracting("kind").containsOnly(Kind.STRING, Kind.INT);
    }

    private void assertPartType(Part part, Type expected) {
        assertThat(expected.equivalent(part.getType())).overridingErrorMessage("Expected type %s for part %s", expected, part).isTrue();
    }

    private TypeDef createEnumTypeAlias() {
        final EnumBuilder enumBuilder = EnumBuilder.enumType("", "tekgenesis.test", "EnumTypeMetaModel").value("1", "ONE").value("2", "TWO");
        final EnumType    enumType    = enumBuilder.build();

        return TypeDefBuilder.typeDef(QName.createQName("tekgenesis.test", "EnumTypeAlias"), enumType).build();
    }

    private StructType createProductListType()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.ProductList");

        final Type productTypeArray = Types.arrayType(createProductType());

        final StructBuilder builder = StructBuilder.struct(name);
        builder.addField(field("id", stringType(), "List Id"));
        builder.addField(field("products", productTypeArray, "Products"));

        return builder.build();
    }

    private StructType createProductType()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.Product");

        final StructBuilder builder = StructBuilder.struct(name);
        builder.addField(field("productId", stringType(8), "Id"));
        builder.addField(field("price", decimalType(10, 2), "Price"));
        builder.addField(field("created", dateTimeType(), "Created"));

        return builder.build();
    }

    private TypeFieldBuilder field(@NotNull String id, @NotNull Type type, @NotNull String label) {
        return new TypeFieldBuilder(id, type).label(label);
    }
}  // end class HandlerBuilderTest
