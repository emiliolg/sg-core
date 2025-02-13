
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.io.PrintWriter;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.util.Files;

import static tekgenesis.database.DbMacro.AlterView;
import static tekgenesis.database.DbMacro.CommentOnView;
import static tekgenesis.database.introspect.TableInfo.getQName;

/**
 * Metadata Information about a database view.
 */
public class ViewInfo extends SchemaObject<ViewInfo> {

    //~ Instance Fields ..............................................................................................................................

    private boolean notLoaded = true;

    private String viewSql = null;

    //~ Constructors .................................................................................................................................

    ViewInfo(SchemaInfo schema, String name, @Nullable String remarks) {
        super(schema, name, remarks);
    }

    //~ Methods ......................................................................................................................................

    /** The name as a a QName. */
    public String asQName() {
        return getQName(getSchema().getPlainName(), getName());
    }

    /** Write the Sql DDL statements to the specified writer. */
    public void dumpSql(PrintWriter writer, boolean create) {
        loadAll();
        final PrintWriter w = Files.printWriter(writer);
        w.print(create ? "create view" : AlterView);
        w.printf(" %s as\n\t%s;;", asQName(), viewSql);
        w.println();
        w.println();
        w.printf("%s %s is '%s';;", CommentOnView, asQName(), quoteViewSql(viewSql));
        w.println();
    }

    /** load all data for view. */
    public void loadAll() {
        if (notLoaded) viewSql = getRetriever().getViewSql(getSchema(), getName());
        notLoaded = false;
    }

    @Override public boolean sameAs(ViewInfo to) {
        loadAll();
        to.loadAll();
        return viewSql.equals(to.viewSql);
    }

    private MetadataRetriever getRetriever() {
        return getSchema().getIntrospector().getRetriever();
    }

    //~ Methods ......................................................................................................................................

    /** Quote the view sql. */
    public static String quoteViewSql(String viewSql) {
        return viewSql.trim().replace("\n", " ").replace("        ", "").replace("'", "''");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 7885789463303626821L;
}  // end class ViewInfo
