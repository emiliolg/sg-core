
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

/**
 * Metadata Columns.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public enum MdColumn {

    //~ Enum constants ...............................................................................................................................

    /** Constants for getSchemas. */
    S_CATALOG("TABLE_CATALOG"), S_NAME("TABLE_SCHEM"),

    /** Constants for getTables. */

    T_NAME("TABLE_NAME"), T_TYPE("TABLE_TYPE"), T_REMARKS("REMARKS"),

    /** Constants for getColumns. */
    C_CATALOG("TABLE_CAT"), C_DATA_TYPE("DATA_TYPE"), C_DEFAULT("COLUMN_DEF"), C_GENERATED_COLUMN("IS_GENERATEDCOLUMN"), C_IS_NULLABLE("IS_NULLABLE"),
    C_NAME("COLUMN_NAME"), C_NULLABLE("NULLABLE"), C_POSITION("ORDINAL_POSITION"), C_SCHEMA("TABLE_SCHEM"), C_SIZE("COLUMN_SIZE"),
    C_AUTO_INCREMENT("IS_AUTOINCREMENT"), C_TABLE("TABLE_NAME"), C_TYPE_NAME("TYPE_NAME"), C_REMARKS("REMARKS"), C_SEQUENCE("SEQUENCE_NAME"),
    ORA_PRECISION,

    /** Constants for metadata.getPrimaryKeys. */
    PK_NAME("PK_NAME"), PK_COLUMN_NAME("COLUMN_NAME"),

    /** Constants for metadata.getIndexInfo. */
    IX_NAME("INDEX_NAME"), IX_COLUMN_NAME("COLUMN_NAME"), IX_TYPE("TYPE"), IX_NON_UNIQUE("NON_UNIQUE"), IX_QUALIFIER("INDEX_QUALIFIER"),
    IX_POSITION("ORDINAL_POSITION"), IX_SORT("ASC_OR_DESC"), IX_PAGES("PAGES"), PK_COL_SEQ("KEY_SEQ"), C_DECIMAL_DIGITS("DECIMAL_DIGITS"),

    /** Constants for metadata.getImportedKeys. */

    FK_NAME("FK_NAME"), FK_PK_SCHEMA("PKTABLE_SCHEM"), FK_PK_TABLE("PKTABLE_NAME"), FK_COLUMN_NAME("FKCOLUMN_NAME"),
    FK_PK_COLUMN_NAME("PKCOLUMN_NAME"),

    /** Constants for getSequences. */
    SEQ_NAME, SEQ_START, SEQ_MIN, SEQ_MAX, SEQ_INC, SEQ_CYCLE, SEQ_CACHE, SEQ_LAST,

    /** Constants for getConstraints. */
    CONSTRAINT_NAME, CHECK_CONDITION, CONSTRAINT_ENABLED;

    //~ Instance Fields ..............................................................................................................................

    private final String columnName;

    //~ Constructors .................................................................................................................................

    MdColumn() {
        columnName = name();
    }

    MdColumn(final String columnName) {
        this.columnName = columnName;
    }

    //~ Methods ......................................................................................................................................

    String getColumnName() {
        return columnName;
    }
}  // end interface MetadataColumns
