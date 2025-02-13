
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

/**
 * An Instruction for the Expression evaluator.
 */
public enum Instruction implements Code {

    //~ Enum constants ...............................................................................................................................

    ADD_DATE_INT, ADD_DEC, ADD_INT, ADD_REAL, ADD_TIME_INT, AND, AVG_DEC, AVG_INT, AVG_REAL, CAT, COUNT, DEC_TO_INT, DEC_TO_LONG, DEC_TO_REAL,
    DIV_DEC, DIV_INT, DIV_REAL, EQ, EQ_NUM, FUN, GE, GT, INT_TO_DEC, INT_TO_LONG, INT_TO_REAL, LE, ASSIGNMENT, LIST, LT, MAX, MIN, MINUS_DEC,
    MINUS_INT, MINUS_REAL, MOD_INT, MULT_DEC, MULT_INT, MULT_REAL, NE, NE_NUM, NOP, NOT, OR, PUSH, REAL_TO_DEC, REAL_TO_INT, REAL_TO_LONG, REF, ROWS,
    SIZE, STR_TO_BOOL, STR_TO_DATE, STR_TO_DEC, STR_TO_INT, STR_TO_REAL, STR_TO_TIME, SUB_DATE, SUB_DATE_INT, SUB_DEC, SUB_INT, SUB_REAL, SUM_DEC,
    SUM_INT, SUM_REAL, SUM_BOOL, SUB_TIME, SUB_TIME_INT, TO_STR, TO_DATE_STR, TO_TIME_STR, IF, ELSE, FORBIDDEN, DATE_TIME_TO_DATE, DATE_TO_DATE_TIME,
    IS_UPDATE, IS_READ_ONLY;

    //~ Methods ......................................................................................................................................

    @Override public void bind(Binder binder) {}

    @Override public Instruction getInstruction() {
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the instruction corresponding to this ordinal. */
    public static Instruction valueOf(int ordinal) {
        return VALUES[ordinal];
    }

    //~ Static Fields ................................................................................................................................

    private static final Instruction[] VALUES = values();
}
