
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.serializer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Set;

import org.infinispan.commons.marshall.AdvancedExternalizer;
import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.database.CacheSerializer;
import tekgenesis.cache.database.infinispan.IsStreamReader;
import tekgenesis.cache.database.infinispan.IsStreamWriter;
import tekgenesis.common.collections.Colls;
import tekgenesis.metadata.form.model.FormModel;

import static tekgenesis.common.Predefined.cast;

/**
 * A Infinispan Serializer that uses FormModel serializers.
 */
public class ISFormModelSerializer implements AdvancedExternalizer<FormModel>, CacheSerializer {

    //~ Methods ......................................................................................................................................

    @Override public FormModel readObject(ObjectInput input)
        throws IOException, ClassNotFoundException
    {
        final IsStreamReader reader = new IsStreamReader(input);
        final FormModel      model  = FormModel.instantiate(reader);
        model.deserialize(reader, true);
        return model;
    }

    @Override public void writeObject(ObjectOutput output, FormModel model)
        throws IOException
    {
        model.serialize(new IsStreamWriter(output), true);
    }

    @Override public AdvancedExternalizer<?> getExternalizer() {
        return this;
    }

    @NotNull @Override public Integer getId() {
        return SERIALIZER_TYPE_ID;
    }

    @Override public Set<Class<? extends FormModel>> getTypeClasses() {
        return cast(Colls.set(FormModel.class));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -9077389907695739988L;

    private static final int SERIALIZER_TYPE_ID = 101;
}
