
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro.social;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

import tekgenesis.common.json.JsonMapping;

/**
 * Facebook User Details.
 */
public class FacebookUserDetails {

    //~ Instance Fields ..............................................................................................................................

    private final String email;
    private final String firstName;
    private final String id;

    private final String jsonString;
    private final String lastName;

    //~ Constructors .................................................................................................................................

    /** Constructor. */
    public FacebookUserDetails(String fbResponse) {
        jsonString = fbResponse;
        try {
            final JsonNode respjson = JsonMapping.shared().readTree(fbResponse);
            id        = respjson.get("id").textValue();
            firstName = respjson.has(FIRST_NAME) ? respjson.get(FIRST_NAME).textValue() : " no name" + id;
            lastName  = respjson.has(LAST_NAME) ? respjson.get(LAST_NAME).textValue() : "";
            // noinspection DuplicateStringLiteralInspection
            if (!respjson.has(EMAIL)) throw new IllegalStateException("Cannot get email for facebook user: " + id);
            email = respjson.get(EMAIL).textValue();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    //~ Methods ......................................................................................................................................

    public String toString() {
        return jsonString;
    }

    /** Return email. */
    public String getEmail() {
        return email;
    }

    /** Return firstname. */
    public String getFirstName() {
        return firstName;
    }

    /** Return id. */
    public String getId() {
        return id;
    }

    /** Return lastname. */
    public String getLastName() {
        return lastName;
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String EMAIL = "email";

    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME  = "last_name";
}  // end class FacebookUserDetails
