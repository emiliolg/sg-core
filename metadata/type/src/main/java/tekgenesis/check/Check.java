
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.check;

import java.io.Serializable;
import java.util.ArrayList;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.expr.Expression;
import tekgenesis.expr.RefTypeSolver;

/**
 * Check class containing validation expression and type.
 */
@SuppressWarnings("FieldMayBeFinal")
public class Check implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private Expression expr;
    private CheckMsg   msg;

    //~ Constructors .................................................................................................................................

    /** Default contructor. */
    public Check() {
        expr = null;
        msg  = null;
    }

    /** Check contains expression and message. */
    private Check(Expression expr, CheckMsg msg) {
        this.expr = expr;
        this.msg  = msg;
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        final String exprStr = expr.toString();
        final String result  = exprStr + " : " + msg.toString();
        return exprStr.charAt(0) == '(' ? "(" + result + ")" : result;
    }

    /** Returns the expression. */
    public Expression getExpr() {
        return expr;
    }

    /** Returns the Message. */
    public CheckMsg getMsg() {
        return msg;
    }

    /** Returns the Message Text. */
    public String getMsgText() {
        return msg.getText();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6074547146845311574L;

    //~ Inner Classes ................................................................................................................................

    public static class List extends ArrayList<Check> {
        private List() {}

        /** Add a check expression. */
        public List addCheck(Expression expression, CheckMsg checkMsg) {
            final List result = this == EMPTY ? new List() : this;
            result.add(new Check(expression, checkMsg));
            return result;
        }

        /** Compile the Check.List. */
        public List compile(RefTypeSolver solver) {
            List result = EMPTY;
            for (final Check check : this)
                result = result.addCheck(check.getExpr().compile(solver), check.getMsg());
            return result;
        }

        /** Serialize the List. */
        public void serialize(StreamWriter w) {
            w.writeInt(size());
            for (final Check check : this) {
                check.getExpr().serialize(w);
                check.getMsg().serialize(w);
            }
        }

        @Override public String toString() {
            if (isEmpty()) return "";
            return size() == 1 ? get(0).toString() : Colls.mkString(this, "(", ", ", ")");
        }

        /** Instantiate the List from an Stream. */
        public static List instantiate(StreamReader r) {
            final int n      = r.readInt();
            List      result = EMPTY;
            for (int i = 0; i < n; i++)
                result = result.addCheck(Expression.instantiate(r), CheckMsg.instantiate(r));
            return result;
        }

        private static final long serialVersionUID = -9093446416935058253L;

        public static final List EMPTY = new List();
    }  // end class List
}  // end class Check
