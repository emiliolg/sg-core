
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.common.core.Option;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.form.widget.WidgetType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.util.Files.readInput;
import static tekgenesis.field.FieldOption.forms;
import static tekgenesis.markdown.Widgets.WIDGETS_MD;
import static tekgenesis.metadata.form.widget.WidgetType.*;

/**
 * Tests documentation for widget types.
 */
public class DocumentationTest {

    //~ Methods ......................................................................................................................................

    /** Tests that every form field option is documented. */
    @Test public void testFieldOptionsDoc() {
        for (final String s : readMarkdownResource("/forms/forms.md")) {
            for (final FieldOption option : forms())
                assertThat(s).contains(toMarkdownBold(option));
        }
    }

    /** Tests that every widget type is documented. */
    @Test public void testWidgetTypesDoc() {
        for (final String s : readMarkdownResource(WIDGETS_MD)) {
            for (final WidgetType type : values())
                if (mustBeDocumented(type)) assertThat(s).contains(type.getId());
        }
    }

    private boolean mustBeDocumented(WidgetType type) {
        return type != TREE_VIEW && type != RANGE && type != RANGE_VALUE && type != NONE;
    }

    private Option<String> readMarkdownResource(String path) {
        String s = null;

        final URL resource = DocumentationTest.class.getResource(path);

        try {
            s = readInput(new FileReader(new File(resource.toURI())));
        }
        catch (final URISyntaxException e) {
            fail("Cannot load widgets markdown documentation file.", e);
        }
        catch (final FileNotFoundException e) {
            fail("Cannot read widgets markdown documentation file.", e);
        }

        return option(s);
    }

    @NotNull private String toMarkdownBold(FieldOption option) {
        return "**" + option.getId() + "**";
    }
}  // end class DocumentationTest
