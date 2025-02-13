
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database.infinispan;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Set;

import org.infinispan.commons.marshall.AdvancedExternalizer;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource;
import tekgenesis.type.resource.AbstractResource;

import static tekgenesis.common.Predefined.cast;

/**
 * Some Default Serializers for our classes.
 */
interface Serializers {

    //~ Instance Fields ..............................................................................................................................

    int                            DATE_ONLY_SERIALIZER_ID = 102;
    AdvancedExternalizer<DateOnly> DATE_ONLY_SERIALIZER    = new AdvancedExternalizer<DateOnly>() {
            private static final long serialVersionUID = -3694546950480005366L;

            @Override public Set<Class<? extends DateOnly>> getTypeClasses() {
                return cast(Colls.set(DateOnly.class));
            }

            @Override public Integer getId() {
                return DATE_ONLY_SERIALIZER_ID;
            }

            @Override public void writeObject(ObjectOutput output, DateOnly d)
                throws IOException
            {
                output.writeLong(d.toMilliseconds());
            }

            @Override public DateOnly readObject(ObjectInput input)
                throws IOException
            {
                return DateOnly.fromMilliseconds(input.readLong());
            }
        };

    int DATE_TIME_SERIALIZER_ID = 103;

    AdvancedExternalizer<DateTime> DATE_TIME_SERIALIZER = new AdvancedExternalizer<DateTime>() {
            private static final long serialVersionUID = -5647318857115307160L;

            @Override
            @SuppressWarnings("TypeParameterExtendsFinalClass")
            public Set<Class<? extends DateTime>> getTypeClasses() {
                return cast(Colls.set(DateTime.class));
            }

            @Override public Integer getId() {
                return DATE_TIME_SERIALIZER_ID;
            }

            @Override public void writeObject(ObjectOutput output, DateTime dt)
                throws IOException
            {
                output.writeLong(dt.toMilliseconds());
            }

            @Override public DateTime readObject(ObjectInput input)
                throws IOException
            {
                return DateTime.fromMilliseconds(input.readLong());
            }
        };

    int RESOURCE_SERIALIZER_ID = 104;

    AdvancedExternalizer<Resource> RESOURCE_SERIALIZER = new AdvancedExternalizer<Resource>() {
            private static final long serialVersionUID = 1300153379816229355L;

            @Override public Set<Class<? extends Resource>> getTypeClasses() {
                return cast(Colls.set(Resource.class));
            }

            @Override public Integer getId() {
                return RESOURCE_SERIALIZER_ID;
            }

            @Override public void writeObject(ObjectOutput output, Resource object) {
                object.serialize(new IsStreamWriter(output));
            }

            @Override public Resource readObject(ObjectInput input) {
                return AbstractResource.instantiate(new IsStreamReader(input));
            }
        };
}  // end interface Serializers
