package tekgenesis.test;

import tekgenesis.external.ExternalInstance;
import tekgenesis.form.ExternalNavigate;
import org.jetbrains.annotations.NotNull;

/** Generated external form reference to FirstStringForm on project darpa. */
public class FirstStringForm
    extends ExternalInstance<FirstStringForm>
{

    //~ Constructors .............................................................................................................

    private FirstStringForm() { super("darpa",FirstStringForm.class); }

    //~ Methods ..................................................................................................................

    /** Return external navigate action. */
    @NotNull public static ExternalNavigate<FirstStringForm> navigate() { return instance.navigation(); }

    /** Return external navigate action with specified primary key. */
    @NotNull public static ExternalNavigate<FirstStringForm> navigate(@NotNull String key) { return instance.navigation(key); }

    //~ Fields ...................................................................................................................

    @NotNull private static final FirstStringForm instance = new FirstStringForm();

}
