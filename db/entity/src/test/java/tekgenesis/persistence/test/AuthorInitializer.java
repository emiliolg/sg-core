
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import tekgenesis.persistence.EntityInitializer;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.test.basic.Author;

import static tekgenesis.persistence.EntityListenerType.AFTER_PERSIST;

/**
 * An EntityInitializer class.
 */
@SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
public class AuthorInitializer implements EntityInitializer {

    //~ Methods ......................................................................................................................................

    @Override public void initialize() {
        Author.addListener(AFTER_PERSIST, instance -> {
                authorPersist++;
                return true;
            });
    }

    @Override public Class<? extends EntityInstance<?, ?>> getEntityType() {
        return Author.class;
    }

    //~ Methods ......................................................................................................................................

    public static void reset() {
        authorPersist = 0;
    }

    //~ Static Fields ................................................................................................................................

    public static int authorPersist = 0;
}
