
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import com.google.gwt.user.client.rpc.IsSerializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.menu.Menu;
import tekgenesis.metadata.menu.MenuItem;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.type.MetaModelKind.FORM;

/**
 * This class represent a Menu option in the menu bar.
 */
@SuppressWarnings("FieldMayBeFinal")
public class MenuOption implements IsSerializable, Comparable<MenuOption> {

    //~ Instance Fields ..............................................................................................................................

    private String domain;

    private String label;
    private String name;

    private String        parametersQueryString;
    private String        target     = null;
    private MetaModelKind targetType = null;

    //~ Constructors .................................................................................................................................

    MenuOption() {
        label                 = null;
        name                  = null;
        targetType            = null;
        target                = null;
        domain                = null;
        parametersQueryString = null;
    }

    /** Creates a menu item given a name and a label. */
    private MenuOption(@NotNull String domain, @NotNull String name, @NotNull String label, @Nullable MetaModelKind targetType,
                       @Nullable String target, @Nullable String parametersQueryString) {
        this.domain                = domain;
        this.label                 = label;
        this.name                  = name;
        this.targetType            = targetType;
        this.target                = target;
        this.parametersQueryString = parametersQueryString;
    }

    //~ Methods ......................................................................................................................................

    @Override public int compareTo(@NotNull final MenuOption o) {
        return Predefined.compare(name, o.name);
    }

    @Override public boolean equals(final Object obj) {
        return obj instanceof MenuOption && Predefined.equal(name, ((MenuOption) obj).name);
    }

    @Override public int hashCode() {
        return Predefined.hashCodeAll(name);
    }

    /** Returns the FQN. */
    public String getFullQualifiedName() {
        return QName.createQName(domain, name).getFullName();
    }

    /** Returns the menu item's label. */
    public String getLabel() {
        return label;
    }

    /** Parameters of a form as a query string. Empty string if it doesn't have any. */
    public String getParametersQueryString() {
        return parametersQueryString;
    }

    /** target. */
    public String getTarget() {
        return target;
    }

    /** Type of menu item. */
    public MetaModelKind getType() {
        return targetType;
    }

    //~ Methods ......................................................................................................................................

    public static MenuOption createFormDomainMenuOption(@NotNull final Form form) {
        return new MenuOption(form.getDomain(), form.getDomain(), form.getDomain(), null, null, null);
    }

    public static MenuOption createMenuItemOption(@NotNull final String d, @NotNull final MenuItem i) {
        return new MenuOption(d, i.getName(), i.getLabel(), i.getTargetType(), i.getTarget(), i.getAssignmentsAsQueryString());
    }

    public static MenuOption createMenuOption(@NotNull final Menu m) {
        return new MenuOption(m.getDomain(), m.getName(), m.getLabel(), null, null, null);
    }

    public static MenuOption createMenuOption(Form form) {
        return new MenuOption(form.getDomain(), form.getName(), form.getLabel(), FORM, form.getDomain() + "." + form.getName(), null);
    }
}  // end class MenuOption
