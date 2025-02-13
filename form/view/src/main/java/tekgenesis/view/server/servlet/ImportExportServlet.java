
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
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.admin.AdminHandler;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Resource;
import tekgenesis.common.media.Mime;
import tekgenesis.common.media.Mimes;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.persistence.etl.EntityEtl;
import tekgenesis.persistence.etl.EntityExporter;
import tekgenesis.persistence.etl.ImporterService;
import tekgenesis.persistence.resource.DbResource;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.persistence.TableMetadata.localEntities;
import static tekgenesis.persistence.etl.EntityEtl.*;
import static tekgenesis.persistence.etl.EntityExporter.export;
import static tekgenesis.view.server.servlet.Servlets.UNAUTHORIZED_USER;

/**
 * Servlet to handle Import/Export operations.
 */
public class ImportExportServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    @Override protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException
    {
        if (!AdminHandler.hasAdminRole()) resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_USER);
        else {
            final QName param = resolve(notNull(req.getPathInfo(), "/"));

            final ResponseType type     = ResponseType.resolve(req.getParameter("type"));
            final String       encoding = req.getParameter("encoding");

            if (!param.getName().isEmpty()) exportEntity(resp, param.getFullName(), type, encoding);
            else exportDomain(resp, param.getQualification(), type, encoding);
        }
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doGet(req, resp);
    }

    private void exportDomain(HttpServletResponse resp, final String name, ResponseType type, String encoding)
        throws IOException
    {
        final ZipOutputStream zip = getZipOutputStream(resp, name);
        zipDomains(name, type, encoding, zip);
        zip.finish();
    }

    private void exportEntity(HttpServletResponse resp, String fqn, ResponseType type, String encoding)
        throws IOException
    {
        final EntityExporter<?, ?> exporter = export(QName.createQName(fqn)).using(type.builder());

        final List<Resource> resources = exporter.resources();
        if (!resources.isEmpty()) {
            final ZipOutputStream zipOutputStream = getZipOutputStream(resp, fqn);
            zipEntity(type, encoding, zipOutputStream, fqn, resources, new HashSet<>());
            zipOutputStream.close();
        }
        else {
            resp.setContentType(type.getContentType());
            final String fileName = fqn + "." + type.name().toLowerCase();
            setHeaderAttachmentName(resp, fileName);

            if (isNotEmpty(encoding)) exporter.encoding(encoding);
            exporter.into(resp.getOutputStream());
        }
    }  // end method exportEntity

    private void exportResources(ZipOutputStream zip, Resource resource)
        throws IOException
    {
        for (final Resource.Entry entry : resource.getEntries()) {
            @SuppressWarnings("DuplicateStringLiteralInspection")
            final String name = ImporterService.RESOURCES_FOLDER + "/" + resource.getUuid() + "/" + entry.getVariant() + "/" +
                                (notEmpty(entry.getName(), entry.isExternal() ? DbResource.UNNAMED : entry.getSha()));
            zip.putNextEntry(new ZipEntry(name + ".info"));
            if (entry.isExternal()) {
                zip.write(ImporterService.EXTERNAL.getBytes(UTF8));
                zip.putNextEntry(new ZipEntry(name));
                zip.write(entry.getUrl().getBytes(UTF8));
            }
            else {
                zip.write(entry.getMimeType().getBytes(UTF8));
                zip.putNextEntry(new ZipEntry(name));

                if (Mimes.isText(entry.getMimeType())) {
                    final StringWriter writer = new StringWriter();
                    entry.copyTo(writer);
                    zip.write(writer.toString().getBytes(UTF8));
                }
                else entry.copyTo(zip);
            }
            zip.flush();
        }
    }

    @NotNull private QName resolve(@NotNull final String path) {
        final QName result;

        final String[] split = path.split("/");
        switch (split.length) {
        case 0:
        case 1:
            result = QName.EMPTY;
            break;
        case 2:
            result = QName.createQName(split[1], "");
            break;
        case 3:
            result = QName.createQName(split[1], split[2]);
            break;
        default:
            throw new IllegalArgumentException("I/O invalid URL. Should be: /<domain>/<entity>");
        }
        return result;
    }

    private void zipDomains(final String domain, ResponseType type, String encoding, ZipOutputStream zip)
        throws IOException
    {
        final HashSet<String> exportedResources = new HashSet<>();
        final Seq<String>     entities          = localEntities(getEnvironment()).filter(s -> isEmpty(domain) || s != null && s.startsWith(domain));

        for (final String table : entities)
            zipEntity(type, encoding, zip, table, exportedResources);
    }

    private void zipEntity(ResponseType type, String encoding, ZipOutputStream zip, String table, Set<String> exportedResources)
        throws IOException
    {
        zipEntity(type, encoding, zip, table, null, exportedResources);
    }

    private void zipEntity(ResponseType type, String encoding, ZipOutputStream zip, String table, @Nullable List<Resource> resources,
                           Set<String> exportedResources)
        throws IOException
    {
        zip.putNextEntry(new ZipEntry(table + "." + type.getExt()));
        final EntityExporter<?, ?> exporter = export(table).using(type.builder());
        if (isNotEmpty(encoding)) exporter.encoding(encoding);
        exporter.into(zip);
        zip.flush();
        final List<Resource> resourcesList = resources == null ? exporter.resources() : resources;
        for (final Resource resource : resourcesList) {
            if (!exportedResources.contains(resource.getUuid())) {
                exportResources(zip, resource);
                exportedResources.add(resource.getUuid());
            }
        }
    }

    private void setHeaderAttachmentName(HttpServletResponse r, String fileName) {
        r.setHeader(HeaderNames.CONTENT_DISPOSITION, "attachment; fileName=" + fileName);
    }

    private ZipOutputStream getZipOutputStream(HttpServletResponse resp, String name)
        throws IOException
    {
        resp.setContentType(Mime.APPLICATION_ZIP.getMime());
        setHeaderAttachmentName(resp, notEmpty(name, "repository") + ".zip");

        final ZipOutputStream zip = new ZipOutputStream(resp.getOutputStream());
        zip.setLevel(ZipOutputStream.DEFLATED);
        return zip;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6422211503408730778L;

    //~ Enums ........................................................................................................................................

    @SuppressWarnings("UnusedDeclaration")
    private enum ResponseType {
        CSV(Mime.TEXT_CSV) { @Override EntityEtl.Builder builder() { return csv(); } },
        JSON(Mime.APPLICATION_JSON) { @Override EntityEtl.Builder builder() { return json(); } },
        XML(Mime.APPLICATION_XML) { @Override EntityEtl.Builder builder() { return xml(); } };

        private final Mime type;

        ResponseType(Mime type) {
            this.type = type;
        }

        abstract EntityEtl.Builder builder();
        String getExt() {
            return name().toLowerCase();
        }

        private String getContentType() {
            return type.getMime();
        }

        static ResponseType resolve(@Nullable String value) {
            return isEmpty(value) ? CSV : valueOf(value.toUpperCase());
        }
    }
}  // end class ImportExportServlet
