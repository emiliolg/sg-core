
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.math.BigDecimal;

import org.junit.Test;

import tekgenesis.persistence.Criteria;
import tekgenesis.persistence.expr.Expr;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.persistence.Criteria.EMPTY;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.persistence.expr.Expr.constant;
import static tekgenesis.test.basic.g.BasicTypesTable.BASIC_TYPES;
import static tekgenesis.test.basic.g.CartItemTable.CART_ITEM;
import static tekgenesis.test.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.test.basic.g.ProductTable.PRODUCT;

@SuppressWarnings({ "JavaDoc", "MagicNumber" })
public class ExprTest {

    //~ Methods ......................................................................................................................................

    @Test public void testCases() {
        // simple case
        //J-
        assertThat(select(PRODUCT.PRICE,
                          PRODUCT.PRICE.when(BigDecimal.ONE).then(PRODUCT.PRICE)
                                       .when(BigDecimal.TEN).then(PRODUCT.PRICE.negate())
                                       .otherwise(BigDecimal.ZERO))
                           .from(PRODUCT)
                           .asSql())
            .isEqualTo("select PRICE, case PRICE when 1 then PRICE when 10 then -(PRICE) else 0 end\nfrom QName(BASIC_TEST, PROD)\n");

        assertThat(select(
                PRODUCT.PRICE.ge(BigDecimal.ZERO).then(PRODUCT.PRICE).otherwise(PRODUCT.PRICE.negate()))
                        .from(PRODUCT)
                        .asSql())
                .isEqualTo("select case  when PRICE >= 0 then PRICE else -(PRICE) end\nfrom QName(BASIC_TEST, PROD)\n");

        assertThat(select(PRODUCT.PRICE,
                          PRODUCT.PRICE.when(BigDecimal.ONE).then("this").when(BigDecimal.TEN).then("works!").end())
                           .from(PRODUCT)
                           .asSql())
                .isEqualTo("select PRICE, case PRICE when 1 then n'this' when 10 then n'works!' end\nfrom QName(BASIC_TEST, PROD)\n");

        // searched case

        assertThat(select(PRODUCT.PRICE,
                          PRODUCT.CATEGORY_ID_KEY.gt(10).then(PRODUCT.DESCRIPTION)
                                  .elseIf(PRODUCT.CATEGORY_ID_KEY.lt(10)).then("this")
                                  .otherwise("too!"))
                .from(PRODUCT)
                .asSql())
                .isEqualTo("select PRICE, case  when CAT > 10 then DESCR when CAT < 10 then n'this' else n'too!' end\nfrom QName(BASIC_TEST, PROD)\n");
        //J+
    }

    @Test public void testExpressions() {
        check(PRODUCT.PRICE.ge(100.0),  //
            "PROD.PRICE >= 100.0");
        check(PRODUCT.PRICE.mul(CART_ITEM.QUANTITY.add(1)).eq(1000.0),  //
            "PROD.PRICE * (CART_ITEM.QUANTITY + 1) = 1000.0");
        check(PRODUCT.PRICE.avg().mul(CART_ITEM.QUANTITY.max()),  //
            "avg(PROD.PRICE) * max(CART_ITEM.QUANTITY)");
        check(PRODUCT.DESCRIPTION.upper(),  //
            "upper(PROD.DESCR)");
        check(PRODUCT.DESCRIPTION.equalsIgnoreCase(CATEGORY.DESCR),  //
            "lower(PROD.DESCR) = lower(CATEGORY.DESCR)");
        check(PRODUCT.DESCRIPTION.substr(10),  //
            "substr(PROD.DESCR,10)");
        check(PRODUCT.DESCRIPTION.substr(10, 5),  //
            "substr(PROD.DESCR,10,5)");
        check(PRODUCT.DESCRIPTION.substr(CART_ITEM.QUANTITY),  //
            "substr(PROD.DESCR,CART_ITEM.QUANTITY)");
        check(PRODUCT.DESCRIPTION.concat(PRODUCT.PRICE, constant("  "), CATEGORY.DESCR),  //
            "PROD.DESCR || PROD.PRICE || n'  ' || CATEGORY.DESCR");
        check(PRODUCT.UPDATE_TIME.day(),  //
            "extract(day from PROD.UPDATE_TIME)");
        check(BASIC_TYPES.DATE.day(),  //
            "extract(day from BASIC_TYPES.DATE)");

        check(BASIC_TYPES.BOOL.when(true).then(PRODUCT.PRICE).otherwise(BigDecimal.ZERO).sum(),
            "sum(case BASIC_TYPES.BOOL when True then PROD.PRICE else 0 end)");

        final String   nullValue = null;
        final Criteria criteria  = Criteria.allOf(PRODUCT.PRICE.ge(100.0),
                EMPTY,
                PRODUCT.DESCRIPTION.eq("a"),
                PRODUCT.DESCRIPTION.eq(nullValue),
                PRODUCT.MODEL.contains("x"));

        check(criteria, "PROD.PRICE >= 100.0 and PROD.DESCR = n'a' and PROD.MODEL like n'%x%'");
    }

    private void check(final Expr<?> e, final String expected) {
        assertThat(e.toString()).isEqualTo(expected);
    }
}  // end class ExprTest
