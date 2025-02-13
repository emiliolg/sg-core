
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.lang.Language;

import org.jetbrains.annotations.NonNls;

/**
 * Language class for MetaModel language.
 */
public class MMLanguage extends Language {

    //~ Constructors .................................................................................................................................

    private MMLanguage() {
        super(ID, "application/metamodel");
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String ID = MMPluginConstants.META_MODEL;

    public static final Language INSTANCE = new MMLanguage();
}
