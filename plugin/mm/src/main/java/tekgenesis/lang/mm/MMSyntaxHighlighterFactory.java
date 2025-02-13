
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.highlight.MMSyntaxHighlighter;

/**
 * The factory for the Syntax Highlighter.
 */
@SuppressWarnings("WeakerAccess")
public class MMSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    //~ Methods ......................................................................................................................................

    @NotNull public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        return new MMSyntaxHighlighter();
    }
}
