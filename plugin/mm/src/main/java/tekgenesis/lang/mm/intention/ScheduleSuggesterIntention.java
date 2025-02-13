
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.intention;

import java.awt.*;
import java.util.NoSuchElementException;

import javax.swing.*;

import com.intellij.codeInsight.intention.AbstractIntentionAction;
import com.intellij.codeInsight.intention.impl.QuickEditAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ui.UIUtil;

import org.intellij.lang.regexp.RegExpLanguage;
import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateTime;
import tekgenesis.common.util.CronExpression;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * Schedule intention suggestion class.
 */
public class ScheduleSuggesterIntention extends AbstractIntentionAction implements Iconable {

    //~ Instance Fields ..............................................................................................................................

    private Balloon balloon = null;

    private CheckScheduleExpForm checkScheduleExpForm = null;

    private final String cronExpression;
    private String[]     cronExpressions;

    private Editor        editor = null;
    private final PsiFile file;

    private final ScheduleSuggesterIntention ssIntention;

    private final int startOffsetExpression;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public ScheduleSuggesterIntention(@NotNull final String cronExpression, PsiFile file, int startOffsetExpression) {
        this.cronExpression        = cronExpression;
        this.startOffsetExpression = startOffsetExpression;

        try {
            cronExpressions = generateSuggestDates(cronExpression);
        }
        catch (NoSuchElementException | CronExpression.Exception e) {
            cronExpressions    = new String[1];
            cronExpressions[0] = MSGS.badChronologicalExpression();
        }

        this.file   = file;
        ssIntention = this;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Method to generate an array of suggestions, presented as String. Default method that
     * generates de default number of suggestions from a given cron expression
     */
    public String[] generateSuggestDates(String cronExpressionString) {
        return generateSuggestDates(cronExpressionString, DEFAULT_DATE_NUMBER);
    }

    /**
     * Method to generate an array of suggestions, presented as String. generates as many
     * suggestions from a given cron expression as the second parameter that receive s
     */
    public String[] generateSuggestDates(String cronExpressionString, int numberOfSuggestions)
        throws NoSuchElementException
    {
        final String[] dates = new String[numberOfSuggestions];

        final CronExpression newCronExpression = new CronExpression(cronExpressionString);
        DateTime             timeAfter         = newCronExpression.getTimeAfter(DateTime.current());

        for (int i = 0; i < numberOfSuggestions; i++) {
            if (timeAfter == null) break;
            dates[i]  = timeAfter.toDate().toString();
            timeAfter = newCronExpression.getTimeAfter(timeAfter);
        }

        return dates;
    }

    @Override public void invoke(@NotNull final Project project, final Editor newEditor, final PsiFile psiFile)
        throws IncorrectOperationException
    {
        editor = newEditor;
        ApplicationManager.getApplication().runWriteAction(() -> {
            final Ref<Balloon> ref = Ref.create(null);
            checkScheduleExpForm = new CheckScheduleExpForm(Pair.create(file, ref), cronExpression, ssIntention);

            balloon = JBPopupFactory.getInstance().createBalloonBuilder(checkScheduleExpForm.getRootPanel()).setShadow(true).setAnimationCycle(0)
                                    .setHideOnClickOutside(true)
                                    .setHideOnKeyOutside(true)
                                    .setHideOnAction(false)
                                    .setFillColor(UIUtil.getControlColor())
                                    .createBalloon();

            ref.set(balloon);
            Disposer.register(project, balloon);
            showBalloon();
        });
    }

    /**
     * Method called from the Schedule intention form when the expression it's modified in order to
     * modify the document as well.
     */
    public void onChange(@NotNull final String newExp) {
        final Document document = editor.getDocument();
        CommandProcessor.getInstance().executeCommand(editor.getProject(), () ->
                ApplicationManager.getApplication().runWriteAction(() -> {
                    final PsiElement element = file.findElementAt(startOffsetExpression);
                    if (element != null) {
                        final int length = element.getText().length();
                        document.replaceString(startOffsetExpression, startOffsetExpression + length, '"' + newExp + '"');
                    }
                }),
            SCHEDULE_TASK, null);
    }

    /** Default method to show the suggestion intention balloon whit standard measures. */
    public void showBalloon() {
        final Balloon.Position position = QuickEditAction.getBalloonPosition(editor);
        RelativePoint          point    = JBPopupFactory.getInstance().guessBestPopupLocation(editor);
        if (position == Balloon.Position.above) {
            final Point p = point.getPoint();
            point = new RelativePoint(point.getComponent(), new Point(p.x, p.y - editor.getLineHeight()));
        }

        checkScheduleExpForm.updateDateList(cronExpressions);
        balloon.show(point, position);
    }

    /** Method to show the suggestion intention balloon whit a larger date list. */
    public void showBalloon(int dateNumber) {
        try {
            checkScheduleExpForm.updateDateList(generateSuggestDates(cronExpression, dateNumber));
            balloon.revalidate();
        }
        catch (final NullPointerException e) {
            // Do nothing
        }
    }

    @Override
    @SuppressWarnings("all")
    public Icon getIcon(@IconFlags int flags) {
        return RegExpLanguage.INSTANCE.getAssociatedFileType().getIcon();
    }

    @NotNull @Override public String getText() {
        return MSGS.showFutureSchedules();
    }

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_DATE_NUMBER = 15;

    public static final String SCHEDULE_TASK = "Schedule task";
}  // end class ScheduleSuggesterIntention
