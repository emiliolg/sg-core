
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.cache.database.infinispan.InfinispanCacheManager;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.exception.SuiGenerisException;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.media.Mime;
import tekgenesis.common.util.Reflection;
import tekgenesis.form.DownloadImpl;
import tekgenesis.form.FormInstance;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.view.client.FormViewMessages;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.util.UUID.randomUUID;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.common.service.HeaderNames.CONTENT_DISPOSITION;
import static tekgenesis.common.service.HeaderNames.CONTENT_TYPE;
import static tekgenesis.form.Download.DownloadWriter;
import static tekgenesis.form.FormsImpl.createUserFormInstance;
import static tekgenesis.form.extension.FormExtensionRegistry.getLocalizedForm;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Servlet to handle form downloads.
 */
public class DownloadServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    @Override protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException
    {
        final DownloadParams path = new DownloadParams(req.getPathInfo());

        // Find persisted form model instance.
        final Option<Tuple<FormModel, DownloadImpl>> option = retrieve(path.uuid, path.fqn);

        if (option.isPresent()) {
            final Tuple<FormModel, DownloadImpl> tuple = option.get();

            final FormModel    model    = tuple.first();
            final DownloadImpl download = tuple.second();

            // Construct stream writer instance.
            final DownloadWriter writer = construct(model, download);

            // Prepare response headers.
            setHeaders(resp, download);

            runInTransaction(() -> write(resp, download, writer));
        }
        else resp.setStatus(HTTP_NOT_FOUND);  // Proper 404
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private DownloadWriter construct(@NotNull final FormModel model, @NotNull final DownloadImpl download) {
        final FormInstance<?>                 base           = createUserFormInstance(model).getInstance();
        final Class<? extends DownloadWriter> downloadWriter = download.getWriter();
        return Reflection.construct(downloadWriter, base);
    }

    /** Retrieve model and download previously persisted. */
    private Option<Tuple<FormModel, DownloadImpl>> retrieve(@NotNull final String uuid, @NotNull final String fqn) {
        final Form                                   form   = getLocalizedForm(fqn);
        final Map<Object, Object>                    map    = getFormInstancesMap();
        final FormModelDownload                      hz     = (FormModelDownload) map.get(uuid);
        final Option<Tuple<FormModel, DownloadImpl>> result;
        if (hz != null) {
            result = some(tuple(hz.init(form), hz.getDownload()));
            map.remove(uuid);
        }
        else result = Option.empty();
        return result;
    }

    private void write(HttpServletResponse resp, DownloadImpl download, DownloadWriter writer) {
        try {
            // Write into stream.
            if (download.isZipped()) {
                final ZipOutputStream zip = new ZipOutputStream(resp.getOutputStream());
                zip.putNextEntry(new ZipEntry(download.getFilename()));
                writer.into(zip);
                zip.finish();
            }
            else writer.into(resp.getOutputStream());
        }
        catch (final IOException e) {
            logger.error(e);
        }
    }

    private void setHeaders(@NotNull final HttpServletResponse r, @NotNull final DownloadImpl download) {
        final String filename = download.getFilename() + (download.isZipped() ? ".zip" : "");
        // noinspection DuplicateStringLiteralInspection
        r.setHeader(CONTENT_DISPOSITION, (download.isInBrowser() ? "inline" : "attachment") + "; filename=" + quoted(filename));

        if (download.isZipped()) r.setHeader(CONTENT_TYPE, Mime.APPLICATION_ZIP.getMime());
        else r.setHeader(CONTENT_TYPE, download.getContentType().getMime() + "; " + download.getCharacterEncoding());
    }

    //~ Methods ......................................................................................................................................

    /** Persist model and download for future retrieval. Return location. */
    public static String persist(@NotNull final FormModel model, @NotNull final DownloadImpl download) {
        final String              uuid = randomUUID().toString();
        final Map<Object, Object> map  = getFormInstancesMap();
        map.put(uuid, new FormModelDownload(model, download));
        return DownloadParams.createPath(uuid, model.getFormFullName());
    }

    private static Map<Object, Object> getFormInstancesMap() {
        final InfinispanCacheManager manager = getSingleton(InfinispanCacheManager.class);
        return manager.getCache(Constants.SUIGENERIS_FORM_INSTANCES_MAP);
    }

    //~ Static Fields ................................................................................................................................

    public static final Logger logger = Logger.getLogger(DownloadServlet.class);

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final long serialVersionUID = -6422211503408730778L;

    private static final String SEP  = "/";
    private static final String PATH = "/sg/download";

    //~ Inner Classes ................................................................................................................................

    private static class DownloadParams {
        private final String fqn;
        private final String uuid;

        private DownloadParams(@Nullable final String path) {
            final String[] split = check(path);
            fqn  = split[1];
            uuid = split[2];
        }

        @NotNull private String[] check(@Nullable final String path) {
            final String[] split = notNull(path, "").split(SEP);
            if (split.length != 3) throw new SuiGenerisException.Default(FormViewMessages.MSGS.invalidDownloadArgumentException());
            return split;
        }

        private static String createPath(@NotNull final String uuid, @NotNull final String fqn) {
            return PATH + SEP + fqn + SEP + uuid;
        }
    }
}  // end class DownloadServlet
