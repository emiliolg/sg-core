
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.TypeField;

import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.core.Option.empty;

/**
 * A Base abstract type that extends {@link MetaModel} and provides default implementations of
 * common {@link Type} methods.
 */
@SuppressWarnings("FieldMayBeFinal")
public abstract class ModelType extends AbstractType implements MetaModel {

    //~ Instance Fields ..............................................................................................................................

    private String defaultForm;

    private int   hashCode;
    private QName key;

    private String            label;
    private EnumSet<Modifier> modifiers;

    private String sourceName;

    private List<MetaModel> usages;

    //~ Constructors .................................................................................................................................

    /** constructor.* */
    ModelType() {
        defaultForm = null;
        hashCode    = -1;
        label       = null;
        modifiers   = null;
        sourceName  = null;
        usages      = null;
        key         = null;
    }

    @SuppressWarnings("WeakerAccess")
    protected ModelType(@NotNull String sourceName, @NotNull String domain, @NotNull String name) {
        this(sourceName, domain, name, "", Modifier.NONE, "");
    }

    protected ModelType(@NotNull String sourceName, @NotNull String domain, @NotNull String name, @NotNull String label,
                        @NotNull EnumSet<Modifier> modifiers, @NotNull String defaultForm) {
        this.sourceName  = sourceName;
        key              = QName.createQName(domain, name);
        this.label       = label;
        hashCode         = key.hashCode();
        usages           = new LinkedList<>();
        this.modifiers   = modifiers;
        this.defaultForm = defaultForm;
    }

    //~ Methods ......................................................................................................................................

    /** Add an usage to this ModelType. */
    @SuppressWarnings("WeakerAccess")
    public void addUsage(MetaModel metaModel) {
        usages.add(metaModel);
    }

    @Override public boolean equals(Object o) {
        return o instanceof ModelType && key.equals(((ModelType) o).key);
    }

    @Override public int hashCode() {
        return hashCode;
    }

    public boolean hasModifier(Modifier mod) {
        return modifiers.contains(mod);
    }

    /** Remove an usage from this ModelType. */
    public void removeUsage(MetaModel model) {
        usages.remove(model);
    }

    @Override public String toString() {
        return key.toString();
    }

    @NotNull @Override public Seq<? extends TypeField> getChildren() {
        return emptyList();
    }

    /** Returns the Entity (Or Enum) default form. or the empty string */
    @NotNull public String getDefaultForm() {
        return defaultForm;
    }

    @NotNull @Override public String getDomain() {
        return key.getQualification();
    }

    /** Returns the field with the given name or empty. */
    @NotNull public Option<? extends TypeField> getField(String name) {
        return empty();
    }

    @NotNull public final String getFullName() {
        return key.getFullName();
    }

    @Override public String getImplementationClassName() {
        return getFullName();
    }

    @NotNull @Override public QName getKey() {
        return key;
    }

    @NotNull @Override public final String getLabel() {
        return label;
    }

    /** Return the model modifiers. */
    public EnumSet<Modifier> getModifiers() {
        return modifiers;
    }

    @NotNull @Override public String getName() {
        return key.getName();
    }

    @Override public Seq<MetaModel> getReferences() {
        return emptyIterable();
    }

    @NotNull @Override public String getSchema() {
        return "";
    }

    @NotNull public final String getSourceName() {
        return sourceName;
    }

    @Override public Seq<MetaModel> getUsages() {
        return immutable(usages);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8864572773659685370L;
}  // end class ModelType
