
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

import tekgenesis.common.core.Constants;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.type.MetaModel;

import static tekgenesis.codegen.common.MMCodeGenConstants.FIELDS;
import static tekgenesis.codegen.common.MMCodeGenConstants.OUTPUT_DIR;
import static tekgenesis.codegen.common.MMCodeGenConstants.TITLE;
import static tekgenesis.codegen.documentation.EntityDocGenerator.ENTITY_TEMPLATE;
import static tekgenesis.common.core.Constants.DESCRIPTION;

/**
 * Struct type Documentation Generator.
 */
@SuppressWarnings("unchecked")
public class StructTypeDocGenerator extends MMDocGenerator {

    //~ Constructors .................................................................................................................................

    StructTypeDocGenerator(File outputDir) {
        super(outputDir);
    }

    //~ Methods ......................................................................................................................................

    @Override void getFinalHtml(@NotNull Writer writer, @NotNull Reader reader, MetaModel metaModel) {
        if (!(metaModel instanceof StructType)) return;
        final StructType          type = (StructType) metaModel;
        final Map<String, Object> map  = new HashMap<>();
        map.put(TITLE, type.getName());
        map.put(OUTPUT_DIR, getTargetDir(type.getFullName()));
        map.put(DESCRIPTION, type.getDocumentation().replaceAll("\n", "<br/>"));
        map.put(FIELDS, type.getChildren().map(EntityDocGenerator.FieldDoc::new).toList());
        renderMustache(writer, reader, map);
    }

    @NotNull @Override String getInnerFolder() {
        return Constants.TYPES;
    }

    @NotNull @Override Class<StructType> getMMClass() {
        return StructType.class;
    }

    @NotNull @Override String getMustacheTemplate() {
        return ENTITY_TEMPLATE;
    }
}
