package tekgenesis.test;

import tekgenesis.external.ExternalInstance;
import tekgenesis.form.ExternalNavigate;
import org.jetbrains.annotations.NotNull;
import tekgenesis.common.core.Reals;
import tekgenesis.common.core.Strings;

/** Generated external form reference to FirstForm on project darpa. */
public class FirstForm
    extends ExternalInstance<FirstForm>
{

    //~ Constructors .............................................................................................................

    private FirstForm() { super("darpa",FirstForm.class); }

    //~ Methods ..................................................................................................................

    /** Return external navigate action. */
    @NotNull public static ExternalNavigate<FirstForm> navigate() { return instance.navigation(); }

    /** Return external navigate action with specified primary key. */
    @NotNull public static ExternalNavigate<FirstForm> navigate(@NotNull String a, @NotNull String b, @NotNull String cA, double cB, @NotNull String cPA, double cPB, @NotNull String dA, double dB, @NotNull String dPA, double dPB) {
        String pk = "";
        pk = pk + Strings.escapeCharOn(a, ':');
        pk = pk + Strings.escapeCharOn(b, ':');
        pk = pk + Strings.escapeCharOn(cA, ':');
        pk = pk + Reals.checkSigned("cB", cB, false);
        pk = pk + Strings.escapeCharOn(cPA, ':');
        pk = pk + Reals.checkSigned("cPB", cPB, true);
        pk = pk + Strings.escapeCharOn(dA, ':');
        pk = pk + Reals.checkSigned("dB", dB, false);
        pk = pk + Strings.escapeCharOn(dPA, ':');
        pk = pk + Reals.checkSigned("dPB", dPB, true);
        return navigate(pk);
    }

    /** Return external navigate action with specified primary key. */
    @NotNull public static ExternalNavigate<FirstForm> navigate(@NotNull String key) { return instance.navigation(key); }

    //~ Fields ...................................................................................................................

    @NotNull private static final FirstForm instance = new FirstForm();

}
