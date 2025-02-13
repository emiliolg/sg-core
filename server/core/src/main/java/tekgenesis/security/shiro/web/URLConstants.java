
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.security.shiro.web;

import tekgenesis.common.core.Constants;

/**
 * URL constants.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public interface URLConstants {

    //~ Instance Fields ..............................................................................................................................

    String DEFAULT_FAVICON            = "/public/img/favicon.png";
    String DEFAULT_TEKGENESIS_FAVICON = "/public/sg/img/favicon.png";

    String DEFAULT_TEKGENESIS_THEME = "/public/sg/css/theme/tekgenesis.css";

    String DEFAULT_THEME = "/public/css/theme.css";
    String FORMS_CSS     = "public/css/forms.css";
    String FORMS_LESS    = "public/css/forms.less";

    String INDEX_URI            = "/";
    String INTERFACE_TYPE_PROXY = "tekgenesis.html.context";
    String JSESSIONID           = ";JSESSIONID=";
    String LOCALE_PARAM         = "locale";
    String LOGIN_URI            = Constants.LOGIN_URI;
    String LOGOUT_URI           = "/sg/logout";
    String RESET_IP_FILTERS     = "resetIPFilters";
    String REST_API_URI         = "/sg/restapi";
    String STYLE_CSS            = "css/style.css";
    String SUCCESS_URI          = "/sg/success";
    String SUIGENERIS_CSS       = "/public/sg/css/suigeneris.css";
    String UNAUTHORIZED_URI     = "/sg/unauthorized";
}
