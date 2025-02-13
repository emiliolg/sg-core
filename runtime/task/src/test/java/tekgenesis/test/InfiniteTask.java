
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test;

import java.math.BigInteger;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.Status;

/**
 * User class for Task: InfiniteTask
 */
public class InfiniteTask extends InfiniteTaskBase<Integer> {

    //~ Constructors .................................................................................................................................

    private InfiniteTask(@NotNull ScheduledTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @Override public Seq<Integer> enumerate() {
        final ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < 50; i++)
            list.add(i);
        return Colls.immutable(list);
    }

    @Override public Status process(Integer i) {
        System.out.println(i + ": " + fib(new BigInteger(String.valueOf(i))));
        return Status.ok();
    }

    //~ Methods ......................................................................................................................................

    private static BigInteger fib(BigInteger n) {
        if (n.compareTo(BigInteger.ONE) == -1 || n.compareTo(BigInteger.ONE) == 0) return n;
        else return fib(n.subtract(BigInteger.ONE)).add(fib(n.subtract(BigInteger.ONE).subtract(BigInteger.ONE)));
    }
}
