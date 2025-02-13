
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
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.codec.binary.Base64InputStream;

import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.media.Mime;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.view.shared.utils.CropperServlet;

import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Receives a base 64 encoded image from the client.
 */
public class CameraServlet extends CropperServlet {

    //~ Instance Fields ..............................................................................................................................

    private final ResourceHandler resourceHandler = Context.getSingleton(ResourceHandler.class);

    //~ Methods ......................................................................................................................................

    @Override protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        final Part        content     = req.getPart("base64");
        final InputStream inputStream = content.getInputStream();
        final InputStream decoder     = getInputStream(req, new Base64InputStream(inputStream));
        final String      name        = "camera.png";
        final String      mimeType    = Mime.IMAGE_PNG.getMime();

        final Resource resource = invokeInTransaction(() ->
                    generateImageVariants(resourceHandler.create().upload(name, mimeType, decoder), content.getSize()));

        // send the content info back to the client
        final Resource.Entry master       = resource.getMaster();
        final Resource.Entry thumb        = resource.getThumb();
        final ObjectNode     jsonResponse = PostResponse.success(resource.getUuid(),
                false,
                name,
                master.getUrl(),
                thumb != null ? thumb.getUrl() : null,
                mimeType);

        resp.getOutputStream().print(jsonResponse.toString());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 236107739147468154L;
}  // end class CameraServlet
