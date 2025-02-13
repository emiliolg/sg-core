package tekgenesis.showcase;

import tekgenesis.form.Action;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.Suggestion;

/** User class for form: SuggestBoxShowcase */
public class SuggestBoxShowcase
    extends SuggestBoxShowcaseBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the user type something on suggest_box(entityWithSuggest) to create suggest list */
    @NotNull public static Iterable<SimpleEntity> suggestEntity(@Nullable String query) { return new ArrayList<SimpleEntity>(); }

    /** Invoked when the user type something on suggest_box(integer) to create suggest list */
    @NotNull public static Iterable<Integer> suggestInts(@Nullable String query) { return new ArrayList<Integer>(); }

    /** Invoked when the user type something on suggest_box(strings) to create suggest list */
    @NotNull public static Iterable<Suggestion> onSuggestStrings(@Nullable String query, @Nullable SimpleEntity value) { return new ArrayList<Suggestion>(); }

    /** Invoked when the form is loaded */
    @Override public void load() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when suggest_box(entity) value changes */
    @Override @NotNull public Action entityChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when suggest_box(integer) value changes */
    @Override @NotNull public Action integerChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when suggest_box(strings) value ui changes */
    @Override @NotNull public Action changed() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when the user type something on suggest_box(stringsSync) to create suggest list */
    @Override @NotNull public Iterable<Suggestion> suggestSync(@Nullable String query) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when suggest_box(stringsSync) value changes */
    @Override @NotNull public Action stringsChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when the user type something on suggest_box(entitySync) to create suggest list */
    @Override @NotNull public Iterable<SimpleEntity> entitySuggestSync(@Nullable String query) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(resetStrings) is clicked */
    @Override @NotNull public Action resetStrings() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when tags_suggest_box(stringsTagsSync) value changes */
    @Override @NotNull public Action stringsTagsSyncChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when tags_suggest_box(entityTagsSync) value changes */
    @Override @NotNull public Action entityTagsSyncChanged() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(click) is clicked */
    @Override @NotNull public Action click() { throw new IllegalStateException("To be implemented"); }

    /** Invoked when button(clear) is clicked */
    @Override @NotNull public Action clear() { throw new IllegalStateException("To be implemented"); }

    //~ Inner Classes ............................................................................................................

    public class TableRow
        extends TableRowBase
    {

        //~ Methods ..................................................................................................................

        /** Invoked when suggest_box(columnStrings) value ui changes */
        @Override @NotNull public Action changed() { throw new IllegalStateException("To be implemented"); }

        /** Invoked when the user type something on suggest_box(columnStringsSync) to create suggest list */
        @Override @NotNull public Iterable<Suggestion> tableSuggestSync(@Nullable String query) { throw new IllegalStateException("To be implemented"); }

        /** Invoked when suggest_box(columnStringsSync) value changes */
        @Override @NotNull public Action stringsChanged() { throw new IllegalStateException("To be implemented"); }

        /** Invoked when the user type something on suggest_box(columnEntitySync) to create suggest list */
        @Override @NotNull public Iterable<SimpleEntity> entitySuggestSync(@Nullable String query) { throw new IllegalStateException("To be implemented"); }

    }
}
