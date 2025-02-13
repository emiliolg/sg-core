
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

/**
 * Generate Default Listing for Entity o Enum.
 */
public class GenerateDefaultListingAction extends GenerateForModelAction {

    //~ Constructors .................................................................................................................................

    /** Generate Default Listing for Entity. */
    public GenerateDefaultListingAction() {}

    /** Generate Default Listing for Entity. */
    public GenerateDefaultListingAction(Editor editor, Project project) {
        super(editor, project);
    }

    //~ Methods ......................................................................................................................................

    @Override String getActionName() {
        return GENERATE_DEFAULT_LISTING;
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    String getTextToWrite() {
        final String defaultListingName = psiModel.getFullName() + "Listing";
        String       fullListingName    = defaultListingName;
        String       listingName        = psiModel.getName() + "Listing";
        boolean      hasListingWithName = psiModel.hasFormWithName(fullListingName);
        int          i                  = 0;
        while (hasListingWithName) {
            i++;
            fullListingName    = defaultListingName + i;
            listingName        = psiModel.getName() + "Listing" + i;
            hasListingWithName = psiModel.hasFormWithName(fullListingName);
        }
        return "\n\nform " + listingName + " listing " + psiModel.getName() + ";";
    }

    @Override String getTextToWriteInModel() {
        return "";
    }

    //~ Static Fields ................................................................................................................................

    public static final String GENERATE_DEFAULT_LISTING = "GenerateDefaultListing";
}  // end class GenerateDefaultListingAction
