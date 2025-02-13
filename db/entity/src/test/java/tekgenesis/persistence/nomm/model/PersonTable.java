
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.CacheType;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField;
import tekgenesis.type.Modifier;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * Test class.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
public class PersonTable extends DbTable<Person, Tuple<Integer, String>> {

    //~ Instance Fields ..............................................................................................................................

    public final TableField.Date BIRTHDAY;
    public final TableField.Str  DOC_CODE;
    public final TableField.Int  DOC_TYPE;
    public final TableField.Str  FIRST_NAME;
    public final TableField.Str  LAST_NAME;

    public final TableField.Decimal           SALARY;
    public final TableField.Enum<Sex, String> SEX;

    public final TableField.DTime UPDATE_TIME;

    //~ Constructors .................................................................................................................................

    PersonTable() {
        super(Person.class, "MODEL", "PERSON", "", Modifier.NONE, CacheType.NONE);
        FIRST_NAME  = strField("firstName", "FIRST_NAME", 30);
        LAST_NAME   = strField("lastName", "LAST_NAME", 30);
        DOC_TYPE    = intField("docType", "DOC_TYPE");
        DOC_CODE    = strField("docCode", "DOC_CODE", 30);
        BIRTHDAY    = dateField("birthday", "BIRTHDAY");
        SALARY      = decimalField("salary", "SALARY", true, 10, 2);
        SEX         = enumField("sex", "SEX", Sex.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(DOC_TYPE, DOC_CODE));
        secondaryKeys(listOf(listOf(DOC_CODE, DOC_TYPE)));
    }

    //~ Methods ......................................................................................................................................

    @Override public PersonTable as(final String alias) {
        return createAlias(new PersonTable(), alias);
    }

    @Override protected EntityTable<Person, Tuple<Integer, String>> createEntityTable() {
        return new EntityTable<>(PERSON);
    }

    @Override protected Tuple<Integer, String> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 2);
        return Tuple.tuple(Integer.valueOf(parts[0]), parts[1]);
    }

    //~ Static Fields ................................................................................................................................

    public static final PersonTable PERSON = new PersonTable();
}  // end class PersonTable
