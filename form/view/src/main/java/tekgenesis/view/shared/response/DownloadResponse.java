
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;

/**
 * FormResponse for download.
 */
@SuppressWarnings("FieldMayBeFinal")
public class DownloadResponse implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String  location;
    private boolean print;

    //~ Constructors .................................................................................................................................

    private DownloadResponse() {
        location = "";
        print    = false;
    }

    public DownloadResponse(String l, boolean p) {
        location = l;
        print    = p;
    }

    //~ Methods ......................................................................................................................................

    /** URL location. */
    public String getLocation() {
        return location;
    }

    /** Should popup for print or not? */
    public boolean isPrint() {
        return print;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7562654511841987618L;
}
