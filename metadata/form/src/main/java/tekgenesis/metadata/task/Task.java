
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.task;

import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.field.ModelField;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Modifier;

import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.core.Strings.*;
import static tekgenesis.metadata.task.TaskConstants.DEFAULT_PURGE_POLICY;
import static tekgenesis.type.MetaModelKind.TASK;

/**
 * Task MetaModel.
 */
public class Task implements MetaModel {

    //~ Instance Fields ..............................................................................................................................

    private final int batchSize;

    private final String cronExpression;
    private final String exclusionGroup;

    private final String            label;
    private final QName             modelKey;
    private final EnumSet<Modifier> modifiers;
    private final String            pattern;
    private final int               purgePolicy;
    private final String            scheduleAfter;
    private final String            sourceName;
    private final TransactionMode   transactionMode;
    private final TaskType          type;

    //~ Constructors .................................................................................................................................

    Task() {
        modelKey   = null;
        sourceName = null;
        label      = null;
        // noinspection DuplicateStringLiteralInspection
        cronExpression  = "never";
        exclusionGroup  = null;
        transactionMode = null;
        type            = null;
        purgePolicy     = DEFAULT_PURGE_POLICY;
        modifiers       = null;
        pattern         = null;
        batchSize       = 1;
        scheduleAfter   = null;
    }

    /** Copy constructor. */
    public Task(@NotNull final Task m) {
        this(m.getSourceName(),
            m.getDomain(),
            m.getName(),
            m.getType(),
            m.getLabel(),
            m.getCronExpression(),
            m.getExclusionGroup(),
            m.getTransactionMode(),
            m.getBatchSize(),
            m.getScheduleAfter(),
            m.modifiers,
            m.pattern);
    }

    /**  */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Task(@NotNull final String sourceName, @NotNull final String domain, @NotNull final String name, @NotNull TaskType type,
                @NotNull String label, @NotNull String cronExpression, @Nullable String exclusionGroup, @Nullable TransactionMode transactionMode,
                int batchSize, @Nullable String scheduleAfter, @NotNull EnumSet<Modifier> modifiers, @Nullable String pattern) {
        this.sourceName      = sourceName;
        this.label           = label;
        this.type            = type;
        this.transactionMode = transactionMode != null ? transactionMode : TransactionMode.ALL;
        modelKey             = QName.createQName(domain, name);
        this.cronExpression  = cronExpression;
        this.exclusionGroup  = exclusionGroup;
        purgePolicy          = DEFAULT_PURGE_POLICY;
        this.modifiers       = modifiers;
        this.pattern         = pattern;
        this.batchSize       = batchSize;
        this.scheduleAfter   = scheduleAfter;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object obj) {
        return obj instanceof Task && modelKey.equals(((Task) obj).modelKey);
    }

    @Override public int hashCode() {
        return modelKey.hashCode();
    }

    @Override public boolean hasModifier(Modifier mod) {
        return modifiers.contains(mod);
    }

    /** @return  TransactionMode */
    public TransactionMode transactionMode() {
        return transactionMode;
    }

    /** Return the task batch size. */
    public int getBatchSize() {
        return batchSize;
    }

    @NotNull @Override public Seq<? extends ModelField> getChildren() {
        return emptyIterable();
    }

    /** @return  cron expression */
    public String getCronExpression() {
        return cronExpression;
    }

    @NotNull @Override public String getDomain() {
        return modelKey.getQualification();
    }

    /** Exclusion Group name.* */
    @Nullable public String getExclusionGroup() {
        return exclusionGroup;
    }

    @NotNull @Override public String getFullName() {
        return modelKey.getFullName();
    }

    /** @return  Task Id */
    public String getId() {
        return Strings.deCapitalizeFirst(getName());
    }

    /** @return  The full class name of the implementation class */
    public String getImplementationClassFullName() {
        return getDomain() + "." + getImplementationClassName();
    }

    @NotNull @Override public QName getKey() {
        return modelKey;
    }

    @NotNull @Override public String getLabel() {
        return toWords(fromCamelCase(label));
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return TASK;
    }

    @NotNull @Override public String getName() {
        return modelKey.getName();
    }

    /** Return pattern for importer task. */
    public String getPattern() {
        return pattern;
    }

    /** .* */
    public int getPurgePolicy() {
        return purgePolicy;
    }

    @Override public Seq<MetaModel> getReferences() {
        return emptyIterable();
    }

    /** Return the fqn of the scheduled task. */
    public String getScheduleAfter() {
        return scheduleAfter;
    }

    @NotNull @Override public String getSchema() {
        return "";
    }

    @NotNull @Override public String getSourceName() {
        return sourceName;
    }

    /** Return transaction mode. */
    public TransactionMode getTransactionMode() {
        return transactionMode;
    }

    /** Return Task type. */
    public TaskType getType() {
        return type;
    }

    @Override public Seq<MetaModel> getUsages() {
        return emptyIterable();
    }

    private String getImplementationClassName() {
        return capitalizeFirst(getName());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3885793251633768336L;
}  // end class Task
