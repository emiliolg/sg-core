
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;

import org.junit.Test;

import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.codegen.project.ProjectBuilder;
import tekgenesis.common.collections.Colls;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.lang.mm.TestUtil.createFile;

/**
 * IdeaTest.
 */
@SuppressWarnings({ "JUnit4AnnotatedMethodInJUnit3TestCase", "DuplicateStringLiteralInspection", "JavaDoc" })
public class IdeaTest extends MMFixtureTestCase {

    //~ Methods ......................................................................................................................................

    @Test public void testCompletion()
        throws IOException
    {
        assertCompletion("plugin/mm/src/test/resources/IdeaTest/CompletionTest.mm",
            "CompletionTest.mm",
            false,
            "Boolean",
            "Date",
            "DateTime",
            "Decimal",
            "Int",
            "Real",
            "Resource",
            "String",
            "GORBEL",
            "Simi");
    }

    @Test public void testHighlighting()
        throws IOException
    {
        initProject();
        final String fileString = TestUtil.readFile("plugin/mm/src/test/resources/IdeaTest/HighlightingTest");
        VirtualFileManager.getInstance().syncRefresh();
        final PsiFile file = createFile(mainModule, TestUtil.findChild(srcMM, "sales"), "HLTest.mm", fileString);

        final File            rootDir         = new File(srcMM.getPath());
        final ModelRepository repository      = new ModelRepositoryLoader(rootDir).build();
        final File            outputDir       = new File(rootDir, "../../target");
        final File            generatedSrcDir = new File(outputDir, "generatedsrc");
        new ProjectBuilder(repository, rootDir, outputDir, generatedSrcDir, Colls.emptyIterable()).buildProject();

        FileUtils.synchronizeFiles();
        myFixture.testHighlighting(false, false, false, file.getVirtualFile());
    }

    @Test public void testModelCompletion()
        throws IOException
    {
        assertCompletion("plugin/mm/src/test/data/completionTestProject/BloodAndFire/src/main/mm/domain/Victarion.mm",
            "Victarion.mm",
            true,
            "case",
            "entity",
            "enum",
            "form",
            "handler",
            "importer task ",
            "lifecycle task ",
            "link",
            "menu",
            "processor task ",
            "runnable task ",
            "task",
            "view",
            "widget");
    }

    @Test public void testRename()
        throws IOException
    {
        initProject();
        myFixture.testRename("resources/IdeaTest/renameTestBefore.mm", "resources/IdeaTest/renameTestAfter.mm", "Mono", "resources/file2.mm");
    }

    @Test public void testWidgetCompletion()
        throws IOException
    {
        assertCompletion("plugin/mm/src/test/data/completionTestProject/BloodAndFire/src/main/mm/domain/Plan.mm",
            "Plan.mm",
            true,
            "breadcrumb",
            "check",
            "affix",
            "check_box_group",
            "combo_box",
            "default",
            "disable",
            "depends_on",
            "display",
            "popover",
            "dynamic",
            "hide",
            "hide_column",
            "hint",
            "internal",
            "is",
            "skip_tab",
            "label_expression",
            "list_box",
            "on_change",
            "on_ui_change",
            "on_blur",
            "pick_list",
            "placeholder",
            "shortcut",
            "radio_group",
            "reset",
            "style",
            "suggest_box",
            "tags_suggest_box",
            "tags_combo_box",
            "tooltip",
            "tree_view",
            "unique",
            "toggle_button",
            "check_box",
            "dialog",
            "content_style",
            "column_style",
            "horizontal",
            "vertical",
            "width",
            "subform",
            "widget",
            "col",
            "inline_style",
            "label_col",
            "no_label",
            "offset_col",
            "top_label");
    }  // end method testWidgetCompletion

