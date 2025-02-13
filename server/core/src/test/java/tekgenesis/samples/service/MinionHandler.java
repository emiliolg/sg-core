
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.samples.service;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

/**
 * User class for Handler: MinionHandler
 */
public class MinionHandler extends MinionHandlerBase {

    //~ Constructors .................................................................................................................................

    MinionHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/service/all". */
    @NotNull @Override public Result<Seq<Minion>> all() {
        final Seq<Minion> seq = ImmutableList.of(minion("Dave"), minion("Carl"), minion("Jerry"), minion("Kevin"));
        return ok(seq);
    }

    @NotNull @Override public Result<Minion> some(@NotNull String name) {
        return ok(minion(name));
    }

    private Minion minion(String name) {
        final Minion minion = new Minion();
        minion.setName(name);
        minion.setMood(false);
        return minion;
    }
}
