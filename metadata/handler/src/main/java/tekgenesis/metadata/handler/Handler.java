
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.type.*;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.type.AbstractType.addMetaModelTypes;
import static tekgenesis.type.Modifier.REMOTE;

/**
 * Class that contains metadata for service handlers.
 */
public class Handler implements MetaModel {

    //~ Instance Fields ..............................................................................................................................

    private final String form;

    private final QName               key;
    private final String              label;
    private final EnumSet<Modifier>   modifiers;
    @Nullable private final EnumType  raising;
    private final Seq<Route>          routes;
    private final String              sourceName;

    //~ Constructors .................................................................................................................................

    /** Handler default constructor. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Handler(QName key, String label, EnumSet<Modifier> modifiers, String sourceName, String form, @Nullable EnumType raising,
                   Iterable<Route> routes) {
        this.key        = key;
        this.label      = label;
        this.modifiers  = modifiers;
        this.raising    = raising;
        this.form       = notEmpty(form, "");
        this.routes     = Colls.seq(routes);
        this.sourceName = sourceName;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean hasModifier(Modifier mod) {
        return modifiers.contains(mod);
    }

    @NotNull @Override public Seq<Route> getChildren() {
        return routes;
    }

    @NotNull @Override public String getDomain() {
        return key.getQualification();
    }

    /** Return true if handler is for remote usage. */
    public boolean isRemote() {
        return modifiers.contains(REMOTE);
    }

    /** Return handler optional enumeration for errors. */
    public Option<EnumType> getErrorRaiseEnum() {
        return option(raising);
    }

    /** Get bound form or empty. */
    @NotNull public QName getForm() {
        return QName.createQName(form);
    }

    @NotNull @Override public String getFullName() {
        return key.getFullName();
    }

    /** Return the name of the user class. */
    public String getImplementationClassName() {
        return capitalizeFirst(getName());
    }

    @NotNull @Override public QName getKey() {
        return key;
    }

    @NotNull public String getLabel() {
        return label;
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return MetaModelKind.HANDLER;
    }

    @NotNull @Override public String getName() {
        return key.getName();
    }

    @Override public Seq<MetaModel> getReferences() {
        final Set<MetaModel> result = new HashSet<>();
        for (final EnumType errors : getErrorRaiseEnum())
            addMetaModelTypes(result, errors);
        for (final Route route : getChildren()) {
            final Type type = route.getType().getFinalType();
            addMetaModelTypes(result, type);
            final Type body = route.getBodyType().getFinalType();
            addMetaModelTypes(result, body);
            for (final Parameter parameter : route.getParameters())
                addMetaModelTypes(result, parameter.getFinalType());
        }
        return seq(result);
    }

    /** Return handler routes. */
    public Seq<Route> getRoutes() {
        return routes;
    }

    @NotNull @Override public String getSchema() {
        return "";
    }

    @NotNull @Override public String getSourceName() {
        return sourceName;
    }

    @Override public Seq<MetaModel> getUsages() {
        return emptyIterable();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3624910200301041984L;
}  // end class Handler
