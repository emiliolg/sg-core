
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.admin.sg.Views;
import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.service.Call;
import tekgenesis.common.service.Method;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;
import tekgenesis.type.MetaModel;
import tekgenesis.util.MMDumper;

import static tekgenesis.admin.RestHandler.getFqn;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.MD_EXT;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.common.util.Files.copy;
import static tekgenesis.metadata.form.model.FormConstants.CURRENT_FORM_ID;

/**
 * User class for Handler: HelpHandler
 */
public class DocumentationHandler extends DocumentationHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    DocumentationHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<Html> help(@NotNull final String fqn) {
        final String externalDocUrl = Context.getProperties(ApplicationProps.class).externalDocUrl;

        if (isNotEmpty(externalDocUrl)) {
            final Result<Html> redirect = redirect(new Call(Method.GET, externalDocUrl + "?fqn=" + fqn));
            return redirect.withHeader(CURRENT_FORM_ID, fqn);
        }

        final String helpMd = getHelpMd(fqn);
        if (helpMd != null) return ok(views.sgHelp(helpMd));

        final String helpIndexMd = getHelpMd(fqn + "/" + Constants.INDEX);
        if (helpIndexMd != null) return ok(views.sgHelp(helpIndexMd));

        return notFound(views.sgHelp(DOCUMENTATION_NOT_FOUND));
    }

    @NotNull @Override public Result<Html> helpIndex() {
        final String externalDocUrl = Context.getProperties(ApplicationProps.class).externalDocUrl;
        if (isNotEmpty(externalDocUrl)) return redirect(new Call(Method.GET, externalDocUrl));

        final String helpMd = getHelpMd(Constants.INDEX);
        if (helpMd == null) return notFound(views.sgHelp(INDEX_NOT_FOUND));
        return ok(views.sgHelp(helpMd));
    }

    @NotNull @Override public Result<String> viewSource(@NotNull final String path) {
        if (!AdminHandler.hasDeveloperRole()) return unauthorized();

        final ModelRepository repository = getSingleton(ModelRepository.class);
        final QName           key        = createQName(getFqn(path));

        return repository.getModel(key, Form.class).map(form -> {
                final Option<MetaModel> entityModel = repository.getModel(form.getBinding());

                final MMDumper dumper = MMDumper.createDumper(repository);
                for (final MetaModel metaModel : entityModel)
                    dumper.model(metaModel);
                dumper.model(form);

                return ok(dumper.toString()).withContentType(MediaType.TEXT_PLAIN);
            }).orElseGet(() -> notFound("Form \"" + key.getFullName() + "\" not found!"));
    }

    @Nullable private String getHelpMd(@NotNull String fqn) {
        final InputStream helpDocument = getHelpStream(fqn);
        if (helpDocument == null) return null;

        final StringWriter      writer = new StringWriter();
        final InputStreamReader in     = new InputStreamReader(helpDocument);
        copy(in, writer);
        return writer.toString();
    }

    @Nullable private InputStream getHelpStream(@NotNull String fqn) {
        final String language = Context.getContext().getLocale().getLanguage();
        final String baseName = "help/" + Strings.split(fqn, '.').mkString("/");

        final InputStream result = getResourceAsStream(language, baseName, baseName + "_" + language, MD_EXT);
        return result != null ? result : getResourceAsStream("", baseName, baseName, MD_EXT);
    }

    @Nullable private InputStream getResourceAsStream(String language, String baseName, String resource, String ext) {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource + ext);
        if (resourceAsStream == null) {
            final String packageName = baseName + "/package-info_" + language + ext;
            resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(packageName);
        }
        return resourceAsStream;
    }

    //~ Static Fields ................................................................................................................................

    private static final String INDEX_NOT_FOUND         = "# Index not found";
    private static final String DOCUMENTATION_NOT_FOUND = "# Documentation not found";
}  // end class DocumentationHandler
