
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.form.Action;

import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * User class for Form: TagSuggestBoxOnNew
 */
public class TagSuggestBoxOnNew extends TagSuggestBoxOnNewBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the 'Create new' options of the tags_suggest_box(tags) is clicked. */
    @NotNull @Override public Action createAndTag(@Nullable String text) {
        if (isNotEmpty(text)) {
            if (Tag.find(text) == null) {
                final Tag newTag = Tag.create(text);
                newTag.insert();

                setTags(getTags().append(newTag));
            }
        }

        return actions.getDefault();
    }
}
