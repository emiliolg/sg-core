
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.type.Modifier.LISTING;

/**
 * Builder to create graphical model instances such as {@link Form} and {@link WidgetDef}.
 */
public abstract class UiModelBuilder<T extends MetaModel, This extends UiModelBuilder<T, This>> extends WidgetBuilder
    implements ModelBuilder<T, This>
{

    //~ Instance Fields ..............................................................................................................................

    protected boolean generated;

    int autoIdCount;

    @NotNull Option<Type> binding;

    @NotNull final String domain;
    int                   globalOptionsDimension;

    @NotNull final String id;

    @NotNull final EnumSet<Modifier> modifiers;
    int                              multipleDimension;
    @NotNull final List<ModelField>  parameters;
    @Nullable ModelRepository        repository;
    @NotNull final String            sourceName;

    //~ Constructors .................................................................................................................................

    UiModelBuilder(@NotNull String sourceName, @NotNull String pkg, @NotNull String name, @NotNull final WidgetType widgetType) {
        this(sourceName, pkg, name, widgetType, new FieldOptions());
    }

    UiModelBuilder(@NotNull String sourceName, @NotNull String pkg, @NotNull String name, @NotNull final WidgetType widgetType,
                   @NotNull final FieldOptions options) {
        super(widgetType, options);
        domain          = pkg;
        id              = name;
        this.sourceName = sourceName;
        autoIdCount     = 0;
        repository      = null;
        parameters      = new ArrayList<>();
        modifiers       = Modifier.emptySet();
        binding         = empty();
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public List<BuilderError> check() {
        return emptyList();
    }

    @Override public This children(@NotNull WidgetBuilder... bs)
        throws BuilderException
    {
        return cast(super.children(bs));
    }

    @Override public This label(@NotNull String label) {
        return cast(super.label(label));
    }

    /** Sets the form parameters. */
    public void parameters(Collection<ModelField> fs) {
        if (!parameters.isEmpty()) throw new IllegalStateException("Parameters already set");
        parameters.addAll(fs);
    }

    /** Specifies graphical model binding. */
    public This withBinding(Type type) {
        binding = type.isNull() ? empty() : of(type);
        return self();
    }

    /** For now this will be empty, in the future it might have documentation. */
    @Override public This withDocumentation(@NotNull String documentation) {
        return self();
    }

    @Override public This withModifier(Modifier mod) {
        modifiers.add(mod);
        return self();
    }

    @Override public This withModifiers(EnumSet<Modifier> mods) {
        modifiers.addAll(mods);
        return self();
    }

    /** Specify the repository to be used by the build. */
    public This withRepository(ModelRepository rep) {
        repository = rep;
        return self();
    }

    /** Return ui model optional binding. */
    @NotNull public Option<Type> getBinding() {
        return binding;
    }

    @NotNull @Override public String getFullName() {
        return domain + "." + id;
    }

    /** Return true if ui model is listing. */
    public boolean isListing() {
        return hasModifier(LISTING);
    }

    /**
     * Sets a boolean indicating that the ui model has been auto-generated from the specified
     * binding.
     */
    public This setGenerated(boolean b) {
        generated = b;
        return self();
    }

    @NotNull @Override public String getId() {
        return id;
    }

    @NotNull ModelRepository assertRepositoryNotNull() {
        if (repository == null) throw new IllegalStateException(FORGOT_WITH_REPOSITORY);
        return repository;
    }

    Option<DbObject> bindingAsDbObject() {
        return binding.castTo(DbObject.class);
    }

    boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    /** Get the qualified name of the bound meta model. */
    @NotNull QName getBindingKey() {
        return binding.map(t -> createQName(t.getImplementationClassName())).orElse(QName.EMPTY);
    }

    private This self() {
        return cast(this);
    }

    //~ Static Fields ................................................................................................................................

    static final String FORGOT_WITH_REPOSITORY = "Forgot to call \"withRepository\" method";
}  // end class UiModelBuilder