    @Test public void testWidgetCompletionNoValue()
        throws IOException
    {
        assertCompletion("plugin/mm/src/test/data/completionTestProject/BloodAndFire/src/main/mm/domain/Danerys.mm",
            "Danerys.mm",
            true,
            "anchor",
            "button",
            "chart",
            "date_box",
            "double_date_box",
            "date_picker",
            "date_time_box",
            "pick_list",
            "popover",
            "dynamic",
            "footer",
            "form",
            "gallery",
            "header",
            "horizontal",
            "image",
            MMCodeGenConstants.LABEL,
            "mail_field",
            "map",
            "message",
            "password_field",
            "progress",
            "radio_group",
            "range",
            "range_value",
            "rating",
            "rich_text_area",
            "search_box",
            "section",
            "showcase",
            "subform",
            "widget",
            "table",
            "tabs",
            "tags",
            "text_area",
            "text_field",
            "time_picker",
            "vertical",
            "breadcrumb",
            "check_box_group",
            "color_picker",
            "combo_box",
            "combo_date_box",
            "display",
            "internal",
            "list_box",
            "suggest_box",
            "tags_suggest_box",
            "tags_combo_box",
            "tree_view",
            "toggle_button",
            "check_box",
            "dialog",
            "upload",
            "video",
            "dropdown",
            "input_group",
            "iframe");
    }  // end method testWidgetCompletionNoValue

    @Test public void testWidgetCompletionThroughBinding()
        throws IOException
    {
        assertCompletion("plugin/mm/src/test/data/completionTestProject/BloodAndFire/src/main/mm/domain/Quentyn.mm",
            "Quentyn.mm",
            true,
            "breadcrumb",
            "check",
            "affix",
            "check_box_group",
            "change_delay",
            "change_threshold",
            "combo_box",
            "color_picker",
            "custom_mask",
            "column_style",
            "default",
            "disable",
            "expand",
            "depends_on",
            "display",
            "dynamic",
            "gallery",
            "hide",
            "align",
            "axis",
            "hide_column",
            "hint",
            "icon",
            "icon_expr",
            "width",
            "iframe",
            "image",
            "mask",
            "skip_tab",
            "internal",
            "subform",
            "widget",
            "nopaste",
            "is",
            "label_expression",
            "list_box",
            "mail_field",
            "message",
            "on_change",
            "on_ui_change",
            "on_blur",
            "optional",
            "password_field",
            "pick_list",
            "placeholder",
            "radio_group",
            "required",
            "reset",
            "rich_text_area",
            "secret",
            "showcase",
            "signed",
            "style",
            "suggest_box",
            "tags",
            "tags_suggest_box",
            "tags_combo_box",
            "shortcut",
            "text_area",
            "text_field",
            "tooltip",
            "tree_view",
            "unique",
            "unsigned",
            "content_style",
            "video",
            "col",
            "inline_style",
            "label_col",
            "no_label",
            "offset_col",
            "top_label");
    }  // end method testWidgetCompletionThroughBinding

    @Test public void testWidgetOptionCompletion()
        throws IOException
    {
        assertCompletion("plugin/mm/src/test/data/completionTestProject/BloodAndFire/src/main/mm/domain/UnbowedUnbentUnbroken.mm",
            "UnbowedUnbentUnbroken.mm",
            true,
            "check",
            "default",
            "disable",
            "depends_on",
            "is",
            "skip_tab",
            "label_expression",
            "on_change",
            "multiple",
            "on_ui_change",
            "on_blur",
            "optional",
            "placeholder",
            "required",
            "reset",
            "signed",
            "tooltip",
            "shortcut",
            "content_style",
            "width",
            "unique",
            "unsigned");
    }

    private void assertCompletion(String fileName, String mmName, boolean exactly, String... completionWords)
        throws IOException
    {
        initProject();
        final String  file    = TestUtil.readFile(fileName);
        final PsiFile psiFile = createFile(mainModule, TestUtil.findChild(srcMM, "catalog"), mmName, file);
        mainModule.getComponent(MMModuleComponent.class).initializeModelRepository();
        final List<String> variants = myFixture.getCompletionVariants(psiFile.getVirtualFile().getPath());
        if (exactly) assertThat(variants).containsOnly(completionWords);
        else assertThat(variants).contains(completionWords);
    }
}  // end class IdeaTest
