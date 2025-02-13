
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import javax.inject.Named;

import tekgenesis.common.env.Mutable;

/**
 * Ix properties.
 */
@Mutable
@Named("ix")
public class IxProps {

    //~ Instance Fields ..............................................................................................................................

    /**
     * Timeout value, in milliseconds, to be used when opening a communications link to a resource.
     * Zero value implies that the option is disabled (i.e., timeout of infinity).
     */
    @SuppressWarnings("MagicNumber")
    public int connectTimeout = 2000;

    public MODE mode         = MODE.READ;
    @SuppressWarnings("MagicNumber")
    public int  pingInterval = 300000;
    @SuppressWarnings("MagicNumber")
    public int  pingTimeout = 200;

    /**
     * Timeout value, in milliseconds, for reading from input stream when a connection is
     * established. Zero value implies that the option is disabled (i.e., timeout of infinity).
     */
    public int timeout;

    public String  url                  = null;
    public boolean validateQueryIndexes = false;

    //~ Enums ........................................................................................................................................

    public enum MODE { READ, WRITE }
}
