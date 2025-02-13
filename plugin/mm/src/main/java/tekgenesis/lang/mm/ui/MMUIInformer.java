
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.ui;

import javax.swing.*;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import tekgenesis.lang.mm.MMFileEditorListener;

/**
 * Class in charge of informing User of Changes.
 */
public class MMUIInformer {

    //~ Constructors .................................................................................................................................

    private MMUIInformer() {}

    //~ Methods ......................................................................................................................................

    /** Shows Info popUp in Status Bar. */
    public static void showBalloonPopUp(Project project, String message) {
        showBalloonPopUp(project, message, MessageType.INFO);
    }
    /** Shows Info popUp in Status Bar. */
    public static void showErrorBalloonPopUp(Project project, String message) {
        showBalloonPopUp(project, message, MessageType.ERROR);
    }
    /** Shows message in Log. */
    public static void showLogMessage(Project project, String message) {
        showLogMessage(project, message, NotificationType.INFORMATION);
    }
    /** Shows message in Log. */
    public static void showLogMessage(Project project, String message, NotificationType type) {
        notificationGroup.createNotification(message, type).notify(project);
    }

    /** Shows Info popUp in Status Bar. */
    public static void showWarningBalloonPopUp(Project project, String message) {
        showBalloonPopUp(project, message, MessageType.WARNING);
    }
    /** Shows popUp in Status Bar with desired @MessageType. */

    private static void showBalloonPopUp(Project project, String message, MessageType type) {
        if (SwingUtilities.isEventDispatchThread()) {
            final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);

            assert statusBar != null;
            JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder("<html><body>" + message + "</body></html>", type, null)
                .setFadeoutTime(FADE_TIME)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
        }
        else showLogMessage(project, message, type.toNotificationType());
    }

    //~ Static Fields ................................................................................................................................

    private static final NotificationGroup notificationGroup = NotificationGroup.logOnlyGroup(MMFileEditorListener.MM_GROUP);

    private static final int FADE_TIME = 5000;
}
