
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
import java.io.OutputStream;
import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;
import tekgenesis.form.ExecutionFeedback;

import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.media.Mime.TEXT_PLAIN;
import static tekgenesis.form.Download.DownloadWriter;
import static tekgenesis.showcase.FeedbackMessages.*;
import static tekgenesis.showcase.g.NodeTable.NODE;

/**
 * Widget Showcase entity class.
 */
@SuppressWarnings("WeakerAccess")
public class WidgetShowcase extends WidgetShowcaseBase {

    //~ Methods ......................................................................................................................................

    @NotNull public Action breadCrumbNavigation() {
        final String selectedLink = getBreadcrumb();
        System.out.println("to!!!!!!!!!!!!!!!!!!!!!! = " + selectedLink);

        return actions.getDefault();
    }

    @NotNull @Override public Action changed() {
        System.out.println(Colls.mkString(getPick()));
        return actions.getDefault();
    }

    @NotNull @Override public Action doLongExecution(@NotNull ExecutionFeedback feedback) {
        feedback.step(STARTING.label());
        for (int i = 0; i < 100 && !feedback.isCanceled(); i += 5) {
            try {
                feedback.step(i, PROGRESS.label(i));
                Thread.sleep(5_000);
            }
            catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        final String msg = feedback.isCanceled() ? CANCELED.label() : COMPLETED.label();
        message(Field.LONG_BTN, msg).info().inline();
        return actions.getDefault();
    }

    @NotNull @Override public Action doStuff() {
        return actions.navigate(ClientForm.class).dialog();
    }

    @NotNull @Override public Action doWait() {
        try {
            Thread.sleep(5000);
        }
        catch (final InterruptedException e) {
            // ignore
        }
        return actions.getDefault();
    }

    @NotNull @Override public Action download() {
        final Action result = actions.getDefault();
        result.withDownload(PlainTextDownload.class).withFileName("example.txt").withContentType(TEXT_PLAIN).zipped();
        return result.withMessage("Plain text download started!");
    }

    public void load() {
        setBreadcrumbOptions(Arrays.asList("A", "B", "C"));
        setTreeViewOptions(Node.listWhere(NODE.PARENT_ID.isNull()).list());
        setPickOptions(ImmutableList.of(Options.OPTION1, Options.OPTION3, Options.OPTION5));
        setPick(ImmutableList.of(Options.OPTION1));
    }

    @NotNull @Override public Action makeChanged() {
        if (isDefined(Field.SUGGEST)) setSuggestInfo("Make changed: " + getSuggest());
        else setSuggestInfo("Make is not defined.");
        return actions.getDefault();
    }

    @NotNull @Override public Action nodeSelected() {
        final Node selected = getTreeView();
        return actions.getDefault().withMessage(selected.getName() + " selected!");
    }

    @NotNull @Override public Action printColor() {
        if (isDefined(WidgetShowcaseBase.Field.COLORPICKER)) System.out.println("getColorPicker() = " + getColorpicker());

        return actions.getDefault();
    }

    @NotNull @Override public Action randomProgress() {
        setProgressNumber(Math.random() * 100);
        return actions.getDefault();
    }

    @NotNull @Override public Action viewDate() {
        System.out.println("getDatebox2() = " + getDatebox2());

        return actions.getDefault();
    }

    @NotNull @Override public Action viewDateTimeBox() {
        System.out.println("getDatetimebox() = " + getDatetimebox());

        return actions.getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class PlainTextDownload implements DownloadWriter {
        @Override public void into(@NotNull OutputStream stream)
            throws IOException
        {
            final String msg = "Hello World! " + (isDefined(Field.WORDS) ? getWords() : "");
            stream.write(msg.getBytes(UTF8));
            stream.flush();
        }
    }
}  // end class WidgetShowcase
