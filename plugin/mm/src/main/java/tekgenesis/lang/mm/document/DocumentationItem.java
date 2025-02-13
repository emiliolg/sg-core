
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.memetix.mst.language.Language;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.i18n.I18nBundle;
import tekgenesis.common.env.i18n.I18nMessagesFactory;
import tekgenesis.common.logging.Logger;
import tekgenesis.lang.mm.i18n.PluginMessages;
import tekgenesis.lang.mm.util.Item;
import tekgenesis.lang.mm.util.Retryable;
import tekgenesis.lang.mm.util.Utils;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.type.MetaModel;
import tekgenesis.type.permission.Permission;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Constants.MD_EXT;
import static tekgenesis.lang.mm.document.markdown.MarkdownMarkup.*;
import static tekgenesis.lang.mm.document.markdown.MarkdownToken.H1;
import static tekgenesis.lang.mm.document.markdown.MarkdownToken.H3;
import static tekgenesis.metadata.form.widget.ButtonType.CUSTOM;
import static tekgenesis.metadata.form.widget.UiModelLocalizer.localizer;

/**
 * Documentation Item Generation.
 */
public class DocumentationItem implements Item {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final File      file;
    private final Language           language;
    @NotNull private final MetaModel model;
    @NotNull private final File      propsFile;

    //~ Constructors .................................................................................................................................

    /**
     * Default Document.
     *
     * @param  model       model
     * @param  language    language
     * @param  mmRootPath  mm Root folder
     */
    public DocumentationItem(@NotNull MetaModel model, Language language, String mmRootPath) {
        file          = Utils.ensureDestinationFile(mmRootPath, model, language.toString() + MD_EXT, ARTIFACT_PATH);
        propsFile     = new File(Utils.getResourcesDirectoryPath(mmRootPath, null));
        this.model    = model;
        this.language = language;
    }

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings({ "DuplicateStringLiteralInspection", "SpellCheckingInspection" })
    public String process(@NotNull final Retryable retryable) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);

            final Locale locale       = new Locale(language.toString());
            final Form   localizeForm = getLocalizedForm(locale);

            final Seq<Permission> permissions = localizeForm.getPermissions();
            final List<Row>       fieldsMd    = new ArrayList<>();
            final List<Row>       actionsMd   = new ArrayList<>();

            generateMarkdown(localizeForm, fieldsMd, actionsMd);

            final PluginMessages msgs = I18nMessagesFactory.create(PluginMessages.class, locale);
            final StringWriter   sw   = new StringWriter();

            sw.write(h(H1, localizeForm.getLabel()));
            sw.write("\n\n");
            sw.write(italic(localizeForm.getFullName()));
            sw.write("\n\n");
            sw.write(h(H3, bold(msgs.overview())));
            sw.write("\n\n");

            sw.write(h(H3, bold(msgs.fields())));
            sw.write("\n\n");
            generateTable(sw, seq(fieldsMd), bold(msgs.fieldName()), bold(msgs.required()), bold(msgs.description()));

            sw.write(h(H3, bold(msgs.actions())));
            sw.write("\n\n");
            generateTable(sw, seq(actionsMd), bold(msgs.actionName()), bold(msgs.description()));

            sw.write(h(H3, bold(msgs.permissions())));
            sw.write("\n\n");
            sw.write(msgs.kindPermissions());
            sw.write("\n\n");
            // noinspection ClassEscapesDefinedScope

            generateTable(sw, Colls.map(permissions, value -> new Row(value.getName(), " ")), bold(msgs.permission()), bold(msgs.operation()));

            fileWriter.write(sw.toString());
        }
        catch (final IOException e) {
            LOGGER.error(e);
        }
        finally {
            if (fileWriter != null) try {
                fileWriter.close();}
            catch (final IOException e) {
                // ignore it
            }
        }

        return model.getFullName();
    }  // end method process

    /** @return  The generated file */
    @NotNull public File getFile() {
        return file;
    }

    private Row generateActionMarkdown(@NotNull Widget widget) {
        String text = widget.getLabel();
        if (isEmpty(text)) text = widget.getButtonType() == CUSTOM ? widget.getLabel() : widget.getButtonType().getId();
        return new Row(text, " ");
    }

    private void generateFieldMarkdown(final List<Row> fieldsMd, Widget widget) {
        // it is a form field
        final String text = isEmpty(widget.getLabel()) ? widget.getName() : widget.getLabel();
        fieldsMd.add(new Row(text, Boolean.toString(widget.isRequired()), " "));
    }

    private void generateGroupMarkdown(final List<Row> fieldsMd, Widget widget) {
        // it is a form field
        final String text = isEmpty(widget.getLabel()) ? widget.getName() : widget.getLabel();
        fieldsMd.add(new Row(text, " ", "**" + widget.getWidgetType() + "**"));
    }

    private void generateMarkdown(@NotNull Iterable<Widget> widgetModel, @NotNull List<Row> fieldsMd, @NotNull List<Row> actionsMd) {
        for (final Widget widget : widgetModel) {
            final WidgetType widgetType = widget.getWidgetType();
            switch (widgetType) {
            case NONE:
            case HEADER:
            case DIALOG:
            case INTERNAL:
                // ignore them
                break;
            case BUTTON:
                actionsMd.add(generateActionMarkdown(widget));
                break;
            case FORM:
                // Add Form link doc
                break;
            case HORIZONTAL:
            case VERTICAL:
            case FOOTER:
                generateMarkdown(widget, fieldsMd, actionsMd);
                break;
            case TABLE:
            case TABS:
                generateGroupMarkdown(fieldsMd, widget);
                generateMarkdown(widget, fieldsMd, actionsMd);
                break;
            // generateFieldMarkdown(fieldsMd, widget);
            // // For each column in the table
            // break;
            // case TABS:
            default:
                generateFieldMarkdown(fieldsMd, widget);
            }
        }
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private void generateTable(final StringWriter sw, final Seq<Row> rows, final String... columns) {
        sw.write("\n");
        sw.write(thead(columns));
        sw.write("\n");
        for (final Row row : rows) {
            sw.write(tr(row.columns));
            sw.write("\n");
        }
        sw.write("\n");
    }

    private Form getLocalizedForm(Locale locale)
        throws MalformedURLException
    {
        final ClassLoader loader = new URLClassLoader(new URL[] { propsFile.toURI().toURL() });
        final I18nBundle  bundle = I18nBundle.getBundle(model.getFullName()).setClassLoader(loader);
        return localizer((Form) model, locale, bundle).localize();
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String ARTIFACT_PATH = "help";

    private static final Logger LOGGER = Logger.getLogger(DocumentationItem.class);

    //~ Inner Classes ................................................................................................................................

    private final class Row {
        String[] columns;

        Row(String... columns) {
            this.columns = columns;
        }
    }
}  // end class DocumentationItem
