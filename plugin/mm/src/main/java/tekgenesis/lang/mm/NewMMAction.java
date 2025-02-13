
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.CreateFromTemplateAction;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.fileTemplates.impl.FileTemplateBase;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.mmcompiler.ast.MMToken;

import static tekgenesis.lang.mm.FileUtils.fileCanBeCreatedInIde;
import static tekgenesis.lang.mm.MMPluginConstants.*;

/**
 * Intellij Idea action to create a new .mm file.
 */
class NewMMAction extends CreateFromTemplateAction<MMFile> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public NewMMAction() {
        super(META_MODEL, "New Meta Model file", MMFileType.ENTITY_FILE_ICON_SMALL);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
        builder.addKind(META_MODEL, MMFileType.ENTITY_FILE_ICON, META_MODEL);
        builder.addKind(Constants.ENTITY, MMFileType.ENTITY_ICON, Constants.ENTITY);
        builder.addKind(FORM, MMFileType.FORM_ICON, FORM);
        builder.addKind(HANDLER, MMFileType.HANDLER_ICON, HANDLER);
        builder.addKind(TASK, MMFileType.TASK_ICON, TASK);
        builder.addKind(ENUM, MMFileType.ENUM_ICON, ENUM);
    }

    @Nullable @Override protected MMFile createFile(String name, String templateName, PsiDirectory dir) {
        return checkOrCreate(name, dir, templateName);
    }

    @Override protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return "Add MetaModel";
    }

    @Override protected boolean isAvailable(final DataContext dataContext) {
        return fileCanBeCreatedInIde(dataContext);
    }

    @Override protected String getErrorTitle() {
        return "Error adding MetaModel";
    }

    private void appendEntity(final String name, final String labelName, StringBuilder builder) {
        builder.append(MMToken.ENTITY.getText()).append(" ").append(name).append(" \"").append(labelName).append("\"\n");
        builder.append("\t").append(MMToken.DESCRIBED_BY.getText()).append(" name ").append("\n\t").append(MMToken.SEARCHABLE.getText()).append("\n");
        builder.append("{\n");
        builder.append("\tname \"Name\" : String(20);\n");
        builder.append("}\n\n");
    }

    private void appendEnum(final String name, StringBuilder builder) {
        builder.append(MMToken.ENUM.getText()).append(" ").append(name).append("\n{\n");
        builder.append("\t").append("NAME").append(" : ").append("\"name\"").append(";\n}");
    }

    private void appendForm(final String name, final String labelName, StringBuilder builder) {
        builder.append(MMToken.FORM.getText()).append(" ").append(name).append("\n");
        builder.append("{\n");
        builder.append("\tname \"Name\" : String(20);\n").append("}\n\n");
        builder.append(MMToken.MENU.getText()).append(" ").append(name).append("Menu").append(" \"").append(labelName).append("\"\n");
        builder.append("{\n");
        builder.append("\t").append(name).append(";\n");
        builder.append("}");
    }

    private void appendHandler(final String name, StringBuilder builder) {
        builder.append(MMToken.HANDLER.getText()).append(" ").append(name).append("\n");
        builder.append("{\n\t");
        builder.append("\"/path").append("/").append("\\$").append("parameter\" : Html, method;").append("\n}\n\n");
    }

    private void appendTask(final String name, StringBuilder builder) {
        builder.append(MMToken.TASK.getText()).append(" ").append(name).append(" ").append("\"").append(name).append("\";\n\n");
    }

    @Nullable private MMFile checkOrCreate(String newName, PsiDirectory directory, String templateName)
        throws IncorrectOperationException
    {
        final String                      extension = StringUtil.getShortName(templateName);
        final Tuple<PsiDirectory, String> dir       = FileUtils.checkAndCreateDirectoriesForFileName(directory, newName, extension);

        return doCreate(dir.first(), dir.second(), templateName);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private MMFile createFile(PsiDirectory psiDirectory, final String input, final String templateName)
        throws Exception
    {
        final String           name      = Strings.capitalizeFirst(input);
        final String           labelName = Strings.toWords(Strings.fromCamelCase(name));
        final FileTemplateBase template  = new MMFileTemplate(name);

        final Tuple<PsiDirectory, PsiPackage> dirPackage = FileUtils.getPackageForDirectory(psiDirectory);

        final String        qualifiedName = dirPackage.second().getQualifiedName();
        final StringBuilder builder       = new StringBuilder();
        builder.append(Constants.PACKAGE_SPC).append(qualifiedName).append(";\n\n");

        switch (templateName) {
        case FORM:
            appendForm(name, labelName, builder);
            break;
        case Constants.ENTITY:
            appendEntity(name, labelName, builder);
            break;
        case HANDLER:
            appendHandler(name, builder);
            break;
        case TASK:
            appendTask(name, builder);
            break;
        case ENUM:
            appendEnum(name, builder);
            break;
        }

        template.setText(builder.toString());
        return (MMFile) FileTemplateUtil.createFromTemplate(template, name, null, dirPackage.first());
    }  // end method createFile

    private MMFile doCreate(PsiDirectory dir, String className, String templateName)
        throws IncorrectOperationException
    {
        try {
            return createFile(dir, className, templateName);
        }
        catch (final Exception e) {
            throw new IncorrectOperationException("Error creating file", (Throwable) e);
        }
    }
}  // end class NewMMAction
