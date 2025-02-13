
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo;

import org.junit.Test;

import tekgenesis.report.fo.components.BlockB;
import tekgenesis.report.fo.components.InlineBuilder;
import tekgenesis.report.fo.components.TableB;
import tekgenesis.report.fo.components.TableCellB;
import tekgenesis.report.fo.components.TableColumnB;
import tekgenesis.report.fo.components.TableRowB;
import tekgenesis.report.fo.document.PageSequenceBuilder;
import tekgenesis.report.fo.document.SimpleDocument;
import tekgenesis.report.fo.document.SimplePageMasterBuilder;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.report.fo.components.BlockB.block;
import static tekgenesis.report.fo.components.ExternalGraphicB.externalGraphic;
import static tekgenesis.report.fo.components.TableB.table;
import static tekgenesis.report.fo.components.TableCellB.tableCell;
import static tekgenesis.report.fo.components.TableColumnB.tableColumn;
import static tekgenesis.report.fo.components.TableRowB.tableRow;
import static tekgenesis.report.fo.document.PageSequenceBuilder.pageSequence;
import static tekgenesis.report.fo.document.SimpleDocumentBuilder.simpleDocument;

@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class FopBuilderTest {

    //~ Methods ......................................................................................................................................

    @Test public void fopComplexBuilderTest() {
        final String build = FopBuilder.build(buildComplex());
        assertThat(build).isEqualTo(
            "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n" +
            "<root xmlns=\"http://www.w3.org/1999/XSL/Format\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><layout-master-set xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><simple-page-master margin-bottom=\"10mm\" margin-left=\"10mm\" margin-right=\"10mm\" margin-top=\"10mm\" master-name=\"content\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><region-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><region-before xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><region-after xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/></simple-page-master></layout-master-set><page-sequence master-reference=\"content\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><flow flow-name=\"xsl-region-body\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-column column-width=\"45%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"55%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell border-color=\"#0079DB\" border-style=\"solid\" border-width=\"1mm\" padding=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-column column-width=\"45mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"45mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block text-align=\"center\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><external-graphic content-height=\"scale-to-fit\" content-width=\"auto\" height=\"15mm\" src=\"garbarinoviajes.jpg\" width=\"25mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/></block></table-cell><table-cell padding-before=\"3mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block color=\"grey\" font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Garbarino Viajes S.A.</block><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Leg Nº 12.541 Cabildo 2025</block><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">1er Piso - C.A.B.A</block><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">(011)153-260-4551</block></block></table-cell></table-row></table-body></table></block></table-cell><table-cell border-color=\"#0079DB\" border-style=\"solid\" border-width=\"1mm\" padding=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-column column-width=\"75%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"25%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" font-weight=\"bold\" padding-before=\"3mm\" padding-left=\"2mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Número de Referencia <inline font-size=\"8pt\" font-weight=\"normal\" padding-bottom=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">123456</inline></block><block font-size=\"8pt\" font-weight=\"bold\" padding-before=\"3mm\" padding-left=\"2mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Voucher emitido el: <inline font-size=\"8pt\" font-weight=\"normal\" padding-bottom=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">03/05/1990</inline></block><block font-size=\"8pt\" font-weight=\"bold\" padding-before=\"3mm\" padding-left=\"2mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Referencia del Proveedor: <inline font-size=\"12pt\" font-weight=\"normal\" padding-bottom=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">123456</inline></block><block font-size=\"8pt\" font-weight=\"bold\" padding-before=\"3mm\" padding-left=\"2mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Telefono emergencia: <inline font-size=\"12pt\" font-weight=\"normal\" padding-bottom=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">15326604551</inline></block></block></table-cell></table-row></table-body></table></block></table-cell></table-row><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell border-color=\"#0079DB\" border-style=\"solid\" border-width=\"1mm\" number-columns-spanned=\"2\" padding=\"1mm\" padding-left=\"3mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-column column-width=\"25mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"75mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"25mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"75mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell padding-bottom=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Hotel</block></table-cell><table-cell xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Sofitel</block></table-cell></table-row><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell padding-bottom=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Dirección</block></table-cell><table-cell xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Arroyo 871</block></table-cell><table-cell xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Ciudad</block></table-cell><table-cell xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Buenos Aires</block></table-cell></table-row><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell padding-bottom=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Teléfono</block></table-cell><table-cell xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">43941060</block></table-cell><table-cell xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Fax</block></table-cell><table-cell xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">11111111</block></table-cell></table-row></table-body></table><block font-size=\"8pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Nombre del titular de la reserva:<inline font-weight=\"normal\" padding-left=\"10mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">contacto</inline></block></block></table-cell></table-row><table-row border-color=\"#0079DB\" border-style=\"solid\" border-width=\"1mm\" height=\"50mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell number-columns-spanned=\"2\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-column column-width=\"125mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"50mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell padding=\"1mm\" padding-left=\"3mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Double twin Standard Room</block><block margin-left=\"2mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Desde 20/06/2014 hasta 25/06/2014 (1 Adl, 1 Niño)</block></block><block font-size=\"8pt\" padding-before=\"3mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Pasajeros:</block><block font-size=\"8pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Pasajero 1</block><block font-size=\"8pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Pasajero 2</block><block font-size=\"8pt\" padding-before=\"2mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Observaciones:</block><block font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Atención!! Esta reserva no admite modificaciones. Si desea cambiar algún dato (fechas, número\n" +
            "de habitaciones, etc) tendrá que realizar una reserva nueva y a continuación cancelar la\n" +
            "anterior.</block></block></table-cell><table-cell padding-top=\"1mm\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><external-graphic content-height=\"scale-to-fit\" content-width=\"scale-to-fit\" height=\"45mm\" src=\"http://maps.googleapis.com/maps/api/staticmap?center=-34.591287,-58.379345&amp;zoom=13&amp;size=250x250&amp;maptype=roadmap&amp;markers=icon:http://garbarinoviajes.com.ar/img/marker.png&amp;label:este-34.591287,-58.379345&amp;sensor=false\" width=\"45mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/></block></table-cell></table-row></table-body></table></block></table-cell></table-row><table-row border-color=\"#0079DB\" border-style=\"solid\" border-width=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell number-columns-spanned=\"2\" padding=\"3mm\" padding-left=\"3mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Reservado y pagadero por TRANSHOTEL</block></table-cell></table-row><table-row background-color=\"#0079DB\" border-color=\"#0079DB\" border-style=\"solid\" border-width=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell number-columns-spanned=\"2\" padding=\"1mm\" padding-left=\"3mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block color=\"white\" font-size=\"10pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Gracias por elegirnos</block></table-cell></table-row></table-body></table><block id=\"last-block\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/></flow></page-sequence></root>");

        final String invoice = FopBuilder.build(buildInvoice());
        assertThat(invoice).isEqualTo(
            "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n" +
            "<root xmlns=\"http://www.w3.org/1999/XSL/Format\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><layout-master-set xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><simple-page-master margin-bottom=\"10mm\" margin-left=\"10mm\" margin-right=\"10mm\" margin-top=\"10mm\" master-name=\"content\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><region-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><region-before xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><region-after xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/></simple-page-master></layout-master-set><page-sequence master-reference=\"content\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><flow flow-name=\"xsl-region-body\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block margin-bottom=\"20px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-column column-width=\"45%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"55%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell border-width=\"1mm\" padding=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-column column-width=\"45mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"45mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell padding-before=\"3mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block color=\"#4B4B4B\" font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block margin-bottom=\"10px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">From:</block><block font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Garbarino S.A.</block><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Leg Nº 12.541 Cabildo 2025</block><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">1er Piso - C.A.B.A</block><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Tel: (011)153-260-4551</block></block></table-cell></table-row></table-body></table></block></table-cell><table-cell border-width=\"1mm\" padding=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-column column-width=\"100%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-row xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block color=\"#4B4B4B\" font-size=\"8pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Invoice No.</block><block color=\"#da0000\" font-size=\"9pt\" font-weight=\"bold\" margin-bottom=\"10px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">INV-000567F7-00</block><block margin-bottom=\"10px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">To:</block><block font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Sofía Braun.</block><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Boluogne Sur Mer 1430</block><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">1er Piso - C.A.B.A</block><block margin-bottom=\"10px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Tel: (011)1234567</block><block font-size=\"8pt\" font-weight=\"bold\" padding-left=\"2mm\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Invoice Date: <inline font-size=\"8pt\" font-weight=\"normal\" padding-bottom=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">24/06/2014</inline></block><block font-size=\"8pt\" font-weight=\"bold\" padding-left=\"2mm\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Due Date: <inline font-size=\"8pt\" font-weight=\"normal\" padding-bottom=\"1mm\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">30/06/2014</inline></block></block></table-cell></table-row></table-body></table></block></table-cell></table-row></table-body></table></block><table xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-column column-width=\"61%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"13%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"13%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-column column-width=\"13%\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><table-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-row border-bottom=\"solid\" border-color=\"#C0C0C0\" border-width=\"1px\" color=\"#4B4B4B\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell font-family=\"open-sans\" padding-left=\"3px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"10pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Product</block></table-cell><table-cell font-family=\"open-sans\" padding-left=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"10pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Quantity</block></table-cell><table-cell font-family=\"open-sans\" padding-left=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"10pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Unit Price</block></table-cell><table-cell font-family=\"open-sans\" padding-left=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"10pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Total Price</block></table-cell></table-row><table-row border-bottom=\"solid\" border-color=\"#C0C0C0\" border-width=\"1px\" color=\"#4B4B4B\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell font-family=\"open-sans\" padding=\"3px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">imac</block><block font-size=\"9pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Esta es la primer descripción</block></block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">1</block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">10000</block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">10000</block></table-cell></table-row><table-row border-bottom=\"solid\" border-color=\"#C0C0C0\" border-width=\"1px\" color=\"#4B4B4B\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell font-family=\"open-sans\" padding=\"3px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">led</block><block font-size=\"9pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Esta es la segunda descripción</block></block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">2</block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">5000</block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">10000</block></table-cell></table-row><table-row border-bottom=\"solid\" border-color=\"#C0C0C0\" border-width=\"1px\" color=\"#4B4B4B\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell font-family=\"open-sans\" padding=\"3px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">plancha</block><block font-size=\"9pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Esta es la tercer descripción</block></block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">3</block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">1000</block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">3000</block></table-cell></table-row><table-row border-bottom=\"solid\" border-color=\"#C0C0C0\" border-width=\"1px\" color=\"#4B4B4B\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><table-cell font-family=\"open-sans\" padding=\"3px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" font-weight=\"bold\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">ps4</block><block font-size=\"9pt\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">Esta es la cuarta descripción</block></block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">4</block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">1000</block></table-cell><table-cell font-family=\"open-sans\" padding=\"3px\" text-align=\"right\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block font-size=\"9pt\" margin-top=\"5px\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">4000</block></table-cell></table-row></table-body></table><block id=\"last-block\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/></flow></page-sequence></root>");
    }

    @Test public void fopSimpleBuilderTest() {
        final String build = FopBuilder.build(buildSimple());
        assertThat(build).isEqualTo(
            "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n" +
            "<root xmlns=\"http://www.w3.org/1999/XSL/Format\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><layout-master-set xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><simple-page-master margin-bottom=\"10mm\" margin-left=\"10mm\" margin-right=\"10mm\" margin-top=\"10mm\" master-name=\"content\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><region-body xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><region-before xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/><region-after xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/></simple-page-master></layout-master-set><page-sequence master-reference=\"content\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><flow flow-name=\"xsl-region-body\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><block xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">hola mundo</block><block id=\"last-block\" xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"/></flow></page-sequence></root>");
    }

    //~ Methods ......................................................................................................................................

    public static BlockB blockHeaderRight(String value, InlineBuilder inline) {
        return block().content(value).children(inline).paddingBefore("3mm").paddingLeft("2mm").bold().size("8pt");
    }

    public static TableRowB fifthRow() {
        final TableCellB tableCell = tableCell().child(block().content("Gracias por elegirnos").color("white").bold().size("10pt"))
                                     .padding("1mm")
                                     .paddingLeft("3mm")
                                     .span("2");
        return tableRow().children(tableCell).borderWidth("1mm").background(BLUE_COLOR).borderStyle("solid").borderColor(BLUE_COLOR);
    }

    public static TableRowB fourthRow() {
        final TableCellB tableCell = tableCell().child(block().content("Reservado y pagadero por TRANSHOTEL").size("8pt"))
                                     .padding("3mm")
                                     .paddingLeft("3mm")
                                     .span("2");
        return tableRow().children(tableCell).borderWidth("1mm").borderColor(BLUE_COLOR).borderStyle("solid");
    }

    public static TableB garbaHeader() {
        final String     tel = "(011)153-260-4551";
        final TableCellB img = tableCell().child(
                block().content("")  //
                       .children(
                           externalGraphic().src("garbarinoviajes.jpg")
                                     .height("15mm")
                                     .width("25mm")
                                     .contentWidth(Fo.AUTO)
                                     .contentHeight(Fo.SCALE_TO_FIT))
                       .alignCenter());

        final TableCellB   gvdesc = tableCell().child(
                    block().content("")
                       .children(block().content("Garbarino Viajes S.A."),
                           block().content("Leg Nº 12.541 Cabildo 2025"),
                           block().content("1er Piso - C.A.B.A"),
                           block().content(tel))
                       .size("8pt")
                       .color("grey")).paddingBefore("3mm");
        final TableColumnB tc     = tableColumn().columnWidth("45mm");
        return table().columns(tc, tc).rows(tableRow().children(img, gvdesc));
    }

    public static TableB hotelTable() {
        final TableRowB firstRow = tableRow().children(tableCell().child(block().content("Hotel").bold().size("8pt")).paddingBottom("1mm"),
                tableCell().child(block().content("Sofitel").size("8pt")));

        final TableRowB secondRow = tableRow().children(tableCell().child(block().content("Dirección").bold().size("8pt")).paddingBottom("1mm"),
                tableCell().child(block().content("Arroyo 871").size("8pt")),
                tableCell().child(block().content("Ciudad").bold().size("8pt")),
                tableCell().child(block().content("Buenos Aires").size("8pt")));
        final TableRowB thirdRow  = tableRow().children(tableCell().child(block().content("Teléfono").bold().size("8pt")).paddingBottom("1mm"),
                tableCell().child(block().content("43941060").size("8pt")),
                tableCell().child(block().content("Fax").bold().size("8pt")),
                tableCell().child(block().content("11111111").size("8pt")));

        final TableColumnB tc1 = tableColumn().columnWidth("25mm");
        final TableColumnB tc2 = tableColumn().columnWidth("75mm");

        return table().columns(tc1, tc2, tc1, tc2).rows(firstRow, secondRow, thirdRow);
    }

    public static InlineBuilder inline(String value, String size) {
        return InlineBuilder.inline().content(value).bold().size(size).paddingBottom("1mm").normal();
    }

    public static BlockB invoiceBlockHeaderRight(String value, InlineBuilder inline) {
        return block().content(value).children(inline).paddingLeft("2mm").bold().size("8pt").alignRight();
    }

    public static TableB providerHeader() {
        final TableCellB cellHeaderRight = tableCell()  //
                                           .child(
                block().content("")  //
                       .children(blockHeaderRight("Número de Referencia ", inline("123456", "8pt")),
                           blockHeaderRight("Voucher emitido el: ", inline("03/05/1990", "8pt")),
                           blockHeaderRight("Referencia del Proveedor: ", inline("123456", "12pt")),
                           blockHeaderRight("Telefono emergencia: ", inline("15326604551", "12pt"))));

        return table().columns(tableColumn().columnWidth("75%"), tableColumn().columnWidth("25%")).rows(tableRow().children(cellHeaderRight));
    }

    public static TableRowB secondRow() {
        final TableB     table        = hotelTable();
        final BlockB     contactBlock = block().content("Nombre del titular de la reserva:")
                                        .bold()
                                        .children(InlineBuilder.inline().content("contacto").paddingLeft("10mm").normal())
                                        .size("8pt");
        final TableCellB tableCell    = tableCell().child(block().content("").children(table, contactBlock))
                                        .padding("1mm")
                                        .paddingLeft("3mm")
                                        .borderWidth("1mm")
                                        .borderColor(BLUE_COLOR)
                                        .borderStyle("solid")
                                        .span("2");
        return tableRow().children(tableCell);
    }

    public static TableRowB thirdRow() {
        final BlockB blockServices   = block().children(block().content("Double twin Standard Room"),
                block().content("Desde 20/06/2014 hasta 25/06/2014 (1 Adl, 1 Niño)").marginLeft("2mm"))
                                       .size("8pt")
                                       .bold();
        final BlockB passengersTitle = block().content("Pasajeros:").size("8pt").paddingBefore("3mm");

        final BlockB observations = block().content(
                    "Atención!! Esta reserva no admite modificaciones. Si desea cambiar algún " +
                    "dato (fechas, número\nde habitaciones, etc) tendrá que realizar una reserva nueva " +
                    "y a continuación cancelar la\nanterior.")
                                    .size("8pt");

        final BlockB block = block().children(blockServices)
                             .children(passengersTitle)
                             .children(block().content("Pasajero 1").size("8pt").bold(), block().content("Pasajero 2").size("8pt").bold())
                             .children(block().content("Observaciones:").size("8pt").paddingBefore("2mm"), observations);

        final String x = "-34.591287";
        final String y = "-58.379345";

        final String src = "http://maps.googleapis.com/maps/api/staticmap?center=" + x + "," + y +
                           "&zoom=13&size=250x250&maptype=roadmap&markers=icon:http://garbarinoviajes.com.ar/img/marker.png&label:este" + x + "," +
                           y + "&sensor=false";
        final BlockB map = block().children(
                externalGraphic().src(src).width("45mm").height("45mm").contentHeight(Fo.SCALE_TO_FIT).contentWidth(Fo.SCALE_TO_FIT));

        final TableB table = table().columns(tableColumn().columnWidth("125mm"), tableColumn().columnWidth("50mm"))
                             .rows(
                tableRow().children(tableCell().child(block).padding("1mm").paddingLeft("3mm"),
                    tableCell().child(map).paddingTop("1mm").alignRight()));

        final TableCellB cell = tableCell().child(block().children(table)).span("2");
        return tableRow().children(cell).borderWidth("1mm").borderColor(BLUE_COLOR).borderStyle("solid").height("50mm");
    }  // end method thirdRow

    private static SimpleDocument buildComplex() {
        final PageSequenceBuilder     page             = pageSequence().withName("content").addChildren(tableContent());
        final SimplePageMasterBuilder simplePageMaster = SimplePageMasterBuilder.simplePageMaster()
                                                         .masterName("content")
                                                         .marginBottom("10mm")
                                                         .marginLeft("10mm")
                                                         .marginRight("10mm")
                                                         .marginTop("10mm");
        return simpleDocument().withSimplePageMaster(simplePageMaster).withPageSequence(page).build();
    }

    private static SimpleDocument buildInvoice() {
        final TableRowB product1 = product("imac", "Esta es la primer descripción", "1", "10000", "10000");
        final TableRowB product2 = product("led", "Esta es la segunda descripción", "2", "5000", "10000");
        final TableRowB product3 = product("plancha", "Esta es la tercer descripción", "3", "1000", "3000");
        final TableRowB product4 = product("ps4", "Esta es la cuarta descripción", "4", "1000", "4000");

        final TableRowB productTableRow = tableRow()  //
                                          .children(
                    tableCell().child(block().content("Product").bold().size("10pt")).paddingLeft("3px").family("open-sans"),
                    tableCell().child(block().content("Quantity").bold().size("10pt")).alignRight().paddingLeft("3px").family("open-sans"),
                    tableCell().child(block().content("Unit Price").bold().size("10pt")).alignRight().paddingLeft("3px").family("open-sans"),
                    tableCell().child(block().content("Total Price").bold().size("10pt")).alignRight().paddingLeft("3px").family("open-sans"))
                                          .borderColor("#C0C0C0")
                                          .borderWidth("1px")
                                          .borderBottom("solid")
                                          .color("#4B4B4B");

        //J-
        final TableB clientTable = table()
                   .columns(tableColumn().columnWidth("100%"))
                   .rows(tableRow()
                         .children(tableCell().child(
                                 block().content("")
                                 .children(
                                    block().content("Invoice No.").size("9pt").bold(),
                                    block().content("INV-000567F7-00").size("9pt").bold().color( "#da0000").marginBottom("10px"),
                                    block().content("To:").marginBottom("10px"),
                                    block().content("Sofía Braun.").bold(),
                                    block().content("Boluogne Sur Mer 1430"),
                                    block().content("1er Piso - C.A.B.A"),
                                    block().content("Tel: (011)1234567").marginBottom("10px"),
                                    invoiceBlockHeaderRight("Invoice Date: ",
                                                            inline("24/06/2014", "8pt")),
                                    invoiceBlockHeaderRight("Due Date: ",
                                                            inline("30/06/2014", "8pt"))).size("8pt").color("#4B4B4B"))
                                                            .alignRight()));
        //J+
        final TableB productsTable = table().columns(tableColumn().columnWidth("61%"),
                    tableColumn().columnWidth("13%"),
                    tableColumn().columnWidth("13%"),
                    tableColumn().columnWidth("13%"))
                                     .rows(productTableRow, product1, product2, product3, product4);

        //J-
        final TableB garbaHeader =
            table().columns(tableColumn().columnWidth("45mm"), tableColumn().columnWidth("45mm"))
                   .rows(tableRow()
                                 .children(tableCell().child(block().content("")
                                                                     .children(block().content("From:").marginBottom("10px"),
                                                                               block().content("Garbarino S.A.").bold(),
                                                                               block().content("Leg Nº 12.541 Cabildo 2025"),
                                                                               block().content("1er Piso - C.A.B.A"),
                                                                               block().content("Tel: (011)153-260-4551")).size(
                                                 "8pt").color("#4B4B4B"))
                                                   .paddingBefore("3mm")));

        //J+

        final TableB headerTable = table().columns(tableColumn().columnWidth("45%"), tableColumn().columnWidth("55%"))
                                   .rows(
                tableRow().children(tableCell().child(block().content("").children(garbaHeader)).borderWidth("1mm").padding("1mm"),
                    tableCell().child(block().content("").children(clientTable)).borderWidth("1mm").padding("1mm")));

        final PageSequenceBuilder page = pageSequence().withName("content")
                                         .addChildren(block().children(headerTable).marginBottom("20px"), productsTable);

        final SimplePageMasterBuilder simplePageMaster = SimplePageMasterBuilder.simplePageMaster()
                                                         .masterName("content")
                                                         .marginBottom("10mm")
                                                         .marginLeft("10mm")
                                                         .marginRight("10mm")
                                                         .marginTop("10mm");

        return simpleDocument().withPageSequence(page).withSimplePageMaster(simplePageMaster).build();
    }  // end method buildInvoice

    private static SimpleDocument buildSimple() {
        final PageSequenceBuilder     page             = pageSequence().withName("content").addChildren(block().content("hola mundo"));
        final SimplePageMasterBuilder simplePageMaster = SimplePageMasterBuilder.simplePageMaster()
                                                         .masterName("content")
                                                         .marginBottom("10mm")
                                                         .marginLeft("10mm")
                                                         .marginRight("10mm")
                                                         .marginTop("10mm");
        return simpleDocument().withSimplePageMaster(simplePageMaster).withPageSequence(page).build();
    }

    private static TableRowB product(String name, String description, String quanity, String unitPrice, String totalPrice) {
        return tableRow().children(
                tableCell().child(block().children(block().content(name).bold().size("9pt"), block().content(description).size("9pt")))
                       .padding("3px")
                       .family("open-sans"),
                tableCell().child(block().content(quanity).size("9pt").marginTop("5px")).padding("3px").alignRight().family("open-sans"),
                tableCell().child(block().content(unitPrice).size("9pt").marginTop("5px")).padding("3px").alignRight().family("open-sans"),
                tableCell().child(block().content(totalPrice).size("9pt").marginTop("5px")).padding("3px").alignRight().family("open-sans"))
               .borderColor("#C0C0C0")
               .borderWidth("1px")
               .borderBottom("solid")
               .color("#4B4B4B");
    }

    private static TableB tableContent() {
        final TableRowB row = tableRow().children(
                tableCell().child(block().content("").children(garbaHeader()))
                           .borderWidth("1mm")
                           .borderColor(BLUE_COLOR)
                           .borderStyle("solid")
                           .padding("1mm"),
                tableCell().child(block().content("").children(providerHeader()))
                           .borderWidth("1mm")
                           .borderColor(BLUE_COLOR)
                           .borderStyle("solid")
                           .padding("1mm"));

        return table().columns(tableColumn().columnWidth("45%"), tableColumn().columnWidth("55%"))
               .rows(row, secondRow(), thirdRow(), fourthRow(), fifthRow());
    }

    //~ Static Fields ................................................................................................................................

    public static final String BLUE_COLOR = "#0079DB";
}  // end class FopBuilderTest
