
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph;

import tekgenesis.common.core.QName;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

/**
 * Graph Utils.
 */
public class GraphUtils {

    //~ Constructors .................................................................................................................................

    private GraphUtils() {}

    //~ Methods ......................................................................................................................................

    /** Checks if attribute is bound to some model in the model Repository. */
    public static boolean hasRelation(ModelRepository modelRepository, ModelField attr) {
        return attr.getType().toString() != null && !attr.getType().toString().isEmpty() && getBindedModelForAttribute(modelRepository, attr) != null;
    }

    /** Gets bound model for the attribute. */
    public static MetaModel getBindedModelForAttribute(ModelRepository modelRepository, ModelField attr) {
        return modelRepository.getModel(QName.createQName(attr.getType().toString())).getOrNull();
    }

    /** returns a string with the cardinality of the attribute. */
    public static String getCardinality(Attribute attr) {
        return attr.isMultiple() ? "n" : "1";
    }
    /** returns true if the version of idea is ultimate. */
    public static boolean isUltimate() {
        try {
            Class.forName("com.intellij.openapi.graph.GraphManager");
        }
        catch (final ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
