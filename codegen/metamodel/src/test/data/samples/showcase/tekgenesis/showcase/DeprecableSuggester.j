package tekgenesis.showcase;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.Suggestion;

/** User class for form: DeprecableSuggester */
public class DeprecableSuggester
    extends DeprecableSuggesterBase
{

    //~ Methods ..................................................................................................................

    /** Invoked when the user type something on search_box(search) to create suggest list */
    @NotNull public static Iterable<Suggestion> suggest(@Nullable String query, boolean deprecated, @Nullable String value) { return new ArrayList<Suggestion>(); }

}
