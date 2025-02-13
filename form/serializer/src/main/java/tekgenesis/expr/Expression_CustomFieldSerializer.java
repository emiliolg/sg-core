
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import tekgenesis.common.serializer.SerializerException;
import tekgenesis.serializer.GwtSerializationExceptionFactory;
import tekgenesis.serializer.GwtStreamReader;
import tekgenesis.serializer.GwtStreamWriter;

/**
 * Compiled ExpressionAST Serializer.
 */
@SuppressWarnings("WeakerAccess")  // GWT
public class Expression_CustomFieldSerializer extends CustomFieldSerializer<Expression> {

    //~ Methods ......................................................................................................................................

    @Override public void deserializeInstance(SerializationStreamReader streamReader, Expression instance)
        throws SerializationException
    {
        deserialize(streamReader, instance);
    }

    @Override public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override public Expression instantiateInstance(SerializationStreamReader r)
        throws SerializationException
    {
        return instantiate(r);
    }

    @Override public void serializeInstance(SerializationStreamWriter w, Expression e)
        throws SerializationException
    {
        serialize(w, e);
    }

    //~ Methods ......................................................................................................................................

    /** De-serialize the object.. Not implemented because the code is already in instantiate */
    @SuppressWarnings({ "UnusedParameters", "EmptyMethod" })
    public static void deserialize(SerializationStreamReader streamReader, Expression instance) {
        // nothing more to do
    }

    /** Instantiate an Expression from the stream. */
    public static Expression instantiate(SerializationStreamReader reader)
        throws SerializationException
    {
        try {
            return Expression.instantiate(new GwtStreamReader(reader));
        }
        catch (final SerializerException e) {
            throw GwtSerializationExceptionFactory.create(e);
        }
    }

    /** Serialize an Expression to the stream. */
    public static void serialize(SerializationStreamWriter w, Expression e)
        throws SerializationException
    {
        try {
            e.serialize(new GwtStreamWriter(w));
        }
        catch (final SerializerException ex) {
            throw GwtSerializationExceptionFactory.create(ex);
        }
    }
}  // end class Expression_CustomFieldSerializer
