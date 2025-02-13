
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.database.DatabaseConstants;
import tekgenesis.form.ReflectedFormInstance;
import tekgenesis.metadata.form.configuration.AxisConfig;
import tekgenesis.metadata.form.configuration.ChartConfig;
import tekgenesis.metadata.form.configuration.SeriesMode;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.samples.form.ChartConfigTest;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.form.FormsImpl.init;
import static tekgenesis.form.ReflectedFormInstance.wrap;

/**
 * Test Form's widgets configuration.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class ConfigurationTest {

    //~ Instance Fields ..............................................................................................................................

    public DbRule db = new DbRule(DbRule.AUTHORIZATION) {
            @Override protected void before() {
                createDatabase(DatabaseConstants.MEM);
            }
        };

    @Rule public TestRule chain = db.around(new FormRule());

    //~ Methods ......................................................................................................................................

    /** Tests axis maxs and mins. */
    @Test public void testAxisMaxAndMin() {
        final ReflectedFormInstance wrap  = wrap(init(ChartConfigTest.class));
        final FormModel             model = wrap.getModel();

        for (final Widget widget : model.metadata().getWidget("chart")) {
            final ChartConfigurationImpl chartConfiguration = new ChartConfigurationImpl(model, widget);

            chartConfiguration.axis(AxisConfig.Axis.X, 1)
                .axisMinimum(1)
                .axis(AxisConfig.Axis.Y, 1)
                .axisMinimum(0)
                .axis(AxisConfig.Axis.Y, 2)
                .axisMaximum(5);

            final ChartConfig modelConfig = (ChartConfig) model.getFieldConfig(widget);

            if (modelConfig != null) {
                final List<AxisConfig> xAxis = modelConfig.getXAxis();
                assertThat(xAxis).hasSize(1);

                final AxisConfig zeroXAxis = xAxis.get(0);
                assertThat(zeroXAxis.isMinimumSet()).isTrue();
                assertThat(zeroXAxis.getMinimum()).isEqualTo(1);
                assertThat(zeroXAxis.isMaximumSet()).isFalse();

                final List<AxisConfig> yAxis = modelConfig.getYAxis();
                assertThat(yAxis).hasSize(2);

                final AxisConfig zeroYAxis = yAxis.get(0);
                assertThat(zeroYAxis.isMinimumSet()).isTrue();
                assertThat(zeroYAxis.getMinimum()).isEqualTo(0);
                assertThat(zeroYAxis.isMaximumSet()).isFalse();

                final AxisConfig oneYAxis = yAxis.get(1);
                assertThat(oneYAxis.isMinimumSet()).isFalse();
                assertThat(oneYAxis.isMaximumSet()).isTrue();
                assertThat(oneYAxis.getMaximum()).isEqualTo(5);
            }
        }
    }  // end method testAxisMaxAndMin

    /** Tests chart configuration. */
    @Test public void testChartConfiguration() {
        final FormModel model = wrap(init(ChartConfigTest.class)).getModel();

        for (final Widget widget : model.metadata().getWidget("chart")) {
            final ChartConfigurationImpl chartConfiguration = new ChartConfigurationImpl(model, widget);

            chartConfiguration.seriesMode(SeriesMode.SIDE_BY_SIDE)
                .axis(AxisConfig.Axis.X, 1)
                .axisLabel("x0 label")
                .axisMinimum(1)
                .axisVisible(false)
                .axis(AxisConfig.Axis.Y, 1)
                .axisLabel("y0 label")
                .axisMinimum(0)
                .axisVisible(true)
                .showLabelsOnHover(true)
                .axis(AxisConfig.Axis.X, 1)
                .axisLabel("x0 label2")
                .axis(AxisConfig.Axis.Y, 2)
                .axisLabel("y1 label")
                .axisMinimum(1)
                .axisVisible(false)
                .withVerticalLabels(true)
                .positiveQuadrant();

            final ChartConfig modelConfig = (ChartConfig) model.getFieldConfig(widget);

            if (modelConfig != null) {
                assertThat(modelConfig.getSeriesMode()).isEqualTo(SeriesMode.SIDE_BY_SIDE);
                assertThat(modelConfig.hasLabelsOnHover()).isEqualTo(true);
                assertThat(modelConfig.hasVerticalLabels()).isEqualTo(true);
                assertThat(modelConfig.isPositiveQuadrant()).isEqualTo(true);

                final List<AxisConfig> xAxis = modelConfig.getXAxis();
                assertThat(xAxis).hasSize(1);

                final AxisConfig zeroXAxis = xAxis.get(0);
                assertThat(zeroXAxis.isMinimumSet()).isTrue();
                assertThat(zeroXAxis.getLabel()).isEqualTo("x0 label2");
                assertThat(zeroXAxis.getMinimum()).isEqualTo(1);
                assertThat(zeroXAxis.isVisible()).isFalse();

                final List<AxisConfig> yAxis = modelConfig.getYAxis();
                assertThat(yAxis).hasSize(2);

                final AxisConfig zeroYAxis = yAxis.get(0);
                assertThat(zeroYAxis.isMinimumSet()).isTrue();
                assertThat(zeroYAxis.getLabel()).isEqualTo("y0 label");
                assertThat(zeroYAxis.getMinimum()).isEqualTo(0);
                assertThat(zeroYAxis.isVisible()).isTrue();

                final AxisConfig oneYAxis = yAxis.get(1);
                assertThat(oneYAxis.isMinimumSet()).isTrue();
                assertThat(oneYAxis.getLabel()).isEqualTo("y1 label");
                assertThat(oneYAxis.getMinimum()).isEqualTo(1);
                assertThat(oneYAxis.isVisible()).isFalse();
            }
        }
    }  // end method testChartConfiguration
}  // end class ConfigurationTest
