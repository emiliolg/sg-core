
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.markdown;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.*;

import tekgenesis.common.core.Option;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.util.Files.readInput;

/**
 * Documentation for Widgets.
 */
public class Widgets {

    //~ Constructors .................................................................................................................................

    private Widgets() {}

    //~ Methods ......................................................................................................................................

    /** Returns the documentation for a given widget id, or Option.none() if not present. */
    public static Option<String> getWidgetDocumentation(@NotNull final String widgetId) {
        if (!widgetsDocLoaded()) return Option.empty();

        for (final RootNode node : buildNodeForWidget(widgetId))
            return some(serializer.toHtml(node));

        return Option.empty();
    }

    private static Option<RootNode> buildNodeForWidget(@NotNull final String widgetId) {
        final List<Node> list = new ArrayList<>();

        boolean collecting = false;

        for (int i = 0; i < root.getChildren().size(); i++) {
            final Node node = root.getChildren().get(i);

            if (node instanceof HeaderNode) {
                final Node firstChild = node.getChildren().get(0);
                if (firstChild instanceof TextNode) {
                    if (((TextNode) firstChild).getText().toLowerCase().startsWith(widgetId.toLowerCase())) {
                        list.add(node);
                        collecting = true;
                    }
                    else collecting = false;
                }
            }
            else {
                if (collecting) list.add(node);
            }
        }

        final RootNode rootNode = new RootNode();
        rootNode.getChildren().addAll(list);

        return some(rootNode);
    }

    private static boolean widgetsDocLoaded() {
        return root != null;
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String WIDGETS_MD = "/forms/widgets/widgets.md";

    private static RootNode               root       = null;
    private static final ToHtmlSerializer serializer;

    static {
        final InputStream inputStream = Widgets.class.getResourceAsStream(WIDGETS_MD);
        final String      widgetsStr  = readInput(new InputStreamReader(inputStream));

        if (isNotEmpty(widgetsStr)) {
            final PegDownProcessor processor = new PegDownProcessor();
            root = processor.parseMarkdown(widgetsStr.toCharArray());
        }

        serializer = new ToHtmlSerializerWithoutImage(new LinkRenderer());
    }

    //~ Inner Classes ................................................................................................................................

    private static class ToHtmlSerializerWithoutImage extends ToHtmlSerializer {
        public ToHtmlSerializerWithoutImage(LinkRenderer linkRenderer) {
            super(linkRenderer);
        }

        @Override protected void printImageTag(LinkRenderer.Rendering rendering) {
            // ignore
            // super.printImageTag(rendering);
        }

        @Override protected void printLink(LinkRenderer.Rendering rendering) {
            printer.print(rendering.text);
        }
    }
}  // end class Widgets
