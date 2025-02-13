
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
import tekgenesis.common.core.Tuple3;
import tekgenesis.common.core.Tuple4;
import tekgenesis.persistence.*;
import tekgenesis.persistence.nomm.model.AddressPhone;
import tekgenesis.persistence.nomm.model.PersonAddress;

import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.persistence.nomm.model.AddressPhoneTable.ADDRESS_PHONE;
import static tekgenesis.persistence.nomm.model.PersonAddressTable.PERSON_ADDRESS;

@SuppressWarnings("FieldMayBeFinal")
public class AddressPhoneBase
    implements InnerInstance<AddressPhone, Tuple4<Integer, String, Integer, Integer>, PersonAddress, Tuple3<Integer, String, Integer>>,
               PersistableInstance<AddressPhone, Tuple4<Integer, String, Integer, Integer>>
{

    //~ Instance Fields ..............................................................................................................................

    public long instanceVersion;

    final EntityRef<PersonAddress, Tuple3<Integer, String, Integer>> personAddress = new EntityRef<>(PERSON_ADDRESS);
    private int                                                      addressSeqId  = 0;
    private String                                                   docCode       = "";
    private int                                                      docType       = 0;
    private int                                                      phone         = 0;
    private int                                                      seqId         = 0;
    private DateTime                                                 updateTime    = DateTime.EPOCH;

    //~ Methods ......................................................................................................................................

    @NotNull public EntityTable<AddressPhone, Tuple4<Integer, String, Integer, Integer>> et() {
        return myEntityTable();
    }

    public long incrementVersion() {
        return instanceVersion++;
    }
    @NotNull @Override public String keyAsString() {
        return docType + ":" + docCode + ":" + addressSeqId + ":" + seqId;
    }

    @NotNull @Override public Tuple4<Integer, String, Integer, Integer> keyObject() {
        return tuple(docType, docCode, addressSeqId, seqId);
    }

    @NotNull @Override public EntityRef<PersonAddress, Tuple3<Integer, String, Integer>> parent() {
        return personAddress;
    }

    @Override public int seqId() {
        return getSeqId();
    }

    @NotNull @Override public InnerEntitySeq<AddressPhone> siblings() {
        return getPersonAddress().getPhones();
    }

    @Override public DbTable<AddressPhone, Tuple4<Integer, String, Integer, Integer>> table() {
        return ADDRESS_PHONE;
    }

    public int getAddressSeqId() {
        return addressSeqId;
    }

    public String getDocCode() {
        return docCode;
    }

    public int getDocType() {
        return docType;
    }

    @Override public long getInstanceVersion() {
        return instanceVersion;
    }

    @NotNull public PersonAddress getPersonAddress() {
        return personAddress.solveOrFail(tuple(docType, docCode, addressSeqId));
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
    public int getSeqId() {
        return seqId;
    }

    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }

    //~ Methods ......................................................................................................................................

    @Nullable public static AddressPhone find(int docType, String docCode, int addressSeqId, int seqId) {
        return myEntityTable().find(tuple(docType, docCode, addressSeqId, seqId));
    }

    private static EntityTable<AddressPhone, Tuple4<Integer, String, Integer, Integer>> myEntityTable() {
        return EntityTable.forTable(ADDRESS_PHONE);
    }
}  // end class AddressPhoneBase
