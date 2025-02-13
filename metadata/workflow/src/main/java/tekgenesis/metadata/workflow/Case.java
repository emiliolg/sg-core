
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.workflow;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Modifier;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.Predefined.option;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.toWords;

/**
 * This class represents a Case of the workflow package. A case has a set of tasks.
 */
public class Case implements MetaModel {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private final DbObject boundEntity;
    @NotNull private final QName     defaultForm;

    @NotNull private final QName        modelKey;
    private final boolean               notify;
    @NotNull private final FieldOptions options;
    @NotNull private final String       schema;
    @NotNull private final String       sourceName;

    /*@NotNull private final ImmutableList<Attribute> attributes;*/
    @NotNull private final Map<String, Task> tasks;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("ConstructorWithTooManyParameters")
    Case(@NotNull String sourceName, @NotNull String domain, @NotNull String name, @NotNull Map<String, Task> tasks, @Nullable DbObject boundEntity,
         @NotNull QName defaultForm, @NotNull String schema, boolean notify) {
        this.sourceName  = sourceName;
        this.tasks       = tasks;
        this.boundEntity = boundEntity;
        this.defaultForm = defaultForm;
        modelKey         = QName.createQName(domain, name);
        options          = FieldOptions.EMPTY;
        this.schema      = schema;
        this.notify      = notify;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object obj) {
        return obj instanceof Case && modelKey.equals(((Case) obj).modelKey);
    }

    @Override public int hashCode() {
        return modelKey.hashCode();
    }

    @Override public boolean hasModifier(Modifier mod) {
        return false;
    }

    @Override public String toString() {
        return "Case(" + modelKey + ")";
    }

    /** Returns the Entity we are bind to. */
    @NotNull public Option<DbObject> getBoundEntity() {
        return Option.option(boundEntity);
    }

    @NotNull @Override public Seq<Task> getChildren() {
        return immutable(tasks.values());
    }

    /** Returns the case default form. */
    @NotNull public QName getDefaultForm() {
        return defaultForm;
    }

    @NotNull @Override public String getDomain() {
        return modelKey.getQualification();
    }

    @NotNull @Override public String getFullName() {
        return modelKey.getFullName();
    }

    @NotNull @Override public QName getKey() {
        return modelKey;
    }

    /** Get the Case label. */
    @NotNull public String getLabel() {
        return notEmpty(getRawLabel(), toWords(fromCamelCase(getName())));
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return MetaModelKind.CASE;
    }

    @NotNull @Override public String getName() {
        return modelKey.getName();
    }

    @Override public Seq<MetaModel> getReferences() {
        return Option.<MetaModel>option(boundEntity).toList();
    }

    @NotNull @Override public String getSchema() {
        return schema;
    }

    @NotNull @Override public String getSourceName() {
        return sourceName;
    }

    /** Returns an option with the named task or null if it does not exist. */
    @NotNull public Option<Task> getTask(String task) {
        return option(tasks.get(task));
    }

    @Override public Seq<MetaModel> getUsages() {
        return emptyIterable();
    }

    /** Returns if the case send notifications. */
    public boolean isNotify() {
        return notify;
    }

    /** Get the explicitly set label. May be empty */
    @NotNull private String getRawLabel() {
        return options.getString(FieldOption.LABEL);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 109901875289747613L;
}  // end class Case
