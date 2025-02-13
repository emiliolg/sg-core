
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model.g;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.persistence.nomm.model.Client;

import static tekgenesis.persistence.nomm.model.ClientTable.CLIENT;

/**
 * Test Class.
 */
public class ClientBase implements PersistableInstance<Client, Integer> {

    //~ Instance Fields ..............................................................................................................................

    private int      id         = 0;
    private String   mail       = "";
    private String   name       = "";
    @SuppressWarnings("FieldMayBeFinal")
    private DateTime updateTime = DateTime.EPOCH;

    //~ Methods ......................................................................................................................................

    @NotNull public EntityTable<Client, Integer> et() {
        return myEntityTable();
    }

    @NotNull @Override public String keyAsString() {
        return String.valueOf(id);
    }

    @NotNull @Override public Integer keyObject() {
        return id;
    }

    @Override public DbTable<Client, Integer> table() {
        return CLIENT;
    }

    public int getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }

    //~ Methods ......................................................................................................................................

    public static Client create(int id) {
        final Client c = new Client();
        ((ClientBase) c).id = id;
        return c;
    }

    /** Javadoc. */
    @Nullable public static Client find(int key) {
        return myEntityTable().find(key);
    }

    private static EntityTable<Client, Integer> myEntityTable() {
        return EntityTable.forTable(CLIENT);
    }
}  // end class ClientBase
// end class Client
