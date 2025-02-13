
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.core.QName;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.exception.InvalidFieldNameException;
import tekgenesis.metadata.exception.InvalidTypeException;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.immutable;

/**
 * Collects the data to build a {@link Type}.
 */
@SuppressWarnings({ "WeakerAccess", "UnusedReturnValue" })
public abstract class CompositeBuilder  //J-
        <T extends MetaModel,
         F extends TypeField,
         FB extends CompositeFieldBuilder<FB>,
         This extends CompositeBuilder<T, F, FB, This>>
//J+
    extends ModelBuilder.Default<T, This>
{

    //~ Instance Fields ..............................................................................................................................

    @NotNull final List<BuilderError> builderErrors;

    @NotNull String defaultForm;

    @NotNull final Map<String, FB> fields;

    //~ Constructors .................................................................................................................................

    CompositeBuilder(String sourceName, @NotNull String pkg, @NotNull String name) {
        super(sourceName, pkg, name);
        builderErrors = new LinkedList<>();
        fields        = new LinkedHashMap<>();
        defaultForm   = "";
    }

    //~ Methods ......................................................................................................................................

    /** Add a field to the builder. */
    public void addField(FB a)
        throws DuplicateFieldException, InvalidFieldNameException, InvalidTypeException
    {
        checkDuplicates(a);
        fields.put(a.getName(), a);
    }

    /** Add a field to the builder. */
    public final void addInternalField(FB a) {
        if (!fields.containsKey(a.getName())) fields.put(a.getName(), a);
    }

    /** Sets the default form for this Entity. */
    public This defaultForm(String formId) {
        defaultForm = QName.qualify(getDomain(), formId);
        return cast(this);
    }

    /** Adds attributes to this entity. */
    @SafeVarargs public final This fields(FB... fieldBuilders)
        throws DuplicateFieldException, InvalidFieldNameException, InvalidTypeException
    {
        for (final FB f : fieldBuilders)
            addField(f);
        return cast(this);
    }

    /** Return the Fields to be built. */
    @NotNull public ImmutableCollection<FB> getFields() {
        return immutable(fields.values());
    }

    void buildAttributes(Map<String, TypeField> map, MetaModel model) {
        for (final FB b : fields.values()) {
            final TypeField a = b.build(model);
            map.put(a.getName(), a);
        }
    }

    void checkDuplicates(FB a)
        throws DuplicateFieldException
    {
        if (fields.containsKey(a.getName())) throw DuplicateFieldException.onEntity(a.getName(), id);
    }

    @Nullable final FB getAttribute(String name) {
        return fields.get(name);
    }
}  // end class CompositeBuilder
