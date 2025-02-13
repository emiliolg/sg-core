package tekgenesis.html;

import tekgenesis.service.html.Html;
import tekgenesis.service.html.HtmlBuilder;
import tekgenesis.service.html.HtmlFactory;
import org.jetbrains.annotations.NotNull;

/** Generated factory class to build html templates for handlers. */
@SuppressWarnings({"WeakerAccess", "DuplicateStringLiteralInspection"})
public class ViewsFactory
    extends HtmlFactory
{

    //~ Fields ...................................................................................................................

    @NotNull private final HtmlBuilder builder;

    //~ Constructors .............................................................................................................

    ViewsFactory(@NotNull HtmlBuilder builder) {
        super(builder);
        this.builder = builder;
    }

    //~ Methods ..................................................................................................................

    /** Returns an Html instance for template located in "/html/fragment/home.xhtml" */
    @NotNull public Html fragmentHome(@NotNull String baby) {
        return builder.html("/html/fragment/home.xhtml").str("baby", baby).build();
    }

    /** Returns an Html instance for template located in "/html/template.xhtml" */
    @NotNull public Html template(@NotNull String baby, @NotNull Html home) {
        return builder.html("/html/template.xhtml").str("baby", baby).html("home", home).build();
    }

}
