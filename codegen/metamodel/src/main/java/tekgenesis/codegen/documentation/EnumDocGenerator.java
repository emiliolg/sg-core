
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

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.field.TypeField;
import tekgenesis.type.EnumType;
import tekgenesis.type.EnumValue;
import tekgenesis.type.Kind;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;

import static tekgenesis.codegen.common.MMCodeGenConstants.FIELDS;
import static tekgenesis.codegen.common.MMCodeGenConstants.OUTPUT_DIR;
import static tekgenesis.codegen.common.MMCodeGenConstants.TITLE;
import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.common.core.Constants.DESCRIPTION;
import static tekgenesis.common.core.Strings.capitalizeFirst;

/**
 * Entity documentation generator.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "unchecked" })
class EnumDocGenerator extends MMDocGenerator {

    //~ Constructors .................................................................................................................................

    EnumDocGenerator(@NotNull File outputDir) {
        super(outputDir);
    }

    //~ Methods ......................................................................................................................................

    @Override public void getFinalHtml(@NotNull Writer writer, @NotNull Reader reader, MetaModel metaModel) {
        if (!(metaModel instanceof EnumType)) return;
        final EnumType            enumType = (EnumType) metaModel;
        final Map<String, Object> map      = new HashMap<>();
        map.put(TITLE, enumType.getName());
        map.put(OUTPUT_DIR, getTargetDir(enumType.getFullName()));
        map.put(DESCRIPTION, enumType.getDocumentation().replaceAll("\n", "<br/>"));
        map.put(FIELDS, enumType.retrieveSimpleFields().map(EnumTypeField::new).toList());
        map.put(ENUM_VALUES, enumType.getValues().map(EnumValueDoc::new).toList());
        renderMustache(writer, reader, map);
    }

    @NotNull @Override String getInnerFolder() {
        return ENUMS;
    }

    @NotNull @Override Class<EnumType> getMMClass() {
        return EnumType.class;
    }

    @NotNull @Override String getMustacheTemplate() {
        return ENUM_TEMPLATE;
    }

    //~ Static Fields ................................................................................................................................

    private static final String ENUM_TEMPLATE = "mm-doc-enum.mustache";
    private static final String ENUMS         = "enums";
    private static final String ENUM_VALUES   = "enumValues";

    //~ Inner Classes ................................................................................................................................

    /**
     * Utility class to encapsulate all Field's necessary documentation to be passed to html.
     */
    private static class EnumTypeField {
        private final String  name;
        private final boolean reference;
        private final String  simpleType;
        private final String  type;

        private EnumTypeField(TypeField field) {
            this(field.getName(), field.getFinalType());
        }

        private EnumTypeField(String name, Type finalType) {
            this.name  = capitalizeFirst(name);
            type       = finalType.toString().replace('.', File.separatorChar);
            simpleType = type.substring(type.lastIndexOf(File.separatorChar) + 1);
            reference  = finalType.getKind() == Kind.REFERENCE || finalType.getKind() == Kind.ENUM;
        }
    }

    /**
     * Utility class to encapsulate all Value's necessary documentation to be passed to html.
     */
    private static class EnumValueDoc {
        private final String documentation;
        private final String label;

        private final String           name;
        private final ImmutableList<?> values;

        private EnumValueDoc(EnumValue value) {
            this(value.getName(), value.getLabel(), value.getValues(), value.getFieldDocumentation());
        }

        private EnumValueDoc(String name, String label, Object[] values, String documentation) {
            this.name          = name;
            this.documentation = documentation;
            this.label         = label;
            this.values        = fromArray(values).map(v -> v != null ? v : "").toList();
        }
    }
}  // end class EnumDocGenerator
