
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

/**
 * Exception to throw when a parameter collides on a route.
 */
public class ParameterCollidingException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new invalid path exception. */
    private ParameterCollidingException(String parameter, String modelName, String msg) {
        super(String.format(msg, parameter), modelName);
    }

    //~ Methods ......................................................................................................................................

    /** Create exception for parameter colliding with existing parameter. */
    public static ParameterCollidingException parameterCollidingParameter(String name, String path) {
        return new ParameterCollidingException(name, path, "Parameter '%s' already defined.");
    }

    /** Create exception for parameter colliding with existing dynamic part. */
    public static ParameterCollidingException parameterCollidingRoute(String name, String path) {
        return new ParameterCollidingException(name, path, "Parameter '%s' collides with route section.");
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
