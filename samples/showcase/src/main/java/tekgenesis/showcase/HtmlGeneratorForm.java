
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.form.Action;
import tekgenesis.service.html.HtmlBuilder;
import tekgenesis.service.html.HtmlInstanceBuilder;

/**
 * User class for Form: JspHtmlGenerator
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
public class HtmlGeneratorForm extends HtmlGeneratorFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action generateFromMustache() {
        HtmlInstanceBuilder.Mustache generator = null;

        final HtmlBuilder htmlBuilder = Context.getSingleton(HtmlBuilder.class);
        if (isDefined(Field.SOURCE)) generator = htmlBuilder.mustacheSource(getSource());
        else if (isDefined(Field.RESOURCE)) generator = htmlBuilder.mustache(getResource());

        if (generator != null) {
            generator.param("persons", persons());

            final StringWriter writer = new StringWriter();
            try {
                generator.build().render(writer);
                setResult(writer.toString());
            }
            catch (final IOException e) {
                setResult(e.getMessage());
            }
        }

        return actions.getDefault();
    }

    @NotNull private ArrayList<Person> persons() {
        final ArrayList<Person> persons = new ArrayList<>();
        persons.add(new Person() {
                @Override public String getName() {
                    return "Lucas";
                }
                @Override public int getAge() {
                    return 30;
                }
            });
        persons.add(new Person() {
                @Override public String getName() {
                    return "Pedro";
                }
                @Override public int getAge() {
                    return 30;
                }
            });

        return persons;
    }
}  // end class HtmlGeneratorForm
