package tekgenesis.showcase;

import tekgenesis.service.Factory;
import tekgenesis.service.html.Html;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: AppHtmlHandler */
public class AppHtmlHandler
    extends AppHtmlHandlerBase
{

    //~ Constructors .............................................................................................................

    AppHtmlHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/showcase/main/secure" */
    @Override @NotNull public Result<Html> main() { return notImplemented(); }

    /** Invoked for route "/showcase/template/secure" */
    @Override @NotNull public Result<Html> template() { return notImplemented(); }

}
