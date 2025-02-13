
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.resource;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * An In Memory implementation of a Resource useful for the GWT side.
 */
public class SimpleResourceImpl extends AbstractResource {

    //~ Instance Fields ..............................................................................................................................

    private Map<String, Entry> entries = null;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("UnusedDeclaration")
    private SimpleResourceImpl() {}

    /** Create Simple Resource with uuid. */
    public SimpleResourceImpl(String uuid) {
        this(uuid, new HashMap<>());
    }
    /** Create Simple Resource with uuid. */
    public SimpleResourceImpl(String uuid, Map<String, Entry> entries) {
        super(uuid);
        this.entries = new HashMap<>();
        for (final Map.Entry<String, Entry> stringEntryEntry : entries.entrySet()) {
            final Entry value = stringEntryEntry.getValue();
            this.entries.put(stringEntryEntry.getKey(),
                new EntryImpl(value.getVariant(), value.isExternal(), value.getName(), value.getUrl(), value.getMimeType(), value.getMetadata()));
        }
    }

    //~ Methods ......................................................................................................................................

    @Override public Factory addLarge() {
        throw new IllegalStateException();
    }

    @Override public Factory addThumb() {
        throw new IllegalStateException();
    }

    @Override public Factory addVariant(String variant) {
        throw new IllegalStateException();
    }

    @Override public SimpleResourceImpl asSimple() {
        return this;
    }

    @NotNull @Override protected Map<String, Entry> entries() {
        return entries;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5126448534852478362L;
}  // end class SimpleResourceImpl
