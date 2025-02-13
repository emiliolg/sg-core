
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

import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;

import static java.lang.String.format;

import static tekgenesis.codegen.common.MMCodeGenConstants.COPY_TO_METHOD_NAME;
import static tekgenesis.codegen.common.MMCodeGenConstants.PERSISTABLE_INSTANCE;
import static tekgenesis.common.core.Strings.deCapitalizeFirst;

/**
 * Class to manage the generation of the EntityBase code.
 */
public class EntityBaseForUpdateCodeGenerator extends EntityBaseCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final String entityName;
    private final String immutableBase;

    //~ Constructors .................................................................................................................................

    /** Create an EntityBaseCodeGenerator. */
    public EntityBaseForUpdateCodeGenerator(EntityBaseCodeGenerator e, JavaCodeGenerator cg, @NotNull final DbObject dbObject, String className) {
        super(cg, dbObject, className, true, e.primaryKeyFields);
        immutableBase = e.getName();
        entityName    = dbObject.getName();
        extractImport(dbObject.getForUpdatePackage() + "." + className);
        e.createCopyTo();
    }

    //~ Methods ......................................................................................................................................

    @Override protected void populate() {
        withSuperclass(dbObject.getFullName());
        withInterfaces(generic(PERSISTABLE_INSTANCE, entityName, getPrimaryKeyType()));
        withComments("Generated base ForUpdate class for entity: " + entityName + ".");

        // Add setters
        dbObject.attributes().filter(a -> !dbObject.isPrimaryKey(a)).forEach(this::generateSetter);

        // Modified method
        method("modified", Boolean.TYPE).override().asPublic().return_("true");

        // create methods
        addCreateMethods();

        // Mutator method
        createMutator();
    }

    @NotNull @Override String createMethodName() {
        return deCapitalizeFirst(className);
    }

    @NotNull @Override String getImmutableBase() {
        return immutableBase;
    }

    private void createMutator() {
        final String argName = deCapitalizeFirst(entityName);
        method(createMethodName(), className).asPublic()
            .asStatic()
            .withComments("Make a " + entityName + " mutable")
            .return_(invoke(argName, COPY_TO_METHOD_NAME, new_(className)))
            .arg(argName, immutableBase);
    }

    private void generateSetter(Attribute attribute) {
        final AttributeGenerator ag = new AttributeGenerator(this, entityName, attribute);
        ag.setClassForField(format("((%s)this)", immutableBase));
        ag.generateSetter();
    }
}  // end class EntityBaseForUpdateCodeGenerator
