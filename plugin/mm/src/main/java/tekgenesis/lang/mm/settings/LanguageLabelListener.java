
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.settings;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA. User: santiago Date: 9/10/12 Time: 12:58 PM To change this template
 * use File | Settings | File Templates.
 */
class LanguageLabelListener implements FocusListener {

    //~ Instance Fields ..............................................................................................................................

    private final JButton removeButton;

    //~ Constructors .................................................................................................................................

    LanguageLabelListener(JButton removeButton) {
        this.removeButton = removeButton;
    }

    //~ Methods ......................................................................................................................................

    @Override public void focusGained(FocusEvent focusEvent) {
        removeButton.setEnabled(true);
    }

    @Override public void focusLost(FocusEvent focusEvent) {
        removeButton.setEnabled(false);
    }
}
