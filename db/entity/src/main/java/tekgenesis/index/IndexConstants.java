
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import static tekgenesis.common.core.Strings.quoted;

/**
 * Index constants.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public interface IndexConstants {

    //~ Instance Fields ..............................................................................................................................

    String DEPRECATED_FIELD = "_deprecated";

    String DESCRIBED_BY_FIELD = "_described_by";
    String DYNAMIC            = "dynamic";

    String ENTITY_FIELD     = quoted("ENTITY");
    String IMAGE_PATH_FIELD = "_image_path";

    String INDEX             = "index";
    String INDEX_DEPRECATED  = "index_deprecated";
    String INDEX_UPDATE_TIME = "index_update_time";
    String INTEGER_TYPE      = "integer";

    String LAST_DELETED = "LAST_DELETED";

    String LOCAL_NODE_NAME = "Local";

    int    MAX_ENTITY_LENGTH = 128;
    String PROPERTIES        = "properties";

    String STRING_TYPE     = "string";
    String TO_STRING_FIELD = "_to_string";
    String TS_FIELD        = quoted("TS");
}
