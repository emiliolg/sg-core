
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.entity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.util.Sha;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.SearchField;
import tekgenesis.type.ArrayType;
import tekgenesis.type.EnumType;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.common.MMCodeGenConstants.SEARCHER_SUFFIX;
import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.collections.Maps.enumMap;
import static tekgenesis.common.collections.Maps.hashMap;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.common.MetadataConstants.DATABASE_SEARCHER;
import static tekgenesis.metadata.common.MetadataConstants.INDEX_SEARCHER;
import static tekgenesis.type.Kind.*;

/**
 * Searchable (base class) code generation.
 */
public class EntitySearcherBaseCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final DbObject dbObject;

    //~ Constructors .................................................................................................................................

    /** Constructor. */
    public EntitySearcherBaseCodeGenerator(JavaCodeGenerator cg, @NotNull final DbObject dbObject) {
        super(cg, dbObject.getName() + "SearcherBase");
        this.dbObject = dbObject;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return getName();
    }

    @Override protected void populate() {
        final String name = dbObject.getFullName();

        withSuperclass(dbObject.isDatabaseSearchable() ? DATABASE_SEARCHER : INDEX_SEARCHER).withComments(
            "Base class for index and searching " + name);

        final Constructor defaultConstructor = constructor().withComments("Default constructor.").asProtected();

        populateConstructor(defaultConstructor);

        if (!dbObject.isDatabaseSearchable()) fieldWithGetter("INDEX_ID", shaFromFields(), "getIndexId");

        final String searcherFqn = name + SEARCHER_SUFFIX;
        field(fromCamelCase(dbObject.getName() + SEARCHER_SUFFIX), searcherFqn, new_(searcherFqn)).asStatic().asFinal().asPublic().notNull();

        super.populate();
    }

    private void addSearchField(Constructor c, SearchField field, String constantName) {
        final String  implementationClassName = field.getField().getFinalType().getImplementationClassName();
        final String  className               = classNameFromType(field.getField().getFinalType());
        final String  methodName              = fieldMethodFromClass(className, implementationClassName);
        final String  fieldName               = quoted(field.getFieldName());
        final String  fieldId                 = quoted(field.getId());
        final boolean isEntity                = methodName.equals(ENTITY_METHOD);
        String        fieldBuilder            = invoke(FIELDS) + "." +
                                                (isEntity || methodName.contains(ENUM_METHOD)
                                                 ? invoke("", methodName, fieldId, fieldName, classOf(implementationClassName))
                                                 : invoke("", methodName, fieldId, fieldName));

        for (final FieldOption option : field.getOptions()) {
            switch (option) {
            case BOOST:
                fieldBuilder = invoke(fieldBuilder, "withBoost", String.valueOf(field.getBoost()));
                break;
            case FILTER_ONLY:
                fieldBuilder = invoke(fieldBuilder, "filterOnly");
                break;
            default:
                throw unreachable("Unsupported search field option.");
            }
        }

        final String fieldType = field.isMultiple()
                                 ? generic(isEntity ? SEARCHABLE_MANY_ENT_FIELD : SEARCHABLE_MANY_FIELD,
                innerImplementationClass(field.getField().getFinalType()))
                                 : className + (isEntity ? "<" + implementationClassName + ">" : "");

        if (dbObject.isDatabaseSearchable()) c.statement(fieldBuilder);
        else {
            final Field f = field(constantName, fieldType).asFinal().asPublic().notNull();

            if (field.isMultiple()) fieldBuilder = invoke(invoke(FIELDS), isEntity ? "manyEntField" : "manyField", fieldBuilder);

            c.assign(f.getName(), fieldBuilder);
        }
    }  // end method addSearchField

    @NotNull private String classNameFromType(Type type) {
        if (type.isDatabaseObject()) return SEARCHABLE_ENTITY_FIELD;

        if (type instanceof EnumType) return generic(SEARCHABLE_ENUM_FIELD, type.getImplementationClassName());
        if (Long.class.equals(type.getImplementationClass())) return SEARCHABLE_LONG_FIELD;
        if (type instanceof ArrayType) return classNameFromType(((ArrayType) type).getElementType());

        final String s = fieldClass.get(type.getKind());
        if (s == null) throw unreachable("Kind is not supported by searchable: " + type.getKind() + ". In dbObject: " + dbObject.getFullName());
        return s;
    }

    @NotNull private String fieldMethodFromClass(String className, String implementationClass) {
        if (className.startsWith("Enum") || className.startsWith(SEARCHABLE_ENUM_FIELD))
            return "<" + extractImport(implementationClass) + ">" + ENUM_METHOD;

        final String s = fieldMethod.get(className);
        if (s == null) throw unreachable();
        return s;
    }

    private void fieldWithGetter(String fieldName, String fieldValue, String getter) {
        field(fieldName, String.class, quoted(fieldValue)).asStatic().asFinal().notNull();

        method(getter, String.class).override().return_(fieldName).notNull().asFinal();
    }

    private String innerImplementationClass(Type t) {
        if (t.isArray()) return ((ArrayType) t).getElementType().getImplementationClassName();
        return t.getImplementationClassName();
    }

    private void populateConstructor(final Constructor c) {
        final List<String> r = new ArrayList<>();
        r.add(classOf(dbObject.getImplementationClassName()));

        for (final SearchField field : dbObject.searchByFields()) {
            final String fieldId      = field.getId();
            final String constantName = fromCamelCase(fieldId);

            addSearchField(c, field, constantName);
        }

        c.invokeSuper(r);
    }

    @NotNull private String shaFromFields() {
        final String uniqueMapping = dbObject.getFullName() + "[" + dbObject.searchByFields()
                                     .map(searchField ->
                        searchField.getId() + ":" + classNameFromType(searchField.getField().getFinalType()) + ":" + searchField.optionsString())
                                     .mkString(",") + "]";

        final Sha shaGenerator = new Sha();
        shaGenerator.process(uniqueMapping.getBytes());
        final String sha = shaGenerator.getDigestAsString();
        return sha.length() > MAX_SHA_LENGTH ? sha.substring(0, MAX_SHA_LENGTH) : sha;
    }

    //~ Static Fields ................................................................................................................................

    private static final int MAX_SHA_LENGTH = 30;

    private static final EnumMap<Kind, String> fieldClass = enumMap(tuple(INT, SEARCHABLE_INT_FIELD),
            tuple(REAL, SEARCHABLE_REAL_FIELD),
            tuple(DECIMAL, SEARCHABLE_DECIMAL_FIELD),
            tuple(STRING, SEARCHABLE_STRING_FIELD),
            tuple(BOOLEAN, SEARCHABLE_BOOLEAN_FIELD),
            tuple(DATE, SEARCHABLE_DATE_FIELD),
            tuple(ENUM, SEARCHABLE_ENUM_FIELD),
            tuple(DATE_TIME, SEARCHABLE_DATE_TIME_FIELD));

    private static final Map<String, String> fieldMethod = hashMap(tuple(SEARCHABLE_INT_FIELD, INT_METHOD),
            tuple(SEARCHABLE_REAL_FIELD, REAL_METHOD),
            tuple(SEARCHABLE_DECIMAL_FIELD, DECIMAL_METHOD),
            tuple(SEARCHABLE_STRING_FIELD, STR_METHOD),
            tuple(SEARCHABLE_BOOLEAN_FIELD, BOOL_METHOD),
            tuple(SEARCHABLE_DATE_FIELD, DATE_METHOD),
            tuple(SEARCHABLE_DATE_TIME_FIELD, "dateTimeField"),
            tuple(SEARCHABLE_LONG_FIELD, LONG_METHOD),
            tuple(SEARCHABLE_ENTITY_FIELD, ENTITY_METHOD));
}  // end class EntitySearcherBaseCodeGenerator
