
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

/**
 * Used to specified the SQL names for an embedded object.
 *
 * <p>Examples:</p>
 *
 * <blockquote>
 * <pre>
   &#064;Columns({
        &#064;Column("ID_TYPE"),
        &#064;Column("ID_VALUE")
   })
   private Id id;

   &#064;Columns(prefix="PARENT_ID_")
   private Id parentId;  // Assume Id has there own &#064;Column annotations and prefix the names
 * </pre>
 * </blockquote>
 *
 * @author  emilio
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface DbColumns {
    /**
     * @return  columns
     */
    DbColumn[] value() default {};
    /**
     * @return  prefix for column names
     */
    String prefix() default "";
}
