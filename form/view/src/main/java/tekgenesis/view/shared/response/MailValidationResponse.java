
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

public class MailValidationResponse implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private MailValidationError validationError;

    //~ Constructors .................................................................................................................................

    public MailValidationResponse() {
        validationError = null;
    }

    public MailValidationResponse(MailValidationError validationError) {
        this();
        this.validationError = validationError;
    }

    //~ Methods ......................................................................................................................................

    public MailValidationError getValidationError() {
        return validationError;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 2135872617619182304L;

    //~ Enums ........................................................................................................................................

    public enum MailValidationError { NONE, DOMAIN, ADDRESS, CONNECTION }
}
