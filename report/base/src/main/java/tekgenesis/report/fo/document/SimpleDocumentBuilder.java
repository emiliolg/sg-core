
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.document;

import tekgenesis.report.fo.components.FoBuilder;

/**
 * Simple Document Builder.
 */
public class SimpleDocumentBuilder extends FoBuilder<SimpleDocumentBuilder, SimpleDocument> {

    //~ Instance Fields ..............................................................................................................................

    PageSequence pageSequence = null;

    SimplePageMaster simplePageMaster = null;

    //~ Methods ......................................................................................................................................

    public SimpleDocument build() {
        return new SimpleDocument(simplePageMaster, pageSequence);
    }

    /** Specify which PageSequence to use. */
    public SimpleDocumentBuilder withPageSequence(PageSequence ps) {
        pageSequence = ps;
        return this;
    }

    /** Specify which PageSequence to use. */
    public SimpleDocumentBuilder withPageSequence(PageSequenceBuilder ps) {
        pageSequence = ps.build();
        return this;
    }

    /** Specify which SimplePageMaster to use. */
    public SimpleDocumentBuilder withSimplePageMaster(SimplePageMaster spm) {
        simplePageMaster = spm;
        return this;
    }

    /** Specify which SimplePageMaster to use. */
    public SimpleDocumentBuilder withSimplePageMaster(SimplePageMasterBuilder spm) {
        simplePageMaster = spm.build();
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** @return  a new SimpleDocument */
    public static SimpleDocumentBuilder simpleDocument() {
        return new SimpleDocumentBuilder();
    }
}  // end class SimpleDocumentBuilder
