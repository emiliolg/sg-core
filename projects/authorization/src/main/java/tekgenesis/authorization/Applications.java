
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.form.Suggestion;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.authorization.shiro.AuthorizationUtils.getModelRepository;

/**
 * Helper class for Applications.
 */
public class Applications {

    //~ Constructors .................................................................................................................................

    private Applications() {}

    //~ Methods ......................................................................................................................................

    /**
     * Returns a map with all the application full names that partially match a given query string.
     */
    public static Seq<Suggestion> applicationMapFromQuery(@Nullable final String query) {
        final ModelRepository repository = getModelRepository();

        final boolean getAll = "*".equals(query);

        return repository.getModels()
               .filter(Form.class, f -> {
                        // return f != null && new FormLocalizer(f,
                        // Context.getContext().getLocale()).localize().getLabel().contains(query);
                        return getAll || f != null && query != null && f.getName().toLowerCase().contains(query.toLowerCase());
                    })
               .map(Form::getFullName)
               .take(10)
               .map(Suggestion::create);
    }
}
