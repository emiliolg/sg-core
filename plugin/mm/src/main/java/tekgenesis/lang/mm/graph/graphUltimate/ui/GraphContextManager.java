
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.ui;

import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.graph.view.Graph2D;

/**
 * Manager of Context.
 */
public class GraphContextManager {

    //~ Constructors .................................................................................................................................

    private GraphContextManager() {}

    //~ Methods ......................................................................................................................................

    /** add a new Context. */
    public static void addContext(Graph2D g, MMGraphContext context) {
        contexts.put(g, context);
    }
    /** Gets Context. */
    public static MMGraphContext getGraphContext(Graph2D g) {
        return contexts.get(g);
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<Graph2D, MMGraphContext> contexts = new HashMap<>();
}
