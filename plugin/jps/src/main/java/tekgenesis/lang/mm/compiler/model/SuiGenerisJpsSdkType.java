
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.compiler.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElement;
import org.jetbrains.jps.model.JpsSimpleElement;
import org.jetbrains.jps.model.java.JpsJavaSdkTypeWrapper;
import org.jetbrains.jps.model.library.sdk.JpsSdkType;

/**
 * Type for SuiGeneris jps sdk properties interpretation.
 */
public class SuiGenerisJpsSdkType extends JpsSdkType<JpsSimpleElement<SuiGenerisJpsSdkProperties>> implements JpsJavaSdkTypeWrapper {

    //~ Methods ......................................................................................................................................

    @Override public String getJavaSdkName(@NotNull JpsElement properties) {
        return ((SuiGenerisJpsSdkProperties) ((JpsSimpleElement<?>) properties).getData()).getJdkName();
    }

    //~ Static Fields ................................................................................................................................

    public static final SuiGenerisJpsSdkType INSTANCE = new SuiGenerisJpsSdkType();
}
