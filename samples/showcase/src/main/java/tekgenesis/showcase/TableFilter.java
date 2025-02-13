
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;

import static java.lang.Double.valueOf;
import static java.util.Collections.max;
import static java.util.Collections.min;

import static tekgenesis.common.collections.Colls.seq;

/**
 * Table Filter Form class.
 */
@SuppressWarnings({ "MagicNumber", "WeakerAccess", "SpellCheckingInspection" })
public class TableFilter extends TableFilterBase {

    //~ Instance Fields ..............................................................................................................................

    private final Predicate<Element> FILTER_CATEGORY = element -> {
                                                           if (!isDefined(Field.CATEGORIES)) return true;
                                                           for (final String next : getCategories())
                                                               if (next.equals(element.category)) return true;
                                                           return false;
                                                       };

    private final Predicate<Element> FILTER_COLOR = element -> {
                                                        if (!isDefined(Field.COLORS)) return true;
                                                        for (final String next : getColors())
                                                            if (next.equals(element.color)) return true;
                                                        return false;
                                                    };

    private final Predicate<Element> FILTER_HIGHEST = element -> !isDefined(Field.HIGHEST) || valueOf(getHighest()) >= element.price;

    private final Predicate<Element> FILTER_LOWEST = element -> !isDefined(Field.LOWEST) || valueOf(getLowest()) <= element.price;

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        setCategories(CATEGORIES);
        setLowest(min(PRICES).toString());
        setLowestOptions(seq(PRICES).toStrings());
        setHighest(max(PRICES).toString());
        setHighestOptions(seq(PRICES).toStrings());
        setColors(COLORS);
    }

    @NotNull @Override public Action refresh() {
        final FormTable<ProductsRow> rows = getProducts();

        getProducts().clear();

        for (final Element element : getFiltered()) {
            final ProductsRow row = rows.add();
            row.setName(element.name);
            row.setCategory(element.category);
            row.setPrice(String.valueOf(element.price));
            row.setColor(element.color);
        }

        return actions.getDefault();
    }

    private Iterable<Element> getFiltered() {
        return seq(ELEMENTS).filter(FILTER_CATEGORY).filter(FILTER_LOWEST).filter(FILTER_HIGHEST).filter(FILTER_COLOR);
    }

    //~ Static Fields ................................................................................................................................

    private static final Collection<Element> ELEMENTS   = new ArrayList<>();
    private static final List<String>        COLORS     = new ArrayList<>();
    private static final List<String>        CATEGORIES = new ArrayList<>();
    private static final List<Double>        PRICES     = new ArrayList<>();
    private static final String              A_TO_Z     = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static {
        COLORS.add("red");
        COLORS.add("green");
        COLORS.add("blue");

        PRICES.add(10.0);
        PRICES.add(15.0);
        PRICES.add(20.0);
        PRICES.add(25.0);
        PRICES.add(30.0);
        PRICES.add(35.0);
        PRICES.add(40.0);
        PRICES.add(45.0);

        CATEGORIES.add("Bacteria");
        CATEGORIES.add("Protozoa");
        CATEGORIES.add("Cromista");
        CATEGORIES.add("Plantae");
        CATEGORIES.add("Fungi");
        CATEGORIES.add("Animalia");

        final Random r = new Random(1);
        for (int i = 0; i < 50; i++)
            ELEMENTS.add(Element.next(r));
    }

    //~ Inner Classes ................................................................................................................................

    private static class Element {
        private final String category;
        private final String color;
        private final String name;
        private final Double price;

        private Element(String category, String color, String name, Double price) {
            this.category = category;
            this.color    = color;
            this.name     = name;
            this.price    = price;
        }

        public static Element create(String name, double price, String category, String color) {
            return new Element(category, color, name, price);
        }

        public static Element next(Random r) {
            final String name     = nextChar(r) + "" + nextChar(r);
            final double price    = PRICES.get(r.nextInt(PRICES.size()));
            final String category = CATEGORIES.get(r.nextInt(CATEGORIES.size()));
            final String color    = COLORS.get(r.nextInt(COLORS.size()));
            return create(name, price, category, color);
        }

        private static char nextChar(Random r) {
            return A_TO_Z.charAt(r.nextInt(A_TO_Z.length()));
        }
    }

    public class ProductsRow extends ProductsRowBase {}
}  // end class TableFilter
