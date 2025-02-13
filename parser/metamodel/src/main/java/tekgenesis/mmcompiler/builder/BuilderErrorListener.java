
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.io.PrintWriter;

import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.mmcompiler.ast.MetaModelAST;

/**
 * Listener that listens for builder errors.
 */
public interface BuilderErrorListener {

    //~ Methods ......................................................................................................................................

    // todo This should not exists !!!

    /** method used to manage BuilderErrors caused by Builder. */
    void error(BuilderError error);

    /** method used to manage BuilderErrors caused by Builder. */
    void error(MetaModelAST node, BuilderError error);

    /** Returns true if any error has been reported. */
    boolean hasErrors();

    //~ Inner Classes ................................................................................................................................

    /**
     * Create an Error Listener that prints arguments to Standard error.
     */
    class Default extends Silent {
        @Override public void error(BuilderError error) {
            super.error(error);
            logger.error(error.getMessage());
        }

        @Override public void error(MetaModelAST node, BuilderError error) {
            super.error(error);
            logger.error(node.getPosition().createMessage(error.getMessage()).getFormattedMessage());
        }

        private static final Logger logger = Logger.getLogger(Default.class);
    }

    class DefaultWriter extends Silent {
        final PrintWriter writer;

        /** Create an Error Listener that prints arguments to the specified writer. */
        public DefaultWriter(PrintWriter writer) {
            this.writer = writer;
        }

        @Override public void error(BuilderError error) {
            super.error(error);
            writer.println(" error:  " + error.getMessage());
        }

        @Override public void error(MetaModelAST node, BuilderError error) {
            super.error(error);
            writer.println(node.getPosition().createMessage(error.getMessage()).getFormattedMessage());
        }
    }

    class Silent implements BuilderErrorListener {
        private int errors = 0;

        @Override public void error(BuilderError error) {
            errors++;
        }

        @Override public void error(MetaModelAST node, BuilderError error) {
            errors++;
        }

        @Override public boolean hasErrors() {
            return errors > 0;
        }
    }
}  // end interface BuilderErrorListener
