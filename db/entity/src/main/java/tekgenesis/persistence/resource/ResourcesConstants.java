
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.resource;

import static tekgenesis.common.core.Strings.quoted;

/**
 * Constants to define Resources tables.
 */
public interface ResourcesConstants {

    //~ Instance Fields ..............................................................................................................................

    String BINARY_DATA   = quoted("BINARY_DATA");
    String CONTENT_TABLE = "RESOURCE_CONTENT";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    String EXTERNAL = quoted("EXTERNAL");

    String INDEX_TABLE = "RESOURCE_INDEX";
    String INFO        = quoted("INFO");
    String MIME_TYPE   = quoted("MIME_TYPE");
    String NAME        = quoted("NAME");

    // Content Table fields

    String SHA       = quoted("SHA");
    String SIZE      = quoted("SIZE");
    String TEXT_DATA = quoted("TEXT_DATA");
    String URL       = quoted("URL");

    // Index Table Fields

    String UUID    = quoted("UUID");
    String VARIANT = quoted("VARIANT");
}
