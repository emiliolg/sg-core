
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.documentation;

/**
 * Exception thrown when mustache templates are not found.
 */
public class MMDocGenerationException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** @param  templateDir:  template that was not found */
    public MMDocGenerationException(String templateDir) {
        super("Could not find mustache template : " + templateDir);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6661452118396822599L;
}
