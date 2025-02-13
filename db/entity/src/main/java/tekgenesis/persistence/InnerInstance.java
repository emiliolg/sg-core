
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import org.jetbrains.annotations.NotNull;

/**
 * Inner Entities Instances implements this interface.
 *
 * @param  This  The Class of this Instance
 * @param  K     The Key of this Instance
 * @param  P     the parent instance of this instance
 * @param  PK    the key of the parent instance
 */
public interface InnerInstance<This extends InnerInstance<This, K, P, PK>, K, P extends EntityInstance<P, PK>, PK>
    extends PersistableInstance<This, K>
{

    //~ Methods ......................................................................................................................................

    /** Get the parent Reference. */
    @NotNull EntityRef<P, PK> parent();
    /** Get the sequence Id field. */
    int seqId();
    /** Get all my siblings and myself. */
    @NotNull EntitySeq<This> siblings();
}
