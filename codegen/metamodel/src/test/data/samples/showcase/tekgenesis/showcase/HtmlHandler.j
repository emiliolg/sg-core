package tekgenesis.showcase;

import tekgenesis.service.Factory;
import tekgenesis.service.html.Html;
import org.jetbrains.annotations.NotNull;
import tekgenesis.service.Result;

/** User class for Handler: HtmlHandler */
public class HtmlHandler
    extends HtmlHandlerBase
{

    //~ Constructors .............................................................................................................

    HtmlHandler(@NotNull Factory factory) { super(factory); }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/showcase/main" */
    @Override @NotNull public Result<Html> main() { return notImplemented(); }

    /** Invoked for route "/showcase/template" */
    @Override @NotNull public Result<Html> template() { return notImplemented(); }

}
