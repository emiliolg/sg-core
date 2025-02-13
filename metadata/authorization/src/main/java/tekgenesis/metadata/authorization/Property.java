
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.authorization;

/**
 * Represents a session property.
 */
public interface Property {

    //~ Methods ......................................................................................................................................

    /** Returns the value of the property as an Boolean. */
    Boolean asBoolean();

    /** Returns the value of the property as an Integer. */
    Integer asInt();

    /** Returns the value of the property as an String. */
    String asString();

    /** Returns the id of the property. */
    String getId();

    /** Returns the name of the property. */
    String getName();

    /** Returns the value of the property. */
    Object getValue();

    /** Sets the value of the property. */
    void setValue(Object value);
}
