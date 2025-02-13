
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions.introspector;

import java.awt.*;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;

import tekgenesis.lang.mm.MMFileType;

import static tekgenesis.lang.mm.FileUtils.fileCanBeCreatedInIde;
import static tekgenesis.lang.mm.MMPluginConstants.META_MODEL;

/**
 * Action to create entities from DB Introspection.
 */
public class EntitiesFromDBAction extends AnAction {

    //~ Constructors .................................................................................................................................

    /** Constructor for action. */
    public EntitiesFromDBAction() {
        super(META_MODEL, ENTITIES_FROM_DB, MMFileType.ENTITY_FILE_ICON_SMALL);
    }

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(final AnActionEvent e) {
        final IntrospectorDialog dialog = new IntrospectorDialog(e);
        dialog.setMinimumSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    @Override public void update(final AnActionEvent e) {
        final DataContext  dataContext  = e.getDataContext();
        final Presentation presentation = e.getPresentation();

        final boolean enabled = isAvailable(dataContext);

        presentation.setVisible(enabled);
        presentation.setEnabled(enabled);
    }

    private boolean isAvailable(DataContext dataContext) {
        return fileCanBeCreatedInIde(dataContext);
    }

    //~ Static Fields ................................................................................................................................

    public static final String ENTITIES_FROM_DB = "Entities from DB";
    public static final int    DIALOG_WIDTH     = 750;
    public static final int    DIALOG_HEIGHT    = 400;
}
