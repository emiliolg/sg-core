
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.template;

import com.intellij.codeInsight.template.FileTypeBasedContextType;

import tekgenesis.lang.mm.MMFileType;

/**
 * Context For MM, limits templates' scope.
 */
class MMContextType extends FileTypeBasedContextType {

    //~ Constructors .................................................................................................................................

    protected MMContextType() {
        super("METAMODEL", "Metamodel", MMFileType.INSTANCE);
    }
}
