
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.documentation;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Kind;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;

import static tekgenesis.codegen.common.MMCodeGenConstants.FIELDS;
import static tekgenesis.codegen.common.MMCodeGenConstants.OUTPUT_DIR;
import static tekgenesis.codegen.common.MMCodeGenConstants.TITLE;
import static tekgenesis.common.core.Constants.DESCRIPTION;

/**
 * Entity documentation generator.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "unchecked" })
class EntityDocGenerator extends MMDocGenerator {

    //~ Constructors .................................................................................................................................

    EntityDocGenerator(@NotNull File outputDir) {
        super(outputDir);
    }

    //~ Methods ......................................................................................................................................

    @Override public void getFinalHtml(@NotNull Writer writer, @NotNull Reader reader, MetaModel metaModel) {
        if (!(metaModel instanceof Entity)) return;
        final Entity              entity = (Entity) metaModel;
        final Map<String, Object> map    = new HashMap<>();
        map.put(TITLE, entity.getName());
        map.put(OUTPUT_DIR, getTargetDir(entity.getFullName()));
        map.put(DESCRIPTION, entity.getDocumentation().replaceAll("\n", "<br/>"));
        map.put(FIELDS, entity.allAttributes().map(FieldDoc::new).toList());
        renderMustache(writer, reader, map);
    }

    @NotNull @Override String getInnerFolder() {
        return ENTITIES;
    }

    @NotNull @Override Class<Entity> getMMClass() {
        return Entity.class;
    }

    @NotNull @Override String getMustacheTemplate() {
        return ENTITY_TEMPLATE;
    }

    //~ Static Fields ................................................................................................................................

    static final String         ENTITY_TEMPLATE = "mm-doc-entity.mustache";
    private static final String ENTITIES        = "entities";

    //~ Inner Classes ................................................................................................................................

    /**
     * Utility class to encapsulate all Field's necessary documentation to be passed to html.
     */
    static class FieldDoc {
        private final String description;

        private final String  id;
        private final boolean reference;
        private final String  simpleType;
        private final String  type;

        FieldDoc(TypeField attribute) {
            this(attribute.getSimpleName(), attribute.getFinalType(), attribute.getFieldDocumentation());
        }

        private FieldDoc(String id, Type finalType, String description) {
            this.id = id;

            final boolean isArray = finalType.isArray();

            final Type realFinalType = isArray ? ((ArrayType) finalType).getElementType() : finalType;
            reference = realFinalType.getKind() == Kind.REFERENCE || realFinalType.getKind() == Kind.ENUM || realFinalType.getKind() == Kind.TYPE;

            type = realFinalType.toString().replace('.', File.separatorChar);

            final String simple = type.substring(type.lastIndexOf(File.separatorChar) + 1);
            simpleType = isArray ? simple + "*" : simple;

            this.description = description;
        }
    }
}  // end class EntityDocGenerator
