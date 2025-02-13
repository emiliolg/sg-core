
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.link;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Modifier;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.QName.EMPTY;
import static tekgenesis.type.MetaModelKind.LINK;

/**
 * Class that contains metadata for links.
 */
public class Link implements MetaModel {

    //~ Instance Fields ..............................................................................................................................

    private List<Assignment> assignments;
    private Form             form;

    private final QName key;
    private String      label;

    private String       link;
    private final String sourceName;

    //~ Constructors .................................................................................................................................

    Link() {
        key         = EMPTY;
        label       = "";
        sourceName  = "";
        link        = "";
        form        = null;
        assignments = new ArrayList<>();
    }

    /** Link constructor. */
    public Link(@NotNull QName key, @NotNull String label, @NotNull String sourceName) {
        this.key        = key;
        this.label      = label;
        this.sourceName = sourceName;

        link        = "";
        form        = null;
        assignments = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Has this link form parameters assignments or not. */
    public boolean hasAssignments() {
        return isNotEmpty(assignments);
    }

    /** Tells if this link points to a form or not. */
    public boolean hasForm() {
        return form != null;
    }

    /** Returns true if this link points to an external URL. */
    public boolean hasLink() {
        return isNotEmpty(link);
    }

    @Override public boolean hasModifier(Modifier mod) {
        return false;
    }

    /** Gets parameters of the linked form. */
    @NotNull public Seq<Assignment> getAssignments() {
        return immutable(assignments);
    }

    /** Sets parameters of the linked form. */
    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    @NotNull @Override public Seq<? extends ModelField> getChildren() {
        return emptyIterable();
    }

    @NotNull @Override public String getDomain() {
        return key.getQualification();
    }

    /** Gets the form this link points to. */
    public Form getForm() {
        return form;
    }

    /** Points link to a form. */
    public void setForm(Form form) {
        this.form = form;
    }

    @NotNull @Override public String getFullName() {
        return key.getFullName();
    }

    @NotNull @Override public QName getKey() {
        return key;
    }

    @NotNull @Override public String getLabel() {
        return label;
    }

    /** Returns the url link that this link represents. */
    public String getLink() {
        return link;
    }

    /** Sets link in case this link points to an external URL. */
    public void setLink(String link) {
        this.link = link;
    }

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return LINK;
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

    @NotNull @Override public String getSourceName() {
        return sourceName;
    }

    @Override public Seq<MetaModel> getUsages() {
        return emptyIterable();
    }

    void setLabel(String label) {
        this.label = label;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3624910200324021984L;
}  // end class Link
