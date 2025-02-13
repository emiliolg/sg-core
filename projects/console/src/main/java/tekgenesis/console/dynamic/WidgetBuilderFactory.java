
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

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.WidgetBuilder;
import tekgenesis.type.DecimalType;
import tekgenesis.type.EntityReference;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;

/**
 * WidgetBuilder Factory for Dynamic Forms.
 */
public final class WidgetBuilderFactory {

    //~ Constructors .................................................................................................................................

    private WidgetBuilderFactory() {}

    //~ Methods ......................................................................................................................................

    /**
     * Create WidgetBuilder.
     *
     * @param   primaryKeys  Primary Keys
     * @param   attribute    Attribute
     *
     * @return  WidgetBuilder
     */
    public static WidgetBuilder create(@NotNull ImmutableList<Attribute> primaryKeys, @NotNull Attribute attribute)
        throws BuilderException
    {
        final Type   type       = attribute.getType().getFinalType();
        final String columnName = attribute.getColumnName();

        final String label = Utils.getLabel(attribute);

        WidgetBuilder widgetBuilder = getWidgetFor(attribute, type, columnName, label);
        if (primaryKeys.contains(attribute) || attribute.isInner()) widgetBuilder = widgetBuilder.disable();
        return widgetBuilder;
    }

    private static WidgetBuilder getWidgetFor(@NotNull Attribute attribute, @NotNull Type type, @NotNull String id, @NotNull String label)
        throws BuilderException
    {
        WidgetBuilderCreator creator = new DefaultWidgetBuilderCreator();
        if (type.isEntity()) creator = new EntityWidgetBuilderCreator();
        if (type.isBoolean()) creator = new BooleanWidgetBuilderCreator();
        if (type.isNumber()) creator = new NumberWidgetBuilderCreator();
        if (type.isEnum()) creator = new EnumWidgetBuilderCreator();
        if (type.isTime()) creator = new TimeWidgetBuilderCreator();

        return Option.option(creator).getOrFail("Widget Creator not found ").build(attribute, type, id, label);
    }

    //~ Inner Interfaces .............................................................................................................................

    public interface WidgetBuilderCreator {
        /**
         * Create WidgetBuilder.
         *
         * @param   attribute  Attribute
         * @param   type       Type
         * @param   id         widget id
         * @param   label      widget label
         *
         * @return  WidgetBuilder
         */
        WidgetBuilder build(@NotNull Attribute attribute, Type type, String id, String label)
            throws BuilderException;
    }

    //~ Inner Classes ................................................................................................................................

    static class BooleanWidgetBuilderCreator implements WidgetBuilderCreator {
        public WidgetBuilder build(@NotNull Attribute attribute, Type type, String id, String label)
            throws BuilderException
        {
            return check(label).id(id);
        }
    }

    static class DefaultWidgetBuilderCreator implements WidgetBuilderCreator {
        public WidgetBuilder build(@NotNull Attribute attribute, Type type, String id, String label)
            throws BuilderException
        {
            return field(label).id(id).optional();
        }
    }

    static class EntityWidgetBuilderCreator implements WidgetBuilderCreator {
        public WidgetBuilder build(@NotNull Attribute attribute, Type type, String id, String label)
            throws BuilderException
        {
            final String rowFieldName = Utils.getEntityReferenceFieldName(type, id);
            final String className    = attribute.getType().getImplementationClassName();
            final QName  qName        = QName.createQName(className);

            final EntityReference entityReference = new EntityReference(qName.getQualification(), qName.getName());
            final WidgetBuilder   widgetBuilder   = suggest(label).withType(entityReference);
            return widgetBuilder.id(rowFieldName).optional();
        }
    }

    static class EnumWidgetBuilderCreator implements WidgetBuilderCreator {
        public WidgetBuilder build(@NotNull Attribute attribute, Type type, String id, String label)
            throws BuilderException
        {
            return combo(label).withType(type).id(id).optional();
        }
    }

    static class NumberWidgetBuilderCreator implements WidgetBuilderCreator {
        public WidgetBuilder build(@NotNull Attribute attribute, Type type, String id, String label)
            throws BuilderException
        {
            final WidgetBuilder widgetBuilder;
            final Kind          kind = type.getKind();
            if (kind == Kind.DECIMAL) {
                final DecimalType dt = (DecimalType) type;
                widgetBuilder = numeric(label, dt.getPrecision(), dt.getDecimals());
            }
            else widgetBuilder = numericInt(label, type.getLength().get());
            return widgetBuilder.id(id).optional();
        }
    }

    static class TimeWidgetBuilderCreator implements WidgetBuilderCreator {
        public WidgetBuilder build(@NotNull Attribute attribute, Type type, String id, String label)
            throws BuilderException
        {
            final WidgetBuilder widgetBuilder = type.getKind() == Kind.DATE ? dateBox(label) : datetimeBox(label);
            return widgetBuilder.id(id).optional();
        }
    }
}
