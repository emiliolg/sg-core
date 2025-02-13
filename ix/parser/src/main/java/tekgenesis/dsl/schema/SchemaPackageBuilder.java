
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.dsl.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Files;
import tekgenesis.field.FieldOption;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.common.ModelSetBuilder;
import tekgenesis.metadata.entity.AttributeBuilder;
import tekgenesis.metadata.entity.EntityBuilder;
import tekgenesis.metadata.entity.IxEntityBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateIndexException;
import tekgenesis.metadata.exception.NoAttributesIndexException;
import tekgenesis.parser.ASTNode;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Names;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.metadata.entity.EntityBuilder.*;
import static tekgenesis.type.FieldReference.unresolvedFieldRef;

/**
 * User: diego Date: 1/9/12 Time: 3:35 PM
 */
class SchemaPackageBuilder {

    //~ Instance Fields ..............................................................................................................................

    private final ModelSetBuilder aPackage;
    private final String          domain;
    private final String          packageStr;
    private final ModelRepository repository;
    private final SchemaAST       rootNode;
    private final File            schemaFile;
    private String                singlePrimaryKey;
    private final String          source;

    //~ Constructors .................................................................................................................................

    /** Creates a SchemaPackageBuilder class to build a {link @Package} from a .sc ASTNode. */
    public SchemaPackageBuilder(SchemaAST rootNode, ModelRepository repository, String source, File schemaFile) {
        this.repository  = repository;
        aPackage         = new ModelSetBuilder();
        this.rootNode    = rootNode;
        singlePrimaryKey = null;

        domain          = retrieveSchema(rootNode);
        packageStr      = retrievePackage(source);
        this.schemaFile = schemaFile;
        this.source     = source;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Builds a {@link ModelRepository}.
     *
     * @return  a Package
     */
    public ModelRepository build() {
        final Map<String, Tuple<String, String>> includedEntities = loadIncludeFiles();
        final Seq<SchemaAST>                     tables           = rootNode.children(SchemaToken.TABLE);
        try {
            for (final SchemaAST table : tables) {
                final EntityBuilder e = buildEntity(table, includedEntities);
                if (e != null) aPackage.addModel(e);
            }

            aPackage.build(repository);
        }
        catch (final BuilderException e) {
            logger.error(e);
        }

        return repository;
    }

    private void addFields(EntityBuilder entity, ASTNode<SchemaAST, SchemaToken> node) {
        assertType(node, SchemaToken.TABLE_FIELDS);
        final ArrayList<AttributeBuilder> fields = new ArrayList<>();

        for (final SchemaAST field : node)
            try {
                fields.add(buildField(field));
            }
            catch (final BuilderException e) {
                logger.error(e);
            }

        try {
            entity.fields(fields.toArray(new AttributeBuilder[1]));
        }
        catch (final BuilderException e) {
            logger.error(e);
        }
    }

    private void addPrimaryKey(EntityBuilder entity, SchemaAST node) {
        if (singlePrimaryKey != null) {
            entity.primaryKey(singlePrimaryKey);
            entity.describedBy(singlePrimaryKey);  /* Described_by fields are mandatory in Entities */
            singlePrimaryKey = null;
            return;
        }

        final Seq<SchemaAST> indices = node.children(SchemaToken.TABLE_INDICES);
        for (final SchemaAST indexAst : indices) {
            processIndex(entity,
                indexAst,
                SchemaToken.PRIMARY,
                fieldList -> {
                    entity.primaryKey(fieldList.second());
                    entity.describedBy(fieldList.second());  /* Described_by fields are mandatory in Entities */
                });

            processIndex(entity,
                indexAst,
                SchemaToken.UNIQUE,
                fieldList -> {
                    try {
                        entity.withUnique(fieldList.first(), fieldList.second());
                    }
                    catch (DuplicateIndexException | NoAttributesIndexException e) {
                        logger.error(e);
                    }
                });

            processIndex(entity,
                indexAst,
                SchemaToken.INDEX,
                fieldList -> {
                    try {
                        entity.withIndex(fieldList.first(), fieldList.second());
                    }
                    catch (DuplicateIndexException | NoAttributesIndexException e) {
                        logger.error(e);
                    }
                });
        }
    }  // end method addPrimaryKey

    private AttributeBuilder buildAttributeBuilder(String id, SchemaAST fieldNode, SchemaAST typeNode)
        throws BuilderException
    {
        final Iterator<SchemaAST> options = typeNode.iterator();

        final SchemaAST fieldOptions = fieldNode.children(SchemaToken.FIELD_OPTIONS).iterator().next();

        // String               inRef   = null;
        // final Seq<SchemaAST> inTable = fieldOptions.children(SchemaToken.IN_TABLE);
        // for (SchemaAST inTableAST : inTable)
        // inRef = inTableAST.children(SchemaToken.IDENTIFIER).iterator().next().getText();
        //
        // final Seq<SchemaAST>   inValueAST  = fieldOptions.children(SchemaToken.IN);
        // final String           enumName    = entity.getId().toLowerCase() + "_" + id;
        // final EnumBuilder      enumBuilder = fillInValues(inValueAST, enumName);
        //
        // if (inRef != null) field = EntityBuilder.reference(id, entity.getDomain(), inRef.toUpperCase());
        // else if (enumBuilder != null) field = EntityBuilder.reference(id, entity.getDomain(), enumName);

        final AttributeBuilder field    = createPrimitiveType(id, typeNode, options);
        final Seq<SchemaAST>   children = fieldOptions.children(SchemaToken.PRIMARY);

        if (!children.isEmpty()) singlePrimaryKey = id;
        final Seq<SchemaAST> description = fieldOptions.children(SchemaToken.DESCRIPTION);
        for (final SchemaAST desc : description)
            field.description(getDescription(desc));

        final Seq<SchemaAST> notnull = fieldOptions.children(SchemaToken.NOT_NULL);
        if (notnull.isEmpty()) field.optional();

        return field;
    }

    @Nullable private EntityBuilder buildEntity(SchemaAST node, Map<String, Tuple<String, String>> includedEntities) {
        final Iterator<SchemaAST>   iterator          = node.iterator();
        final String                id                = getId(iterator.next());
        final String                name              = id.toUpperCase();
        final Tuple<String, String> stringStringTuple = includedEntities.get(name);

        if (!includedEntities.isEmpty() && stringStringTuple == null) return null;

        final String aliasName = stringStringTuple == null ? Names.tableName(name) : stringStringTuple.first();
        final String tableName = Predefined.notEmpty(aliasName, Names.tableName(name));

        final EntityBuilder entity = new IxEntityBuilder(source, packageStr, domain, name, tableName);
        entity.label(getDescription(iterator.next()));
        addFields(entity, iterator.next());
        addPrimaryKey(entity, node);
        return entity;
    }

    private AttributeBuilder buildField(SchemaAST node)
        throws BuilderException
    {
        assertType(node, SchemaToken.TABLE_FIELD);
        final Iterator<SchemaAST> iterator = node.iterator();
        final String              id       = getId(iterator.next());
        final SchemaAST           typeNode = iterator.next();

        return buildAttributeBuilder(id, node, typeNode);
    }

    private AttributeBuilder createPrimitiveType(String id, SchemaAST typeNode, Iterator<SchemaAST> options) {
        final AttributeBuilder field;
        final SchemaToken      type = typeNode.getType();
        try {
            switch (type) {
            case NUMERIC:
                final int precision = Integer.valueOf(options.next().getText());
                final int decimals  = options.hasNext() ? Integer.parseInt(options.next().getText()) : 0;
                field = decimals == 0 && precision <= 9 ? integer(id, precision) : decimal(id, precision, decimals);
                field.with(FieldOption.SIGNED);  // this force Numerics to be signed.
                break;
            case CHARACTER:
                field = options.hasNext() ? string(id, Integer.parseInt(options.next().getText())) : string(id);
                break;
            case DATE:
                field = date(id);
                break;
            case TIME:
                field = integer(id);
                field.with(FieldOption.TIME_OF_DAY);
                break;
            case BOOLEAN:
                field = bool(id);
                break;
            case FLOAT:
                field = real(id);
                field.with(FieldOption.SIGNED);  // this force Floats to be signed.
                break;
            default:
                throw new IllegalArgumentException("unexpected type: " + typeNode.getText());
            }
        }
        catch (final BuilderException e) {
            throw new RuntimeException(e);
        }
        return field;
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private Map<String, Tuple<String, String>> loadIncludeFiles() {
        final Map<String, Tuple<String, String>> include = new HashMap<>();
        final ImmutableList<String>              strings = Files.readLines(Files.changeExtension(schemaFile, ".include"));
        for (final String includes : strings) {
            final int fieldsDeclaredIdx = includes.indexOf(':');
            final int aliasIdx          = includes.indexOf('=');

            final int size = includes.length();

            final boolean hasAlias          = aliasIdx != -1;
            final boolean hasDeclaredFields = fieldsDeclaredIdx != -1;

            final int tableNameIdx = hasDeclaredFields ? fieldsDeclaredIdx : hasAlias ? aliasIdx : size;

            final String table      = includes.substring(0, tableNameIdx);
            final String tableAlias = hasAlias ? includes.substring(aliasIdx + 1) : "";
            final String fields     = hasDeclaredFields ? includes.substring(fieldsDeclaredIdx + 1, hasAlias ? aliasIdx : size) : "";

            System.out.println(String.format(" IX Entity '%s'  -> '%s' [%s]", table, fields, tableAlias));

            include.put(table, tuple(tableAlias, fields));
        }
        return include;
    }

    private void processIndex(EntityBuilder entity, SchemaAST indexAst, SchemaToken indexType,
                              Consumer<Tuple<String, ArrayList<ModelField>>> consumer) {
        for (final SchemaAST schemaASTs : indexAst.children(indexType)) {
            final Seq<SchemaAST> fields = schemaASTs.children(SchemaToken.INDEX_FIELDS).iterator().next().children(SchemaToken.INDEX_FIELD);

            String indexName = null;
            if (!schemaASTs.children(SchemaToken.IDENTIFIER).isEmpty())
                indexName = schemaASTs.children(SchemaToken.IDENTIFIER).iterator().next().getText();

            final ArrayList<ModelField> fieldList = new ArrayList<>();
            for (final SchemaAST field : fields)
                fieldList.add(unresolvedFieldRef(field.children(SchemaToken.IDENTIFIER).iterator().next().getText()));

            consumer.accept(tuple(notNull(indexName), fieldList));
        }
    }

    private String retrievePackage(@NotNull String src) {
        final String mmPath   = File.separator + "mm" + File.separator;
        final int    mmDirPos = src.indexOf(mmPath);
        final String mmDir    = src.substring(mmDirPos + mmPath.length());

        final String packagePath = mmDir.substring(0, mmDir.length() - new File(src).getName().length() - 1);
        String       packName    = packagePath.replace(File.separatorChar, '.');

        // If the package doesn't end with the domain. We add it.
        // We resolved the schema name from the package in the EntityInstace using this hack
        // because from the StoreHandler, EntityTable and EntitytInstace we don't have access
        // to the domain. So, we figure it out in this way.
        // Also, it solved the problem when there are two sc file in the same dir containing tables
        // with same name bu different schema.
        if (!packName.endsWith(domain)) packName = packName + '.' + domain;

        return packName;
    }

    private String retrieveSchema(SchemaAST r) {
        final ImmutableIterator<SchemaAST> it = r.iterator();
        if (!it.hasNext()) return "";
        final ASTNode<SchemaAST, SchemaToken> node = it.next();
        assertType(node, SchemaToken.SCHEMA);
        final Iterator<SchemaAST> iterator = node.iterator();

        return getId(iterator.next());
    }

    //~ Methods ......................................................................................................................................

    // private EnumBuilder fillInValues(Iterable<SchemaAST> inValueAST, String enumName) {
    // EnumBuilder enumBuilder = null;
    // for (SchemaAST inValue : inValueAST) {
    // final Seq<SchemaAST> values = inValue.children(SchemaToken.IN_VALUE);
    //
    // enumBuilder = EnumBuilder.enumType("", domain, enumName);
    // for (SchemaAST value : values) {
    // final Iterator<SchemaAST> iterator = value.iterator();
    // enumBuilder.value(iterator.next().getText(),
    // iterator.next().getText());
    // }
    // try {
    // aPackage.addModel(enumBuilder);
    // }
    // catch (DuplicateDefinitionException e) {
    // System.err.println(e);
    // }
    // }
    // return enumBuilder;
    // }

    private static void assertType(ASTNode<SchemaAST, SchemaToken> node, SchemaToken type) {
        assert node.hasType(type) : unexpectedType(node, type);
    }

    private static String unexpectedType(ASTNode<SchemaAST, SchemaToken> node, SchemaToken expectedType) {
        return "invalid token type: " + node.getType() + ", expecting: " + expectedType;
    }

    private static String getDescription(ASTNode<SchemaAST, SchemaToken> node) {
        assertType(node, SchemaToken.DESCRIPTION);
        final String descr = node.iterator().next().getText();
        return descr.substring(1, descr.length() - 1);
    }

    private static String getId(ASTNode<SchemaAST, SchemaToken> node) {
        assertType(node, SchemaToken.IDENTIFIER);
        return node.getText();
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(SchemaPackageBuilder.class);
}  // end class SchemaPackageBuilder
