
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import tekgenesis.admin.StatusService;
import tekgenesis.service.html.Html;
import tekgenesis.service.html.HtmlBuilderImpl;

/**
 * Test HealthChecker.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class TestStatusService implements StatusService {

    //~ Methods ......................................................................................................................................

    @Override public boolean check() {
        return ok;
    }

    @Override public Html html() {
        return new HtmlBuilderImpl().staticSource("Hello World").build();
    }

    @Override public String name() {
        return TEST_STATUS;
    }

    @Override public ObjectNode status() {
        final ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("version", "1.0");
        return objectNode;
    }

    //~ Methods ......................................................................................................................................

    public static void setOk(boolean b) {
        ok = b;
    }

    //~ Static Fields ................................................................................................................................

    public static final String TEST_STATUS = "TestStatus";
    private static boolean     ok          = true;
}
