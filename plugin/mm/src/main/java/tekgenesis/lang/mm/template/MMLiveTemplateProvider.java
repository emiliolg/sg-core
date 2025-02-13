
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.template;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;

/**
 * Template Provider for mi Plugin.
 */
class MMLiveTemplateProvider implements DefaultLiveTemplatesProvider {

    //~ Methods ......................................................................................................................................

    @Override public String[] getDefaultLiveTemplateFiles() {
        return new String[] { "/tekgenesis/lang/mm/template/templates" };
    }

    @Override public String[] getHiddenLiveTemplateFiles() {
        return EMPTY_ARRAY;
    }

    //~ Static Fields ................................................................................................................................

    private static final String[] EMPTY_ARRAY = {};
}
