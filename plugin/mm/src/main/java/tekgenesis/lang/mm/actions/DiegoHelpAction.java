
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.translate.ui.DiegoHelp;

/**
 * DiegoR can help you.
 */
@SuppressWarnings("WeakerAccess")
public class DiegoHelpAction extends AnAction {

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        new DiegoHelp("No Need to Fear Diegor is Here");
    }
}
