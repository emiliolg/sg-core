
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.websocket.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import org.slf4j.LoggerFactory;

/**
 */
public class Main {

    //~ Constructors .................................................................................................................................

    private Main() {}

    //~ Methods ......................................................................................................................................

    /**  */
    public static void main(String[] args)
        throws Exception
    {
        final Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.valueOf(System.getProperty("debug.level", "INFO")));

        if (args.length < 1) {
            System.err.println("Missing sub-command (proxy|forward)");
            System.exit(1);
        }

        final List<String> subArgs = new ArrayList<>(Arrays.asList(args));
        subArgs.remove(0);

        switch (args[0]) {
        // noinspection DuplicateStringLiteralInspection
        case "proxy":
            TrafficExchangeServer.main(subArgs);
            break;
        default:
            System.err.println("Invalid sub-command: " + args[0]);
            System.exit(1);
        }
    }
}
