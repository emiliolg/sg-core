
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.NoSuchElementException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.BalloonImpl;
import com.intellij.ui.JBColor;

import tekgenesis.common.util.CronExpression;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * Schedule intention suggestion form.
 */
public class CheckScheduleExpForm {

    //~ Instance Fields ..............................................................................................................................

    private String        cronExpression;
    private JList<String> dateList;

    private JLabel eventsLabel;

    private boolean expanded;
    private JPanel  listPanel;

    private JButton moreDatesButton;

    private final Pair<PsiFile, Ref<Balloon>> myParams;

    private JPanel       myRootPanel;
    private Ref<Balloon> mySchedule;
    private JTextField   myScheduleExp;
    private JScrollPane  scrollPane;

    private final ScheduleSuggesterIntention ssIntention;
    private JPanel                           textPanel;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public CheckScheduleExpForm(Pair<PsiFile, Ref<Balloon>> params, String cronExpression, ScheduleSuggesterIntention ssIntention) {
        this.cronExpression = cronExpression;
        myParams            = params;
        this.ssIntention    = ssIntention;
        createUIComponents();
        setBadExpressionMessage();
        updateCronExpText(cronExpression);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Updates and validates the cron expression aether on the schedule intention and the schedule
     * intention form.
     */
    public void updateCronExpText(String exp) {
        if (CronExpression.isValidExpression(exp)) {
            try {
                updateDateList(ssIntention.generateSuggestDates(exp));
                cronExpression = exp;
                myScheduleExp.setBackground(GREEN);
                ssIntention.onChange(exp);
            }
            catch (final NoSuchElementException noSuchElementException) {
                setBadExpressionMessage();
            }
        }
        else setBadExpressionMessage();
    }

    /** Receives an array of Strings and replaces them in the JList. */
    public void updateDateList(String[] dates) {
        dateList.setListData(dates);
    }

    /** get the Root panel. */
    public JPanel getRootPanel() {
        return myRootPanel;
    }

    @SuppressWarnings("all")
    private void createUIComponents() {
        myRootPanel = new JPanel(new BorderLayout()) {
                private static final long serialVersionUID = 4442508249191346245L;

                @Override public void addNotify() {
                    super.addNotify();
                    IdeFocusManager.getGlobalInstance().requestFocus(myScheduleExp, true);

                    updateBalloon();
                    myScheduleExp.selectAll();
                }
            };

        dateList   = new JList<>();
        scrollPane = new JScrollPane(dateList);

        eventsLabel = new JLabel();
        eventsLabel.setText(EVENTS);
        final JLabel scheduleLabel = new JLabel();
        scheduleLabel.setText(SCHEDULE);

        listPanel = new JPanel();
        textPanel = new JPanel();
        JPanel moreDatesPanel = new JPanel(new BorderLayout());

        mySchedule = myParams.second;

        myScheduleExp = new JTextField(cronExpression);
        myScheduleExp.addKeyListener(new KeyListener() {
                @Override public void keyTyped(KeyEvent e) {}

                @Override public void keyPressed(KeyEvent e) {}

                @Override public void keyReleased(KeyEvent e) {
                    updateCronExpText(myScheduleExp.getText());
                }
            });

        myScheduleExp.setToolTipText("second minute hour day mounth");

        myRootPanel.add(textPanel, BorderLayout.NORTH);
        myRootPanel.add(listPanel, BorderLayout.CENTER);
        myRootPanel.add(moreDatesPanel, BorderLayout.SOUTH);

        textPanel.add(scheduleLabel);
        textPanel.add(myScheduleExp);

        listPanel.add(eventsLabel);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        moreDatesButton = new JButton(ARROW_DOWN);
        moreDatesPanel.add(moreDatesButton, BorderLayout.EAST);
        moreDatesButton.setBorder(BUTTON_DATES_BORDER);
        moreDatesButton.setToolTipText(MSGS.showMoreDates());
        moreDatesButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    if (CronExpression.isValidExpression(myScheduleExp.getText())) expandDates();
                }
            });
        moreDatesButton.setBorder(null);
        moreDatesButton.setOpaque(false);
        moreDatesButton.setContentAreaFilled(false);
        moreDatesButton.setBorderPainted(false);

        initSizes();
        moreDatesButton.setToolTipText(MSGS.showMoreDates());
        expanded = false;
    }  // end method createUIComponents

    private void expandDates() {
        final int listSize;
        if (expanded) {
            listSize = SMALL_LIST;
            moreDatesButton.setToolTipText(MSGS.showMoreDates());
            moreDatesButton.setText(ARROW_DOWN);
            expanded = false;
            setSmallList();
        }
        else {
            listSize = LARGE_LIST;
            moreDatesButton.setToolTipText(MSGS.showLessDates());
            moreDatesButton.setText(ARROW_UP);
            expanded = true;
            setLargeList();
        }
        if (CronExpression.isValidExpression(myScheduleExp.getText())) updateDateList(ssIntention.generateSuggestDates(cronExpression, listSize));
        updateBalloon();
        ssIntention.showBalloon(listSize);
    }

    private void initSizes() {
        textPanel.setPreferredSize(TEXT_PANEL_DIMENSION);
        myScheduleExp.setPreferredSize(TEXT_DIMENSION);
        eventsLabel.setPreferredSize(EVENTS_LABEL_DIMENSION);
        setSmallList();
        moreDatesButton.setPreferredSize(MORE_DATES_BUTTON);
    }

    private void updateBalloon() {
        final BalloonImpl balloon = (BalloonImpl) mySchedule.get();
        if (balloon != null && balloon.isDisposed()) balloon.revalidate();
    }

    private void setBadExpressionMessage() {
        myScheduleExp.setBackground(RED);
        final String[] badExpMessage = new String[1];
        badExpMessage[0] = MSGS.badChronologicalExpression();
        updateDateList(badExpMessage);
    }

    private void setLargeList() {
        listPanel.setPreferredSize(LARGE_LIST_PANEL_DIMENSION);
        scrollPane.setPreferredSize(LARGE_LIST_DIMENSION);
    }

    private void setSmallList() {
        listPanel.setPreferredSize(SMALL_LIST_PANEL_DIMENSION);
        scrollPane.setPreferredSize(SMALL_LIST_DIMENSION);
    }

    //~ Static Fields ................................................................................................................................

    private static final String ARROW_UP   = "▲";
    private static final String ARROW_DOWN = "▼";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String SCHEDULE = "Schedule";
    private static final String EVENTS   = "Events:";

    @SuppressWarnings("all")
    private static final JBColor RED                 = new JBColor(new Color(255, 177, 160), new Color(110, 43, 40));
    @SuppressWarnings("all")
    private static final JBColor GREEN               = new JBColor(new Color(231, 250, 219), new Color(68, 85, 66));
    @SuppressWarnings("all")
    private static final Border  BUTTON_DATES_BORDER = new EmptyBorder(3, 90, 4, 30);

    private static final int BALLOON_SIZE      = 300;
    private static final int SMALL_LIST        = 15;
    private static final int LARGE_LIST        = 30;
    private static final int MARGIN            = 5;
    private static final int TEXT_HEIGHT       = 30;
    private static final int TEXT_WIDTH        = 230;
    private static final int EVENTS_TEXT_WIDTH = 57;
    private static final int BUTTON_WIDTH      = 250;

    private static final Dimension TEXT_PANEL_DIMENSION       = new Dimension(BALLOON_SIZE, TEXT_HEIGHT);
    private static final Dimension SMALL_LIST_PANEL_DIMENSION = new Dimension(BALLOON_SIZE, SMALL_LIST * 10 + MARGIN);
    private static final Dimension LARGE_LIST_PANEL_DIMENSION = new Dimension(BALLOON_SIZE, LARGE_LIST * 10 + MARGIN);

    private static final Dimension LARGE_LIST_DIMENSION   = new Dimension(TEXT_WIDTH - 7, LARGE_LIST * 10);
    private static final Dimension SMALL_LIST_DIMENSION   = new Dimension(TEXT_WIDTH - 7, SMALL_LIST * 10);
    private static final Dimension TEXT_DIMENSION         = new Dimension(TEXT_WIDTH, TEXT_HEIGHT - 2);
    private static final Dimension EVENTS_LABEL_DIMENSION = new Dimension(EVENTS_TEXT_WIDTH, TEXT_HEIGHT);
    private static final Dimension MORE_DATES_BUTTON      = new Dimension(BUTTON_WIDTH, SMALL_LIST + MARGIN);
}  // end class CheckScheduleExpForm
