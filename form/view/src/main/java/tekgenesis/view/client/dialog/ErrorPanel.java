
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.dialog;

import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.view.shared.response.ResponseError;

import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.view.shared.response.ResponseError.Line;

/**
 * Exception error panel.
 */
public class ErrorPanel extends FlowPanel {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Document doc;

    @NotNull private final ResponseError error;

    //~ Constructors .................................................................................................................................

    /** Exception error panel. */
    public ErrorPanel(@NotNull ResponseError error) {
        this.error = error;
        doc        = Document.get();
        // noinspection DuplicateStringLiteralInspection
        getElement().setClassName("exception");
        build();
    }

    //~ Methods ......................................................................................................................................

    private void build() {
        final HeadingElement head = doc.createHElement(1);
        head.setInnerText(error.getDevMessage() + "!");
        getElement().insertFirst(head);

        final HeadingElement description = doc.createHElement(2);
        description.setInnerText("In " + error.getClassName() + "." + error.getMethodName() + " at line " + error.getLine());
        getElement().insertAfter(description, head);

        final Node clazz = lines();
        getElement().insertAfter(clazz, description);

        final HeadingElement cause = doc.createHElement(2);
        cause.setInnerText("Caused by:");
        getElement().insertAfter(cause, clazz);

        final Node stack = stack();
        getElement().insertAfter(stack, cause);

        final ImageElement image = doc.createImageElement();
        image.setId("stack-image-href");
        getElement().insertAfter(image, stack);
    }

    private PreElement buildPre(String first, String second) {
        final PreElement  pre      = doc.createPreElement();
        final SpanElement lineSpan = doc.createSpanElement();
        lineSpan.setClassName("line");
        lineSpan.setInnerText(first);
        pre.insertFirst(lineSpan);
        final SpanElement codeSpan = doc.createSpanElement();
        codeSpan.setClassName("code");
        codeSpan.setInnerText(second);
        pre.insertAfter(codeSpan, lineSpan);
        return pre;
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private Node lines() {
        final List<ResponseError.Line> lines = error.getClazz();
        final DivElement               clazz = doc.createDivElement();

        if (lines != null) {
            for (final Line line : lines) {
                final PreElement pre = buildPre(String.valueOf(line.getLine()), line.getContent());
                onclick(line, pre);
                if (line.getLine() == error.getLine()) pre.setClassName("error");
                clazz.appendChild(pre);
            }
        }
        return clazz;
    }

    private void onclick(@NotNull Line line, @NotNull Element element) {
        element.setAttribute("onclick",
            "document.getElementById('stack-image-href').src='http://127.0.0.1:63330/file?file=" + line.getClazz() + "&line=" + line.getLine() + "'");
    }

    private Node stack() {
        final List<Line> elements = error.getStack();
        final DivElement stack    = doc.createDivElement();

        if (elements != null) {
            // Prune reflection related stack...
            for (final Line line : seq(elements).filter(ErrorPanel::isNotReflection).take(8)) {
                final PreElement pre = buildPre(String.valueOf(elements.indexOf(line) + 1), line.getContent());
                onclick(line, pre);
                stack.appendChild(pre);
            }
        }

        return stack;
    }

    //~ Methods ......................................................................................................................................

    private static boolean isNotReflection(final Line s) {
        if (s == null) return false;
        final String content = s.getContent();
        return !content.contains("sun.reflect") && !content.contains("java.lang.reflect") && !content.contains("tekgenesis.common.util.Reflection");
    }
}  // end class ErrorPanel
