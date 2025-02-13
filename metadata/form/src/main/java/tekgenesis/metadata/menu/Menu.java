
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.menu;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Modifier;

import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.toWords;
import static tekgenesis.type.MetaModelKind.MENU;

/**
 * This class represents a Menu. A Menu has a set of MenuItem.
 */
public class Menu implements MetaModel {

    //~ Instance Fields ..............................................................................................................................

    private final String                 label;
    @SuppressWarnings(
                      "GwtInconsistentSerializableClass"
                     )  // lie!!! This module is not part of form.gwt.xml but the
                        // inspection's plugin is not aware of that
    private final ImmutableList<MenuItem> menuItems;
    private final QName                  modelKey;
    private final String                 sourceName;

    //~ Constructors .................................................................................................................................

    Menu() {
        menuItems  = null;
        modelKey   = null;
        sourceName = null;
        label      = null;
    }

    /** Copy constructor. */
    public Menu(@NotNull final Menu m, @NotNull final List<MenuItem> items) {
        this(m.getSourceName(), m.getDomain(), m.getName(), items, m.getLabel());
    }

    Menu(@NotNull final String sourceName, @NotNull final String domain, @NotNull final String name, @NotNull final List<MenuItem> menuItems,
         @NotNull String label) {
        this.sourceName = sourceName;
        this.menuItems  = immutable(menuItems);
        this.label      = label;
        modelKey        = QName.createQName(domain, name);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object obj) {
        return obj instanceof Menu && modelKey.equals(((Menu) obj).modelKey);
    }

    @Override public int hashCode() {
        return modelKey.hashCode();
    }

    @Override public boolean hasModifier(Modifier mod) {
        return false;
    }

    @NotNull @Override public Seq<MenuItem> getChildren() {
        return menuItems;
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

    @NotNull @Override public String getLabel() {
        return toWords(fromCamelCase(label));
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return MENU;
    }

    @NotNull @Override public String getName() {
        return modelKey.getName();
    }

    @Override public Seq<MetaModel> getReferences() {
        return emptyIterable();
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

    private static final long serialVersionUID = -4189467487029516476L;
}  // end class Menu
