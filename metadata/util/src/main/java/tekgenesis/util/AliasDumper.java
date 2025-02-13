
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import tekgenesis.common.IndentedWriter;
import tekgenesis.metadata.entity.SimpleType;
import tekgenesis.metadata.entity.TypeDef;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.repository.ModelRepository;

/**
 * Dump a TypeDef.
 */
class AliasDumper extends ModelDumper {

    //~ Instance Fields ..............................................................................................................................

    private final TypeDef type;

    //~ Constructors .................................................................................................................................

    AliasDumper(TypeDef type, ModelRepository repository, IndentedWriter writer, MMDumper.Preferences preferences) {
        super(type, repository, writer, preferences);
        this.type = type;
    }

    //~ Methods ......................................................................................................................................

    @Override ModelDumper dump() {
        if (type instanceof SimpleType) {
            final SimpleType s = (SimpleType) type;
            beginModel().startList().space().print(MMToken.EQ).space().dumpType(s);
            dumpOptions(s);
            semicolon();
        }
        else super.dump();
        return this;
    }
}
