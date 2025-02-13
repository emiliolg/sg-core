
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.cart;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.authorization.User;
import tekgenesis.sales.cart.g.CartBase;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * User class for Entity: Cart
 */
@SuppressWarnings("WeakerAccess")
public class Cart extends CartBase {

    //~ Methods ......................................................................................................................................

    /** Current cart primary key. */
    @NotNull public static Cart current(@NotNull final User user) {
        return invokeInTransaction(() -> notNull(findByUser(user.getId()), () -> create().setUser(user.getId()).insert()));
    }
}
