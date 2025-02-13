
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import java.io.Serializable;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.util.Diff;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.exception.*;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.*;
import tekgenesis.type.exception.ReverseReferenceException;
import tekgenesis.type.exception.UnresolvedTypeReferenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.metadata.common.ModelSetBuilder.buildAll;
import static tekgenesis.metadata.entity.EntityBuilder.*;
import static tekgenesis.metadata.entity.EnumBuilder.enumType;
import static tekgenesis.metadata.entity.TypeDefBuilder.*;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class EntityBuilderTest {

    //~ Methods ......................................................................................................................................

    @Test public void defaultFields()
        throws BuilderException
    {
        final ModelRepository module = buildAll(
                entity("Package", "Entity1").fields(integer("attr1"), string("attr2")).auditable().deprecable().optimistic(),
                entity("Package", "Entity2").fields(integer("k").serial("SEQ").column("KEY"),
                                                integer("attr1"),
                                                string("attr2"),
                                                dateTime("updateTime", 3).column("UPD"),
                                                dateTime("creationTime", 3).column("CTIME"))
                                            .auditable()
                                            .deprecable());

        final Entity e1 = module.getModel("Package.Entity1", Entity.class).get();
        assertThat(e1.getAttribute("id").get().getSequenceName()).isEqualTo("ENTITY1_SEQ");
        final Seq<Attribute> a1 = e1.attributes();
        assertThat(a1).extracting("name")
            .containsExactly("id",
                "attr1",
                "attr2",
                "updateTime",
                "instanceVersion",
                "deprecationTime",
                "deprecationUser",
                "creationTime",
                "creationUser",
                "updateUser");
        assertThat(a1).extracting("synthesized").containsExactly(true, false, false, true, true, true, true, true, true, true);
        assertThat(a1).extracting("columnName")
            .containsExactly("ID",
                "ATTR1",
                "ATTR2",
                "UPDATE_TIME",
                "INSTANCE_VERSION",
                "DEPRECATION_TIME",
                "DEPRECATION_USER",
                "CREATION_TIME",
                "CREATION_USER",
                "UPDATE_USER");

        final Entity e2 = module.getModel("Package.Entity2", Entity.class).get();
        assertThat(e2.getAttribute("k").get().getSequenceName()).isEqualTo("SEQ");
        final Seq<Attribute> a2 = e2.attributes();
        assertThat(a2).extracting("name")
            .containsExactly("k", "attr1", "attr2", "updateTime", "creationTime", "deprecationTime", "deprecationUser", "creationUser", "updateUser");
        assertThat(a2).extracting("synthesized").containsExactly(false, false, false, false, false, true, true, true, true);
        assertThat(a2).extracting("columnName")
            .containsExactly("KEY", "ATTR1", "ATTR2", "UPD", "CTIME", "DEPRECATION_TIME", "DEPRECATION_USER", "CREATION_USER", "UPDATE_USER");
    }  // end method defaultFields

    @Test public void defaultPrimaryKey()
        throws BuilderException
    {
        final ModelRepository module = buildAll(entity("Package", "Entity1").fields(integer("attr1"), string("attr2")),
                entity("Package", "Entity2").fields(integer("id").serial(), string("attr2")),
                entity("Package", "Entity3").fields(integer("elMacho").serial(), string("attr2")));

        final Attribute idField = module.getModel("Package.Entity1", Entity.class).get().getPrimaryKey().getFirst().get();

        assertThat(idField.getName()).isEqualTo("id");
        assertThat(idField.getType()).isEqualTo(Types.intType());
        assertThat(idField.isSerial()).isTrue();
        assertThat(idField.isSynthesized()).isTrue();

        final Attribute idField2 = module.getModel("Package.Entity2", Entity.class).get().getPrimaryKey().getFirst().get();

        assertThat(idField2.getName()).isEqualTo("id");
        assertThat(idField2.getType()).isEqualTo(Types.intType());
        assertThat(idField2.isSerial()).isTrue();
        assertThat(idField2.isSynthesized()).isFalse();

        final Entity    e3       = module.getModel("Package.Entity3", Entity.class).get();
        final Attribute idField3 = e3.getPrimaryKey().getFirst().get();

        assertThat(idField3.getName()).isEqualTo("elMacho");
        assertThat(idField3.getType()).isEqualTo(Types.intType());
        assertThat(idField3.isSerial()).isTrue();
        assertThat(idField3.isSynthesized()).isFalse();
    }

    @Test public void documentedEntity()
        throws BuilderException
    {
        final String          entityDoc = "Entity comments are \n<b>awesome</b>";
        final String          attr1Doc  = "attr1 is an integer and has a comment";
        final String          attr2Doc  = "attr2 is a string and also has a comment";
        final ModelRepository repo      = buildAll(
                entity("Package", "Entity1").withDocumentation(entityDoc)
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1").withFieldDocumentation(attr1Doc),
                                                string("attr2").withFieldDocumentation(attr2Doc)));
        final Option<Entity>  entityOp  = repo.getModel("Package.Entity1", Entity.class);
        assertThat(entityOp.isPresent()).isTrue();
        assertThat(entityOp.get().describes().getFirst().get().getName()).isEqualTo("attr1");
        assertThat(entityOp.get().getDocumentation()).isEqualTo(entityDoc);
        assertThat(entityOp.get().getAttribute("attr1").isPresent()).isTrue();
        assertThat(entityOp.get().getAttribute("attr1").get().getFieldDocumentation()).isEqualTo(attr1Doc);
        assertThat(entityOp.get().getAttribute("attr2").isPresent()).isTrue();
        assertThat(entityOp.get().getAttribute("attr2").get().getFieldDocumentation()).isEqualTo(attr2Doc);
    }
    @Test public void duplicateAttributeFailure()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1").label("description").primaryKey("attr1").fields(integer("attr1"), string("attr1")));
            failBecauseExceptionWasNotThrown(DuplicateFieldException.class);
        }
        catch (final DuplicateFieldException ignored) {}
    }

    @Test public void duplicateEntity()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1").label("description").primaryKey("attr1").fields(integer("attr1"), string("attr2")),
                entity("Package", "Entity1").describedBy("attr1").label("description").primaryKey("attr1").fields(integer("attr1"), string("attr2")));
            failBecauseExceptionWasNotThrown(DuplicateDefinitionException.class);
        }
        catch (final DuplicateDefinitionException ignored) {}
    }

    @Test public void duplicateEnumFailure()
        throws BuilderException
    {
        try {
            buildAll(enumType("", "Package", "MyEnum"), enumType("", "Package", "MyEnum"));
            failBecauseExceptionWasNotThrown(DuplicateDefinitionException.class);
        }
        catch (final DuplicateDefinitionException ignored) {}
    }

    @Test public void duplicateUniqueKeyFailure()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .withUnique("byAttr2", "attr2")
                                            .withIndex("byAttr2", "attr2")
                                            .fields(integer("attr1"), string("attr2")));
            failBecauseExceptionWasNotThrown(DuplicateIndexException.class);
        }
        catch (final DuplicateIndexException ignored) {}
    }

    @Test public void enumAttribute()
        throws BuilderException
    {
        final ModelRepository module = buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1"),
                                                string("attr2", 20),                      //
                                                reference("attr3", "Package", "MyEnum"),  //
                                                reference("set", "Package", "MyEnum").multiple()),
                //
                enumType("", "Package", "MyEnum").fields(new TypeFieldBuilder("id", Types.intType()))
                                                 .withPrimaryKey("id")
                                                 .withDefault("2")
                                                 .value("1", "Uno")
                                                 .value("2", "Dos"));

        verifyModule(module, "Package", module5Out);

        final EnumType enumType1 = module.getModel("Package.MyEnum", EnumType.class).get();

        assertThat(enumType1.getValues().toString()).isEqualTo("(1:Uno, 2:Dos)");
        assertThat(enumType1.getKind()).isEqualTo(Kind.ENUM);
        assertThat(enumType1.getPkType()).isEqualTo(Types.intType());
        assertThat(enumType1.getPkFieldName()).isEqualTo("id");
        assertThat(enumType1.getDefaultValue()).isEqualTo("2");
    }

    @Ignore public void enumAttributeBadLength()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1"),
                                                string("attr2", 5),                       //
                                                reference("attr3", "Package", "MyEnum"),  //
                                                reference("set", "Package", "MyEnum").multiple()),
                //
                enumType("", "Package", "MyEnum").fields(new TypeFieldBuilder("id", Types.intType()),
                                                     new TypeFieldBuilder("desc", Types.stringType(4)))
                                                 .withPrimaryKey("id")
                                                 .withDefault("2")
                                                 .value("1", "Uno", new Serializable[] { 1, "MuuuuuyLargo" })
                                                 .value("2", "Dos"));
            failBecauseExceptionWasNotThrown(InvalidValueException.class);
        }
        catch (final InvalidValueException ignore) {}
    }

    @Test public void enumDocumentation()
        throws BuilderException
    {
        final String          enumDoc = "My enum documentation is the best";
        final String          oneDoc  = "Uno documentacion";
        final String          twoDoc  = "Dos documentacion";
        final ModelRepository module  = buildAll(
                enumType("", "Package", "MyEnum").fields(new TypeFieldBuilder("id", Types.intType())).withPrimaryKey("id").withDocumentation(
                    enumDoc).withDefault("2").value("1", "Uno", oneDoc).value("2", "Dos", twoDoc).value("3", "Tres"));

        final EnumType enumType = module.getModel("Package.MyEnum", EnumType.class).get();

        assertThat(enumType.getDocumentation()).isEqualTo(enumDoc);

        final EnumValue oneVal = enumType.getValue("1");
        assertThat(oneVal).isNotNull();
        assertThat(oneVal.getFieldDocumentation()).isEqualTo(oneDoc);

        final EnumValue twoVal = enumType.getValue("2");
        assertThat(twoVal).isNotNull();
        assertThat(twoVal.getFieldDocumentation()).isEqualTo(twoDoc);

        final EnumValue threeVal = enumType.getValue("3");
        assertThat(threeVal).isNotNull();
        assertThat(threeVal.getFieldDocumentation()).isEqualTo("");
    }

    @Test public void invalidFieldNames()
        throws BuilderException
    {
        try {
            buildAll(entity("Package", "Entity1").fields(integer("id"), string("attr1")));
            failBecauseExceptionWasNotThrown(InvalidFieldNameException.class);
        }
        catch (final InvalidFieldNameException e) {
            assertThat(e.getMessage()).isEqualTo("Invalid field name  'id'");
        }
        try {
            buildAll(entity("Package", "Entity1").fields(string("attr1"), string("updateTime")));

            failBecauseExceptionWasNotThrown(InvalidFieldNameException.class);
        }
        catch (final InvalidFieldNameException e) {
            assertThat(e.getMessage()).isEqualTo("Invalid field name  'updateTime'");
        }
    }

    @Test public void noAttributeUniqueKeyFailure()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .withUnique("byAttr2")
                                            .fields(integer("attr1"), string("attr2")));
            failBecauseExceptionWasNotThrown(NoAttributesIndexException.class);
        }
        catch (final NoAttributesIndexException ignored) {}
    }

    @Test public void noDescribeByField()
        throws BuilderException
    {
        final ModelRepository repo     = buildAll(
                entity("Package", "Entity1").label("description").primaryKey("attr1").fields(integer("attr1"), string("attr2")));
        final Option<Entity>  entityOp = repo.getModel("Package.Entity1", Entity.class);
        assertThat(entityOp.isPresent()).isTrue();
        assertThat(entityOp.get().describes().getFirst().get().getName()).isEqualTo("attr1");
    }

    @Test public void optionalAttribute()
        throws BuilderException
    {
        final ModelRepository module = buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1").description("attr 1"), string("attr2").description("attr 2").optional()));

        verifyModule(module, "Package", optionalOut);
    }

    @Test public void optionalInPrimaryKeyFailure()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1").optional(), string("attr2")));
            failBecauseExceptionWasNotThrown(NullableInPrimaryKeyException.class);
        }
        catch (final NullableInPrimaryKeyException ignored) {}
    }

    @Test public void optionalInPrimaryKeyFailureCase2()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1").optional(), string("attr2")));
            failBecauseExceptionWasNotThrown(NullableInPrimaryKeyException.class);
        }
        catch (final NullableInPrimaryKeyException ignored) {}
    }

    @Test public void referenceAttribute()
        throws BuilderException
    {
        final ModelRepository module = buildAll(
                entity("Package", "Entity1").label("description")
                                            .primaryKey("attr1")
                                            .describedBy("attr1")
                                            .fields(integer("attr1"), string("attr2", 20), reference("attr3", "Package", "Entity2")),
                entity("Package", "Entity2").label("description").primaryKey("attr1").describedBy("attr1").fields(integer("attr1"), string("attr2")));

        verifyModule(module, "Package", referenceOut);

        final Entity entity2 = module.getModel("Package.Entity2", Entity.class).get();
        final Entity entity1 = module.getModel("Package.Entity1", Entity.class).get();

        final Type refType = entity1.getAttribute("attr3").get().getType();
        assertThat(refType).isEqualTo(entity2);
        assertThat(refType.hashCode()).isEqualTo(entity2.hashCode());
        assertThat(refType.getKind()).isEqualTo(Kind.REFERENCE);
    }

    @Test public void referenceAttributeThroughType()
        throws BuilderException
    {
        final ModelRepository module1 = buildAll(entity("Package1", "Entity1").primaryKey("attr1").fields(integer("attr1"), string("attr2")));

        final Entity entity1 = module1.getModel("Package1.Entity1", Entity.class).get();

        final ModelRepository module2 = buildAll(typeDef(createQName("Package2", "E1"), entity1),
                entity("Package2", "Entity2").primaryKey("attr2").fields(integer("attr2"), string("attr3", 20), reference("attr4", "Package2.E1")));

        final Entity entity2 = module2.getModel("Package2.Entity2", Entity.class).get();

        final Attribute attr = entity2.getAttribute("attr4").get();

        final Type refType = attr.getFinalType();
        assertThat(refType).isEqualTo(entity1);
        assertThat(refType.hashCode()).isEqualTo(entity1.hashCode());
        assertThat(refType.getKind()).isEqualTo(Kind.REFERENCE);

        assertThat(attr.retrieveSimpleFields().toString()).isEqualTo("(attr4Attr1)");
    }

    @Test public void reverseReference()
        throws BuilderException
    {
        buildAll(
            entity("Package", "Entity1").describedBy("attr1")
                                        .label("description")
                                        .primaryKey("attr1")
                                        .fields(integer("attr1"), string("attr2"), reference("attr3", "Package", "Entity2")),
            entity("Package", "Entity2").describedBy("attr1")
                                        .label("description")
                                        .primaryKey("attr1")
                                        .fields(integer("attr1"), string("attr2"), reference("attr3", "Package", "Entity1").multiple()));
    }

    @Test public void reverseReferenceFailure()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1").label("description").primaryKey("attr1").fields(integer("attr1"), string("attr2")),
                entity("Package", "Entity2").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1"), string("attr2"), reference("attr3", "Package", "Entity1").multiple()));
            failBecauseExceptionWasNotThrown(ReverseReferenceException.class);
        }
        catch (final ReverseReferenceException ignored) {}
    }

    @Test public void reverseReferenceMultipleFailure()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1"),
                                                string("attr2"),
                                                reference("attr3", "Package", "Entity2"),
                                                reference("attr4", "Package", "Entity2")),
                entity("Package", "Entity2").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1"), string("attr2"), reference("attr3", "Package", "Entity1").multiple()));
            failBecauseExceptionWasNotThrown(ReverseReferenceException.class);
        }
        catch (final ReverseReferenceException ignored) {}
    }

    @Test public void reverseReferenceUsing()
        throws BuilderException
    {
        buildAll(
            entity("Package", "Entity1").describedBy("attr1")
                                        .label("description")
                                        .primaryKey("attr1")
                                        .fields(integer("attr1"),
                                            string("attr2"),
                                            reference("attr3", "Package", "Entity2"),
                                            reference("attr4", "Package", "Entity2")),
            entity("Package", "Entity2").describedBy("attr1")
                                        .label("description")
                                        .primaryKey("attr1")
                                        .fields(integer("attr1"),
                                            string("attr2"),
                                            reference("attr3", "Package", "Entity1").withReverseReference("attr3")));
    }
    @Test public void simplePackage()
        throws BuilderException
    {
        final ModelRepository module = buildAll(
                entity("Package", "Entity1").label("description")
                                            .describedBy("attr1")
                                            .primaryKey("attr1", "attr2")
                                            .fields(integer("attr1"),
                                                string("attr2"),
                                                string("attr3", 10),
                                                real("attr4"),
                                                date("attr5"),
                                                bool("attr6"),
                                                decimal("attr7", 10),
                                                decimal("attr8", 10, 2),
                                                integer("attr9", 6),
                                                integer("attr10", 10)));

        final String                   out   = printPackage(module, "Package");
        final List<Diff.Delta<String>> delta = Diff.caseSensitive().diff(out, simpleModuleOut);
        assertThat(Diff.makeString(delta)).isEqualTo("");

        final Option<Entity> entityOp = module.getModel("Package.Entity1", Entity.class);

        assertThat(entityOp.isPresent()).isTrue();

        final Option<Attribute> attribute = entityOp.get().getAttribute("attr1");

        assertThat(attribute.isPresent()).isTrue();

        assertThat(attribute.get().getDbObject()).isEqualTo(entityOp.get());
    }

    @Test public void unExistentEntityFailure()
        throws BuilderException
    {
        try {
            buildAll(
                entity("BadModule", "Entity1").describedBy("attr1")
                                              .label("description")
                                              .primaryKey("attr1")
                                              .fields(integer("attr1"), string("attr2"), reference("attr3", "Package", "Entity2")));
            failBecauseExceptionWasNotThrown(UnresolvedTypeReferenceException.class);
        }
        catch (final UnresolvedTypeReferenceException ignored) {}
    }

    @Test public void uniqueKey()
        throws BuilderException
    {
        final ModelRepository module = buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .withUnique("byAttr2", "attr2")
                                            .withIndex("byAttr3", "attr3")
                                            .fields(integer("attr1").description("attr 1"),
                                                string("attr2").description("attr 2"),
                                                string("attr3").description("attr 3")));

        verifyModule(module, "Package", uniqueOut);
    }

    @Test public void unresolvedReferenceFailure()
        throws BuilderException
    {
        try {
            buildAll(
                entity("Package", "Entity1").describedBy("attr1")
                                            .label("description")
                                            .primaryKey("attr1")
                                            .fields(integer("attr1"), string("attr2"), reference("attr3", "Package", "xx")),

                entity("Package", "Entity2").describedBy("attr1").label("description").primaryKey("attr1").fields(integer("attr1"), string("attr2")));
            failBecauseExceptionWasNotThrown(UnresolvedTypeReferenceException.class);
        }
        catch (final UnresolvedTypeReferenceException ignored) {}
    }

    private String printEntity(Entity e) {
        final StrBuilder builder = new StrBuilder();

        builder.append("Entity ").append(e.getName()).append(" \"").append(e.getLabel()).append("\"\n");
        for (final Attribute a : e.attributes()) {
            if (!"instanceVersion".equals(a.getName())) {
                builder.append("\t\t").append(a.getName()).append(": ").append(a.getType());
                if (a.isMultiple()) builder.append("*");
                if (!a.isRequired()) builder.append(" optional");
                builder.append("\n");
            }
        }

        builder.append("PRIMARY KEY (");
        for (final Attribute a : e.getPrimaryKey())
            builder.appendElement(a.getName());
        builder.append(")\n");

        builder.append("DESCRIBED BY (");
        builder.startCollection();

        for (final ModelField a : e.describes())
            builder.appendElement(a.getName());
        builder.append(")\n");

        for (final String indexName : e.getUniqueIndexNames()) {
            builder.append("UNIQUE ");
            builder.append(indexName);
            builder.append("(");
            builder.append(e.getUniqueIndexByName(indexName));
            builder.append(")\n");
        }

        for (final String indexName : e.getIndexNames()) {
            builder.append("INDEX ");
            builder.append(indexName);
            builder.append("(");
            builder.append(e.getIndexByName(indexName));
            builder.append(")\n");
        }

        return builder.toString();
    }  // end method printEntity

    private String printEnum(EnumType e) {
        final StringBuilder builder = new StringBuilder();

        builder.append("Enum ").append(e.getName()).append("\n");

        for (final EnumValue t : e.getValues())
            builder.append("\t\t").append(t.getName()).append(":").append(t.getLabel()).append("\n");

        return builder.toString();
    }

    private String printPackage(ModelRepository repository, String domain) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Package ").append(domain).append("\n");
        for (final MetaModel e : repository.getModels(domain)) {
            final String s;
            switch (e.getMetaModelKind()) {
            case ENTITY:
                s = printEntity((Entity) e);
                break;
            case ENUM:
                s = printEnum((EnumType) e);
                break;
            default:
                s = "";
            }
            builder.append("\t").append(s).append("\n");
        }
        return builder.toString();
    }

    private void verifyModule(ModelRepository repository, String domain, String out) {
        final List<Diff.Delta<String>> deltas = Diff.caseSensitive().diff(printPackage(repository, domain), out);
        assertThat(Diff.makeString(deltas)).isEmpty();
    }

    //~ Static Fields ................................................................................................................................

    private static final String simpleModuleOut = "Package Package\n" +
                                                  "\tEntity Entity1 \"description\"\n" +
                                                  "\t\tattr1: Int\n" +
                                                  "\t\tattr2: String\n" +
                                                  "\t\tattr3: String(10)\n" +
                                                  "\t\tattr4: Real\n" +
                                                  "\t\tattr5: Date\n" +
                                                  "\t\tattr6: Boolean\n" +
                                                  "\t\tattr7: Decimal(10)\n" +
                                                  "\t\tattr8: Decimal(10, 2)\n" +
                                                  "\t\tattr9: Int(6)\n" +
                                                  "\t\tattr10: Int(10)\n" +
                                                  "\t\tupdateTime: DateTime(3)\n" +
                                                  "PRIMARY KEY (attr1,attr2)\n" +
                                                  "DESCRIBED BY (attr1)\n" +
                                                  "\n";

    private static final String optionalOut = "Package Package\n" +
                                              "\tEntity Entity1 \"description\"\n" +
                                              "\t\tattr1: Int\n" +
                                              "\t\tattr2: String optional\n" +
                                              "\t\tupdateTime: DateTime(3)\n" +
                                              "PRIMARY KEY (attr1)\n" +
                                              "DESCRIBED BY (attr1)\n" +
                                              "\n";

    private static final String uniqueOut = "Package Package\n" +
                                            "\tEntity Entity1 \"description\"\n" +
                                            "\t\tattr1: Int\n" +
                                            "\t\tattr2: String\n" +
                                            "\t\tattr3: String\n" +
                                            "\t\tupdateTime: DateTime(3)\n" +
                                            "PRIMARY KEY (attr1)\n" +
                                            "DESCRIBED BY (attr1)\n" +
                                            "UNIQUE byAttr2(attr2)\n" +
                                            "INDEX byAttr3(attr3)\n" +
                                            "\n";

    private static final String referenceOut = "Package Package\n" +
                                               "\tEntity Entity1 \"description\"\n" +
                                               "\t\tattr1: Int\n" +
                                               "\t\tattr2: String(20)\n" +
                                               "\t\tattr3: Package.Entity2\n" +
                                               "\t\tupdateTime: DateTime(3)\n" +
                                               "PRIMARY KEY (attr1)\n" +
                                               "DESCRIBED BY (attr1)\n" +
                                               "\n" +
                                               "\tEntity Entity2 \"description\"\n" +
                                               "\t\tattr1: Int\n" +
                                               "\t\tattr2: String\n" +
                                               "\t\tupdateTime: DateTime(3)\n" +
                                               "PRIMARY KEY (attr1)\n" +
                                               "DESCRIBED BY (attr1)\n" +
                                               "\n" + "";

    private static final String module5Out = "Package Package\n" +
                                             "\tEntity Entity1 \"description\"\n" +
                                             "\t\tattr1: Int\n" +
                                             "\t\tattr2: String(20)\n" +
                                             "\t\tattr3: Package.MyEnum\n" +
                                             "\t\tset: Package.MyEnum*\n" +
                                             "\t\tupdateTime: DateTime(3)\n" +
                                             "PRIMARY KEY (attr1)\n" +
                                             "DESCRIBED BY (attr1)\n" +
                                             "\n" +
                                             "\tEnum MyEnum\n" +
                                             "\t\t1:Uno\n\t\t2:Dos\n\n";
}  // end class EntityBuilderTest
