
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.js;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.core.Constants.SCRIPT;

/**
 * Script generator.
 */
public class ScriptGenerator extends JsItemGenerator<ScriptGenerator> {

    //~ Constructors .................................................................................................................................

    protected ScriptGenerator(@NotNull JsCodeGenerator cg, String name) {
        super(cg, name, SCRIPT);
    }
}
