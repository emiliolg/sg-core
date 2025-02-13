
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import static tekgenesis.common.core.Constants.DEFAULT_STRING_LENGTH;

/**
 * Specify the SQL name for a persistent property or field.
 *
 * <p>Examples:</p>
 *
 * <blockquote>
 * <pre>
   &#064;Column("NAME")
   private String nm;
 * </pre>
 * </blockquote>
 *
 * @author  emilio
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface DbColumn {
    /**
     * @return  column name
     */
    String value();

    /**
     * (Optional) The column length.
     */
    int length() default DEFAULT_STRING_LENGTH;
}
