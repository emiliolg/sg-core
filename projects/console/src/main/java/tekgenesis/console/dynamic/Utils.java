
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console.dynamic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Strings;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * Utilities for Dynamic Forms.
 */
public final class Utils {

    //~ Constructors .................................................................................................................................

    private Utils() {}

    //~ Methods ......................................................................................................................................

    /**
     * Create Entity Reference Field Name.
     *
     * @param   type        Type
     * @param   columnName  column name
     *
     * @return  Entity reference name
     */
    public static String getEntityReferenceFieldName(@NotNull Type type, @NotNull String columnName) {
        final Entity en = (Entity) type;
        return columnName + "_" + en.getPrimaryKey().map(TypeField::getColumnName).mkString("_");
    }

    /**
     * get Label.
     *
     * @param   attribute  Attribute
     *
     * @return  Label
     */
    public static String getLabel(@NotNull Attribute attribute) {
        return isEmpty(attribute.getLabel()) ? Strings.toWords(attribute.getColumnName()) : attribute.getLabel();
    }
}
