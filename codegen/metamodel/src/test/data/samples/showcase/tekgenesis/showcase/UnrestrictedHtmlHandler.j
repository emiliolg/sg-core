package tekgenesis.showcase;

import tekgenesis.service.Factory;
import tekgenesis.service.html.Html;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: UnrestrictedHtmlHandler */
public class UnrestrictedHtmlHandler
    extends UnrestrictedHtmlHandlerBase
{

    //~ Constructors .............................................................................................................

    UnrestrictedHtmlHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/showcase/main/unrestricted" */
    @Override @NotNull public Result<Html> main() { return notImplemented(); }

    /** Invoked for route "/showcase/template/unrestricted" */
    @Override @NotNull public Result<Html> template() { return notImplemented(); }

}
