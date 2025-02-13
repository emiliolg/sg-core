
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.translate;

import java.io.File;
import java.util.Map;

import com.intellij.openapi.project.Project;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.io.PropertyWriter;
import tekgenesis.lang.mm.translate.ui.InternetLostDialog;
import tekgenesis.lang.mm.ui.MMUIInformer;
import tekgenesis.lang.mm.util.Item;
import tekgenesis.lang.mm.util.Retryable;
import tekgenesis.lang.mm.util.Utils;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.core.Strings.toWords;
import static tekgenesis.lang.mm.translate.LocalizeManager.PROPERTY_EXTENSION;
import static tekgenesis.lang.mm.translate.TranslateProperties.PropertiesDelta;
import static tekgenesis.metadata.common.Localizer.stringsToLocalize;

/**
 * Represents a translation to be performed.
 */
class TranslationItem implements Item {

    //~ Instance Fields ..............................................................................................................................

    private boolean connected;

    @NotNull private final File file;

    @NotNull private final MetaModel model;
    @NotNull private final Language  origin;

    @NotNull private final Project             project;
    @NotNull private final Map<String, String> properties;
    @NotNull private final Language            translate;

    //~ Constructors .................................................................................................................................

    private TranslationItem(@NotNull Project project, @NotNull MetaModel model, @NotNull Map<String, String> properties, @NotNull Language origin,
                            @NotNull Language translate, @NotNull String rootPath) {
        this.project    = project;
        this.model      = model;
        this.properties = properties;
        this.origin     = origin;
        this.translate  = translate;
        file            = Utils.ensureDestinationFile(rootPath, model, translate.toString() + PROPERTY_EXTENSION, null);
    }

    //~ Methods ......................................................................................................................................

    @Override public String process(@NotNull Retryable retryable) {
        final StringBuilder message = new StringBuilder(model.getFullName()).append(" (").append(toWords(translate.name())).append(") > ");

        if (!properties.isEmpty()) {
            final PropertiesDelta delta = TranslateProperties.load(file).delta(properties);

            final PropertyWriter writer = new PropertyWriter(file);

            // Translate new properties
            final TranslateProperties props = translate(delta.getProperties(), connected, retryable);

            // Write new properties
            props.write(writer);

            writer.close();

            delta.message(message);
        }
        else message.append("Has no translatable properties.");

        return message.toString();
    }
    void setConnected(boolean connected) {
        this.connected = connected;
    }

    @NotNull private TranslateProperties translate(@NotNull TranslateProperties props, boolean isConnected, @NotNull Retryable retryable) {
        final TranslateProperties   result  = new TranslateProperties();
        final Map<String, Property> entries = props.getProperties();

        if (!entries.isEmpty()) {
            final String[] values = props.changed().map(Property::getLabel).toArray(String[]::new);
            // Translate all form values at once!!!
            final String[] translations = translate(values, isConnected, retryable);

            if (translations.length != values.length) MMUIInformer.showErrorBalloonPopUp(project, MSFT_TRANSLATOR_SERVICE_ERR_MSG);
            else {
                int i = 0;
                for (final Map.Entry<String, Property> entry : entries.entrySet()) {
                    final Property prop = entry.getValue();
                    if (prop.isChanged()) result.add(prop.getKey(), translations[i++], true, prop.getLabel(), prop.isAdded(), true);
                    else result.add(prop);
                }
            }
        }

        return result;
    }  // end method translate

    private String[] translate(@NotNull final String[] values, boolean isConnected, @NotNull final Retryable retryable) {
        String[] result = values;
        if (isConnected && retryable.shouldRetry()) {
            try {
                result = Translate.execute(values, origin, translate);
            }
            catch (final Exception e) {
                if (retryable.shouldRetry() && userAcceptRetrying()) result = translate(values, true, retryable);
                else retryable.stopRetrying();
            }
        }
        return result;
    }

    private boolean userAcceptRetrying() {
        final InternetLostDialog dialog = new InternetLostDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return dialog.getResponse();
    }

    //~ Methods ......................................................................................................................................

    @NotNull static TranslationItem item(@NotNull Project project, @NotNull final MetaModel model, @NotNull final Language origin,
                                         @NotNull final Language translation, @NotNull final String rootPath) {
        return new TranslationItem(project, model, stringsToLocalize(model), origin, translation, rootPath);
    }

    //~ Static Fields ................................................................................................................................

    private static final String MSFT_TRANSLATOR_SERVICE_ERR_MSG =
        "When contacting Microsoft Translation Service, some times, they return fewer translations than the requested ones. Please, try again.";
}  // end class TranslationItem
