package tekgenesis.html;

import tekgenesis.service.html.Html;
import tekgenesis.service.html.HtmlBuilder;
import tekgenesis.service.html.HtmlFactory;
import org.jetbrains.annotations.NotNull;
import tekgenesis.test.Product;
import tekgenesis.common.collections.Seq;

/** Generated factory class to build html templates for handlers. */
@SuppressWarnings({"WeakerAccess", "DuplicateStringLiteralInspection"})
public class ViewsSimpleFactory
    extends HtmlFactory
{

    //~ Fields ...................................................................................................................

    @NotNull private final HtmlBuilder builder;

    //~ Constructors .............................................................................................................

    ViewsSimpleFactory(@NotNull HtmlBuilder builder) {
        super(builder);
        this.builder = builder;
    }

    //~ Methods ..................................................................................................................

    /** Returns an Html instance for template located in "/html/a.xhtml" */
    @NotNull public Html a(@NotNull String title, @NotNull Html content) {
        return builder.html("/html/a.xhtml").str("title", title).html("content", content).build();
    }

    /** Returns an Html instance for template located in "/html/b.xhtml" */
    @NotNull public Html b() { return builder.html("/html/b.xhtml").build(); }

    /** Returns an Html instance for template located in "/html/d.xhtml" */
    @NotNull public Html d(@NotNull String before, @NotNull Seq<String> sections, @NotNull Seq<Product> multiple, @NotNull String after) {
        return builder.html("/html/d.xhtml").str("before", before).str("sections", sections).struct("multiple", multiple).str("after", after).build();
    }

    /** Returns an Html instance for template located in "/html/e.mustache" */
    @NotNull public Html e(@NotNull String title, @NotNull Html content) {
        return builder.mustache("/html/e.mustache").param("title", title).param("content", content).build();
    }

    /** Returns an Html instance for template located in "/html/f.mustache" */
    @NotNull public Html f() { return builder.mustache("/html/f.mustache").build(); }

    /** Returns an Html instance for template located in "/html/g.mustache" */
    @NotNull public Html g(@NotNull String before, @NotNull Seq<String> sections, @NotNull Seq<Product> multiple, @NotNull String after) {
        return builder.mustache("/html/g.mustache").param("before", before).param("sections", sections).param("multiple", multiple).param("after", after).build();
    }

    /** Returns an Html instance for template located in "/html/nested/c.xhtml" */
    @NotNull public Html nestedC() { return builder.html("/html/nested/c.xhtml").build(); }

    /** Returns an Html instance for template located in "/html/nested/h.mustache" */
    @NotNull public Html nestedH() { return builder.mustache("/html/nested/h.mustache").build(); }

}
