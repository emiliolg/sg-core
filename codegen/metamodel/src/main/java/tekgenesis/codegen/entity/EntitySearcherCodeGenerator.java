
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.entity;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.metadata.entity.DbObject;

import static tekgenesis.codegen.common.MMCodeGenConstants.SEARCHER_SUFFIX;

/**
 * Searchable (user class) code generation.
 */
public class EntitySearcherCodeGenerator extends ClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final String baseFqn;

    private final DbObject dbObject;

    //~ Constructors .................................................................................................................................

    /** Create an DatabaseObjectCodeGenerator. */
    public EntitySearcherCodeGenerator(JavaCodeGenerator cg, @NotNull final DbObject dbObject, String baseFqn) {
        super(cg, dbObject.getName() + SEARCHER_SUFFIX);
        this.dbObject = dbObject;
        this.baseFqn  = baseFqn;
    }

    //~ Methods ......................................................................................................................................

    protected void populate() {
        final String name = dbObject.getName();

        withSuperclass(baseFqn).withComments("User class for index and searching " + name);

        super.populate();
    }
}
