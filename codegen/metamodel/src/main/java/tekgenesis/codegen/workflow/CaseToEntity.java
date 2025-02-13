
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.workflow;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.common.ModelLinkerImpl;
import tekgenesis.metadata.entity.AttributeBuilder;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.EntityBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.workflow.Case;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.ModelLinker;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.core.Constants.DESCRIPTION;
import static tekgenesis.common.core.Constants.ENTITY_STRING;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.expr.ExpressionFactory.integer;
import static tekgenesis.md.MdConstants.UPDATE_TIME;

/**
 * Case to Entity class. Generates virtual Case entities.
 */
public class CaseToEntity {

    //~ Constructors .................................................................................................................................

    private CaseToEntity() {}

    //~ Methods ......................................................................................................................................

    /** Generates virtual Case entities. */
    public static Tuple<Entity, Entity> createCaseEntities(@NotNull final Case c, @NotNull DbObject bound) {
        try {
            final EntityBuilder builder = EntityBuilder.entity(c.getSourceName(), c.getDomain(), c.getName(), "", c.getSchema());

            builder.addField(EntityBuilder.reference(ENTITY_STRING, bound));
            builder.addField(EntityBuilder.dateTime(CREATION));

            final Entity caseEntity = builder.describedBy(ENTITY_STRING).build();

            final EntityBuilder items = EntityBuilder.entity(c.getSourceName(), c.getDomain(), c.getName() + WORK_ITEM, "", c.getSchema());
            items.withIndex(CLOSED, CLOSED);
            items.addField(str(TASK));
            final AttributeBuilder reference = EntityBuilder.reference(PARENT_CASE, caseEntity);
            items.addField(reference);
            items.addField(EntityBuilder.dateTime(CREATION));
            items.addField(str(ASSIGNEE));
            items.addField(str(REPORTER).optional());
            items.addField(str(ORG_UNIT_NAME).optional());
            items.addField(EntityBuilder.bool(CLOSED));
            items.addField(str(DESCRIPTION));
            items.withIndex(UPDATE_TIME, UPDATE_TIME);
            items.addField(str(TITLE).optional());
            items.addField(EntityBuilder.integer(PRIORITY_CODE).optional().defaultValue(integer(3).createExpression()));
            items.addField(str(BUSINESS_KEY).optional());

            final Entity itemEntity = items.describedBy(DESCRIPTION).build();

            linkModels(caseEntity, itemEntity);

            return tuple(caseEntity, itemEntity);
        }
        catch (final BuilderException exception) {
            throw new IllegalStateException("Error creating case class", exception);
        }
    }

    private static void linkModels(@NotNull Entity caseEntity, @NotNull Entity itemEntity) {
        final ModelRepository repository = new ModelRepository();
        repository.add(caseEntity);
        repository.add(itemEntity);

        final ModelLinker linker = new ModelLinkerImpl(repository);
        linker.link(caseEntity);
        linker.link(itemEntity);
    }

    private static AttributeBuilder str(final String nm) {
        return EntityBuilder.string(nm, LENGTH);
    }

    //~ Static Fields ................................................................................................................................

    private static final int LENGTH = 256;
}  // end class CaseToEntity
