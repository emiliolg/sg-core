
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.pdf.PDFEncryptionParams;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import tekgenesis.report.fo.document.SimpleDocument;

import static tekgenesis.common.Predefined.cast;

/**
 * FopBuilder, class that builds fo elements into a PDF or String.
 */
public class FopBuilder {

    //~ Constructors .................................................................................................................................

    private FopBuilder() {}

    //~ Methods ......................................................................................................................................

    /** Do Build. */
    public static String build(SimpleDocument simpleDoc) {
        final Document            doc        = simpleDoc.buildDocument();
        final DOMImplementationLS domImplLS  = (DOMImplementationLS) doc.getImplementation();
        final LSSerializer        serializer = domImplLS.createLSSerializer();
        return serializer.writeToString(doc);
    }

    /** Do Build. */
    public static void build(SimpleDocument document, OutputStream out)
        throws FopBuildException
    {
        build(document, out, true);
    }

    /** Do Build. */
    public static void build(SimpleDocument document, File file)
        throws FopBuildException
    {
        try(final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            build(document, bufferedOutputStream);
        }
        catch (final IOException e) {
            throw new FopBuildException(e);
        }
    }

    /** Do Build. */
    public static void build(SimpleDocument document, OutputStream out, boolean encrypted)
        throws FopBuildException
    {
        try {
            // Step 3: Construct fop with desired output format
            final FOUserAgent userAgent = fopFactory.newFOUserAgent();

            if (encrypted) {
                final Map<String, PDFEncryptionParams> rendererOptions = cast(userAgent.getRendererOptions());
                rendererOptions.put("encryption-params", new PDFEncryptionParams(null, null, true, false, false, false));
            }

            final Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, out);

            // Step 4: Setup JAXP using identity transformer
            final TransformerFactory factory     = TransformerFactory.newInstance();
            final Transformer        transformer = factory.newTransformer();

            // Step 5: Setup input and output for XSLT transformation
            // Setup input stream
            final DOMSource src = new DOMSource(document.buildDocument());
            // val src = new StreamSource(new StringReader (document.build()))

            // Resulting SAX events (the generated FO) must be piped through to FOP
            final SAXResult res = new SAXResult(fop.getDefaultHandler());

            // Step 6: Start XSLT transformation and FOP processing
            transformer.transform(src, res);
        }
        catch (final FOPException | TransformerException e) {
            throw new FopBuildException(e);
        }
    }

    private static DocumentBuilder createDocBuilder() {
        try {
            return docFactory.newDocumentBuilder();
        }
        catch (final ParserConfigurationException e) {
            throw new Error(e);
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final FopFactory             fopFactory = FopFactory.newInstance();
    public static final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    public static DocumentBuilder              docBuilder = createDocBuilder();
}  // end class FopBuilder
