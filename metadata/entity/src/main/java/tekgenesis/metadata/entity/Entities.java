
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import tekgenesis.common.collections.Seq;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModelKind;

/**
 * Some Utility Methods.
 */
@SuppressWarnings("WeakerAccess")
public class Entities {

    //~ Constructors .................................................................................................................................

    private Entities() {}

    //~ Methods ......................................................................................................................................

    /** Retrieve all Domains that contain Entities in a give repository. */
    public static Seq<String> retrieveDomainsWithEntities(final ModelRepository repository) {  //
        return repository.getDomains().filter(s -> repository.getModels(s).exists(mm -> mm != null && mm.getMetaModelKind() == MetaModelKind.ENTITY));
    }
}  // end class Entities
