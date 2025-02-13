
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.IndentedWriter;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.TypeDef;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.ArrayType;

/**
 * A visitor to generate the Database Schema.
 */
class TypeDumper extends ModelDumper {

    //~ Constructors .................................................................................................................................

    TypeDumper(@NotNull TypeDef type, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        super(type, repository, writer, preferences);
    }

    //~ Methods ......................................................................................................................................

    @Override void dumpType(ModelField field) {
        if (field.getType().isArray()) printElement(asString(((ArrayType) field.getType()).getElementType()) + "*");
        else super.dumpType(field);
    }
}
