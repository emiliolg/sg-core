
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

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.expr.Expression;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;

import static java.util.Collections.emptyList;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.QName.createQName;

/**
 * Link Builder.
 */
public class LinkBuilder extends ModelBuilder.Default<Link, LinkBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private final List<Assignment> assignments;
    private Option<Form>           form;

    private Option<String> link;

    //~ Constructors .................................................................................................................................

    /** Builder constructor. */
    public LinkBuilder(@NotNull String src, @NotNull String pkg, @NotNull String name) {
        super(src, pkg, name);

        link = Option.empty();
        form = Option.empty();

        assignments = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Add a parameter assignation. */
    public void addAssignation(ModelField field, Expression value) {
        assignments.add(new Assignment(field, value));
    }

    @Override public Link build()
        throws BuilderException
    {
        final QName name = createQName(domain, id);
        final Link  l    = new Link(name, label, sourceName);

        if (link.isPresent()) l.setLink(link.get());
        else if (form.isPresent()) {
            l.setForm(form.get());
            l.setAssignments(assignments);
        }

        return l;
    }  // end method build

    @NotNull @Override public List<BuilderError> check() {
        return emptyList();
    }

    /** Check if parameter assignation is contained. */
    public boolean containsAssignation(String field) {
        for (final Assignment assignment : assignments) {
            if (assignment.getField().getName().equals(field)) return true;
        }
        return false;
    }

    /** Form reference. */
    public void withForm(Form reference) {
        form = some(reference);
    }

    /** Link url. */
    public void withLink(@NotNull String l) {
        link = some(l);
    }
}
