
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Error Servlet to handle unexpected errors.
 */
public class ErrorServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        final StringBuilder sb = new StringBuilder();

        sb.append(
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>Error</title>\n" +
            "    <link rel=\"icon\" href=\"/public/sg/img/favicon.png\">\n" +
            "    <style>\n" +
            "        div {\n" +
            "            font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n" +
            "            margin: 30px;\n" +
            "            padding: 10px 0;\n" +
            "            text-align: center;\n" +
            "            color: #b94a48;\n" +
            "            background-color: #f2dede;\n" +
            "            border-color: #eed3d7;\n" +
            "            border-radius: 4px;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<div>\n");

        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == 404) sb.append("Not Found\n");
        else sb.append("Error ").append(statusCode).append("\n").append("    <a href=\"javascript:window.location.reload(true);\">Reload</a>\n");

        final Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (exception != null) sb.append("    <p>").append(exception.getMessage()).append(" </p>\n");

        final String message = (String) request.getAttribute("javax.servlet.error.message");
        if (message != null) sb.append("    <p>").append(message).append("</p>\n");

        sb.append("</div>\n" +
            "</body>\n" +
            "</html>");

        response.getOutputStream().print(sb.toString());
    }  // end method doGet

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {}

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3209195757268456606L;
}  // end class ErrorServlet
