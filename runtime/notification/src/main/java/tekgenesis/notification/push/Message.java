
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.notification.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;

/**
 * Message.
 */
@SuppressWarnings("ClassEscapesDefinedScope")
public class Message<T> {
    //J-
    @JsonProperty("registration_ids")
    public Seq<String> tos = Colls.emptyList();
    @JsonProperty("data")
    public T           content = null;
    //J+
}
