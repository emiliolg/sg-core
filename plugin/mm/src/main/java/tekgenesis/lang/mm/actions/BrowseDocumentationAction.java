
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.browsers.WebBrowser;
import com.intellij.ide.browsers.WebBrowserManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.Url;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Strings;

import static java.io.File.separator;
import static java.io.File.separatorChar;
import static java.util.Arrays.asList;

import static com.intellij.util.Urls.newLocalFileUrl;

import static tekgenesis.lang.mm.FileUtils.TARGET;
import static tekgenesis.lang.mm.ProjectUtils.isSuiGenerisProject;
import static tekgenesis.lang.mm.sdk.Sdks.sdkFromAction;
import static tekgenesis.lang.mm.sdk.SuiGenerisSdk.isSuiGenerisSDK;

/**
 * Action to browse to sui generis documentation in current or default browser.
 */
public class BrowseDocumentationAction extends AnAction {

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(AnActionEvent e) {
        final Sdk sdk = sdkFromAction(e);
        if (sdk != null && isSuiGenerisSDK(sdk)) browse(e, getSdkDocumentationIndexPath(sdk));
        else if (isSuiGenerisProject(e.getProject())) browse(e, getSuigenDeveloperIndexPath(e.getProject()));
    }

    @Override public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(isSuiGenerisSDK(sdkFromAction(e)) || isSuiGenerisProject(e.getProject()));
    }

    private void browse(AnActionEvent e, String path) {
        final Url        url     = newLocalFileUrl(path);
        final WebBrowser browser = WebBrowserManager.getInstance().getFirstActiveBrowser();
        BrowserLauncher.getInstance().browse(url.toExternalForm(), browser, e.getProject());
    }

    //~ Methods ......................................................................................................................................

    @NotNull private static String getSdkDocumentationIndexPath(Sdk sdk) {
        return sdk.getHomePath() + separator + "doc" + separator + INDEX;
    }

    @NotNull private static String getSuigenDeveloperIndexPath(Project project) {
        return project.getBasePath() + separator + Strings.join(asList(TARGET, "suigen", "doc", "gen-html", INDEX), separatorChar);
    }

    //~ Static Fields ................................................................................................................................

    public static final String INDEX = "index.html";
}  // end class BrowseDocumentationAction
