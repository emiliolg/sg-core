
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import tekgenesis.common.core.Strings;

/**
 * Simple Form Printer: prints form widgets with their types, names and labels
 */
class SimpleFormPrinter {

    //~ Instance Fields ..............................................................................................................................

    private final StringBuilder builder;
    private int                 indentation;

    //~ Constructors .................................................................................................................................

    private SimpleFormPrinter() {
        builder = new StringBuilder();
    }

    //~ Methods ......................................................................................................................................

    private String dump() {
        return builder.toString();
    }

    private String indentation() {
        return Strings.nChars('\t', indentation);
    }

    private void line() {
        builder.append('\n');
    }

    private void print(Widget widget) {
        print(widget.getWidgetType(), widget.getName(), widget.getLabel());
    }

    private void print(WidgetType type, String name, String label) {
        builder.append(indentation()).append(type).append('(').append(name).append(',').append('\"').append(label).append('\"').append(')');
    }

    private void traverse(Iterable<Widget> children) {
        indentation++;
        for (final Widget widget : children) {
            line();
            print(widget);
            traverse(widget);
        }
        indentation--;
    }

    //~ Methods ......................................................................................................................................

    static String dump(final Form form) {
        final SimpleFormPrinter printer = new SimpleFormPrinter();
        printer.print(WidgetType.FORM, form.getName(), form.getLabel());
        printer.traverse(form);
        return printer.dump();
    }
}  // end class SimpleFormPrinter
