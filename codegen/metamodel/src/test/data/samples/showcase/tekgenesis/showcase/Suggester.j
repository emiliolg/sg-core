package tekgenesis.showcase;

import java.util.ArrayList;
import tekgenesis.common.core.DateOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.Suggestion;

/** User class for form: Suggester */
public class Suggester
    extends SuggesterBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the user type something on suggest_box(simple) to create suggest list */
    @NotNull public static Iterable<SimpleEntity> suggestSimple(@Nullable String query) { return new ArrayList<SimpleEntity>(); }

    /** Invoked when the user type something on suggest_box(simpleParam) to create suggest list */
    @NotNull public static Iterable<SimpleEntity> suggestSimpleParam(@Nullable String query, @Nullable DateOnly value) { return new ArrayList<SimpleEntity>(); }

    /** Invoked when the user type something on suggest_box(string) to create suggest list */
    @NotNull public static Iterable<Suggestion> suggestString(@Nullable String query) { return new ArrayList<Suggestion>(); }

    /** Invoked when the user type something on suggest_box(stringParam) to create suggest list */
    @NotNull public static Iterable<Suggestion> suggestStringParam(@Nullable String query, @Nullable DateOnly value) { return new ArrayList<Suggestion>(); }

    /** Invoked when the user type something on suggest_box(simpleSync) to create suggest list */
    @Override @NotNull public Iterable<SimpleEntity> suggestSimpleSync(@Nullable String query) { throw new IllegalStateException("To be implemented"); }

    /** Invoked when the user type something on suggest_box(stringSync) to create suggest list */
    @Override @NotNull public Iterable<Suggestion> suggestStringSync(@Nullable String query) { throw new IllegalStateException("To be implemented"); }

}
