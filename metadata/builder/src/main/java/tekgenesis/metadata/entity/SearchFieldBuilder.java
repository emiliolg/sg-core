
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import tekgenesis.field.FieldOption;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateOptionException;
import tekgenesis.metadata.exception.UnsupportedOptionException;

import static tekgenesis.metadata.entity.SearchField.searchField;
import static tekgenesis.type.MetaModelKind.SEARCHABLE;

/**
 * Searchable field builder.
 */
public class SearchFieldBuilder extends FieldBuilder<SearchFieldBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private final ModelField field;
    private final String     id;

    //~ Constructors .................................................................................................................................

    /** Constructor for field builder. */
    public SearchFieldBuilder(ModelField field, String id) {
        this.field = field;
        this.id    = id;
    }

    //~ Methods ......................................................................................................................................

    /** Build SearchField. */
    public SearchField build() {
        return searchField(id, field, getOptions());
    }

    /** Get field id. */
    public String getId() {
        return id;
    }

    @Override protected void checkOptionSupport(FieldOption option)
        throws BuilderException
    {
        if (!option.isApplicable(SEARCHABLE)) throw new UnsupportedOptionException(option);
        if (getOptions().hasOption(option)) throw new DuplicateOptionException(option.getId());
    }

    @Override protected String getName() {
        return field.getName();
    }
}  // end class SearchFieldBuilder
