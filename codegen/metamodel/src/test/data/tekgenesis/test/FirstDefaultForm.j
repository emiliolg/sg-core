package tekgenesis.test;

import tekgenesis.external.ExternalInstance;
import tekgenesis.form.ExternalNavigate;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;

/** Generated external form reference to FirstDefaultForm on project darpa. */
public class FirstDefaultForm
    extends ExternalInstance<FirstDefaultForm>
{

    //~ Constructors .............................................................................................................

    private FirstDefaultForm() { super("darpa",FirstDefaultForm.class); }

    //~ Methods ..................................................................................................................

    /** Return external navigate action. */
    @NotNull public static ExternalNavigate<FirstDefaultForm> navigate() { return instance.navigation(); }

    /** Return external navigate action with specified primary key. */
    @NotNull public static ExternalNavigate<FirstDefaultForm> navigate(int id) {
        return navigate("" + Integers.checkSignedLength("id", id, false, 9));
    }

    /** Return external navigate action with specified primary key. */
    @NotNull public static ExternalNavigate<FirstDefaultForm> navigate(@NotNull String key) { return instance.navigation(key); }

    //~ Fields ...................................................................................................................

    @NotNull private static final FirstDefaultForm instance = new FirstDefaultForm();

}
