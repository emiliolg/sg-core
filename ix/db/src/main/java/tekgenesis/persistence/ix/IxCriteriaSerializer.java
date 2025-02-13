
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.ix;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.logging.Logger;
import tekgenesis.persistence.Criteria;
import tekgenesis.persistence.TableField;
import tekgenesis.persistence.expr.Const;
import tekgenesis.persistence.expr.ExprOperator;
import tekgenesis.persistence.expr.ExprVisitor;

import static java.lang.String.format;

import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.core.Strings.unquote;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 */
class IxCriteriaSerializer implements ExprVisitor<String> {

    //~ Instance Fields ..............................................................................................................................

    private final ObjectMapper mapper;

    //~ Constructors .................................................................................................................................

    IxCriteriaSerializer() {
        mapper = JsonMapping.shared();
    }

    //~ Methods ......................................................................................................................................

    @Override public String visit(TableField<?> e) {
        return encode(e.getFieldName());
    }

    @Override public String visit(Const<?> e) {
        // Serialize the Value to a Json String
        try {
            return encode(unquote(mapper.writeValueAsString(e.getValue())));
        }
        catch (final IOException e1) {
            logger.error(e1);
        }
        return "";
    }

    @Override public String visit(final ExprOperator operator, final Object[] operands) {
        switch (operator) {
        case AND:
            return operands[0] + "&" + operands[1];
        case OR:
            return operands[0] + encode("||") + operands[1];
        case EQ:
            return operands[0] + "=" + operands[1];
        case IS_NULL:
            return operands[0] + "=";
        default:
            throw new UnsupportedOperationException(format("Operator '%s' is not supported.", operator));
        }
    }

    String accept(final Criteria[] where) {
        final StrBuilder result = new StrBuilder().startCollection("&");
        for (final Criteria c : where)
            if (!c.isConst()) result.appendElement(c.accept(this));  // skip const criteria like Empty,TRUE or FALSE.
        return result.toString();
    }

    //~ Methods ......................................................................................................................................

    private static String encode(final String s) {
        try {
            return URLEncoder.encode(s, UTF8);
        }
        catch (final UnsupportedEncodingException ignore) {
            return s;
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(IxCriteriaSerializer.class);
}  // end class IxCriteriaSerializer
