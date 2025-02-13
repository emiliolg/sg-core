
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.util.function.Function;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;
import static tekgenesis.metadata.form.widget.WidgetType.*;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class UiModelElementsTest {

    //~ Methods ......................................................................................................................................

    @Test public void formMultiples()
        throws BuilderException
    {
        assertThat(
                form(table("table").children(check("check")),
                    section("section").children(button("button"), breadCrumb("breadcrumb")),
                    chart("chart", ChartType.COLUMN).children(field("labels"), numeric("male", 2, 10)),
                    verticalGroup("vertical").children(button("click"), display("display")))).hasElements(TABLE,
                CHECK_BOX,
                SECTION,
                BUTTON,
                BREADCRUMB,
                CHART,
                TEXT_FIELD,
                TEXT_FIELD,
                VERTICAL,
                BUTTON,
                DISPLAY)
            .hasFields(VERTICAL, BUTTON, DISPLAY)
            .hasNoSubForms()
            .hasFieldsWithOptions(BREADCRUMB, DISPLAY)
            .hasFieldsWithConfigurations(CHART)
            .hasMultiples(new Multiple("table") {
                    @Override void accept(@NotNull MultipleAssert multiple) {
                        multiple.hasFields(CHECK_BOX).hasNoSubForms().hasFieldsWithOptions().hasFieldsWithConfigurations();
                    }
                },


                new Multiple("section") {
                    @Override void accept(@NotNull MultipleAssert multiple) {
                        multiple.hasFields(BUTTON, BREADCRUMB).hasNoSubForms().hasFieldsWithOptions(BREADCRUMB).hasFieldsWithConfigurations();
                    }
                }, new Multiple("chart") {
                    @Override void accept(@NotNull MultipleAssert multiple) {
                        multiple.hasFields(TEXT_FIELD, TEXT_FIELD).hasNoSubForms().hasFieldsWithOptions().hasFieldsWithConfigurations();
                    }
                });
    }

    @Test public void formWithFields()
        throws BuilderException
    {
        assertThat(form(check("check").defaultValue(true), label("label"), combo("combo"), list("list"))).hasElements(CHECK_BOX,
                LABEL,
                COMBO_BOX,
                LIST_BOX)
            .hasFields(CHECK_BOX, LABEL, COMBO_BOX, LIST_BOX)
            .hasNoSubForms()
            .hasFieldsWithOptions(COMBO_BOX, LIST_BOX)
            .hasFieldsWithConfigurations();
    }

    @Test public void formWithSubForms()
        throws BuilderException
    {
        assertThat(
                form(field("field"),
                    subform("subform", "other"),
                    table("table1").children(field("field"), subform("subform", "other")),
                    table("table2").children(combo("field"), subform("subform", "other")),
                    button("button"),
                    subform("subform", "any"),
                    subform("subform", "any"))).hasElements(TEXT_FIELD,
                SUBFORM,
                TABLE,
                TEXT_FIELD,
                SUBFORM,
                TABLE,
                COMBO_BOX,
                SUBFORM,
                BUTTON,
                SUBFORM,
                SUBFORM)
            .hasFields(TEXT_FIELD, SUBFORM, BUTTON, SUBFORM, SUBFORM)
            .hasSubForms(3)
            .hasFieldsWithOptions(COMBO_BOX)
            .hasFieldsWithConfigurations(SUBFORM, SUBFORM, SUBFORM)
            .hasMultiples(new Multiple("table1") {
                    @Override void accept(@NotNull MultipleAssert multiple) {
                        multiple.hasFields(TEXT_FIELD, SUBFORM).hasSubForms(1).hasFieldsWithOptions().hasFieldsWithConfigurations(SUBFORM);
                    }
                }, new Multiple("table2") {
                    @Override void accept(@NotNull MultipleAssert multiple) {
                        multiple.hasFields(COMBO_BOX, SUBFORM).hasSubForms(1).hasFieldsWithOptions(COMBO_BOX).hasFieldsWithConfigurations(SUBFORM);
                    }
                });
    }

    @Test public void formWithTable()
        throws BuilderException
    {
        assertThat(form(table("table").children(check("check").defaultValue(true), label("label"), combo("combo"), list("list")))).hasElements(TABLE,
                CHECK_BOX,
                LABEL,
                COMBO_BOX,
                LIST_BOX)
            .hasFields()
            .hasNoSubForms()
            .hasFieldsWithOptions(COMBO_BOX, LIST_BOX)
            .hasFieldsWithConfigurations()
            .hasMultiples(new Multiple("table") {
                    @Override public void accept(@NotNull MultipleAssert multiple) {
                        multiple.hasFields(CHECK_BOX, LABEL, COMBO_BOX, LIST_BOX)
                        .hasNoSubForms()
                        .hasFieldsWithOptions(COMBO_BOX, LIST_BOX)
                        .hasFieldsWithConfigurations();
                    }
                });
    }

    @Test public void formWithTableAndFields()
        throws BuilderException
    {
        assertThat(
                form(table("table").children(horizontalGroup("horizontal").children(check("check").defaultValue(true), label("label")), list("list")),
                    field("text_field"),
                    area("text_area"),
                    tags("tags"))).hasElements(TABLE, HORIZONTAL, CHECK_BOX, LABEL, LIST_BOX, TEXT_FIELD, TEXT_AREA, TAGS)
            .hasFields(TEXT_FIELD, TEXT_AREA, TAGS)
            .hasNoSubForms()
            .hasFieldsWithOptions(LIST_BOX)
            .hasFieldsWithConfigurations()
            .hasMultiples(new Multiple("table") {
                    @Override void accept(@NotNull MultipleAssert multiple) {
                        multiple.hasFields(HORIZONTAL, CHECK_BOX, LABEL, LIST_BOX)
                        .hasNoSubForms()
                        .hasFieldsWithOptions(LIST_BOX)
                        .hasFieldsWithConfigurations();
                    }
                });
    }

    private Form form(WidgetBuilder... widgets)
        throws BuilderException
    {
        return FormBuilderPredefined.form("source", "package", "id").children(widgets).withRepository(new ModelRepository()).build();
    }

    //~ Methods ......................................................................................................................................

    private static FormAssert assertThat(@NotNull final Form form) {
        return new FormAssert(form);
    }

    private static WidgetBuilder chart(@NotNull final String multiple, ChartType type)
        throws BuilderException
    {
        return FormBuilderPredefined.chart(multiple, type).id(multiple);
    }

    private static WidgetBuilder section(@NotNull final String multiple) {
        return FormBuilderPredefined.section(multiple).id(multiple);
    }

    private static WidgetBuilder table(@NotNull final String multiple) {
        return FormBuilderPredefined.table(multiple).id(multiple);
    }

    //~ Inner Classes ................................................................................................................................

    private static class FormAssert extends ModelContainerAssert<FormAssert, Form> {
        protected FormAssert(Form form) {
            super(form, FormAssert.class);
        }

        private FormAssert hasElements(WidgetType... elements) {
            assertTypes(actual.getElementsDimension(), actual::getWidgetByOrdinal, elements);
            return myself;
        }

        private FormAssert hasMultiples(Multiple... multiples) {
            Assertions.assertThat(actual.getMultipleDimension()).isEqualTo(multiples.length);
            for (final Multiple multiple : multiples)
                multiple.resolve(actual);
            return myself;
        }
    }

    private static class ModelContainerAssert<S extends ModelContainerAssert<S, T>, T extends ModelContainer> extends AbstractAssert<S, T> {
        protected ModelContainerAssert(T actual, Class<S> selfType) {
            super(actual, selfType);
        }

        protected void assertTypes(int dimension, Function<Integer, Widget> map, WidgetType[] elements) {
            final WidgetType[] expected = new WidgetType[dimension];
            for (int i = 0; i < dimension; i++)
                expected[i] = map.apply(i).getWidgetType();
            Assertions.assertThat(expected).isEqualTo(elements);
        }

        protected S hasFields(WidgetType... fields) {
            assertTypes(actual.getFieldDimension(), actual::getWidgetByFieldSlot, fields);
            return myself;
        }

        protected S hasFieldsWithConfigurations(WidgetType... configurations) {
            Assertions.assertThat(actual.getConfigureDimension()).isEqualTo(configurations.length);
            assertTypes(actual.getConfigureDimension(), actual::getWidgetByConfigurationSlot, configurations);
            return myself;
        }

        protected S hasFieldsWithOptions(WidgetType... options) {
            Assertions.assertThat(actual.getOptionsDimension()).isEqualTo(options.length);
            assertTypes(actual.getOptionsDimension(), actual::getWidgetByOptionSlot, options);
            return myself;
        }

        protected S hasNoSubForms() {
            Assertions.assertThat(actual.getSubformDimension()).isZero();
            return myself;
        }

        protected S hasSubForms(int subForms) {
            Assertions.assertThat(actual.getSubformDimension()).isEqualTo(subForms);
            return myself;
        }
    }  // end class ModelContainerAssert

    private abstract static class Multiple {
        @NotNull private final String name;

        private Multiple(@NotNull final String name) {
            this.name = name;
        }

        abstract void accept(@NotNull MultipleAssert multiple);

        private void resolve(@NotNull final Form parent) {
            final MultipleWidget multiple = cast(parent.getElement(name));
            accept(new MultipleAssert(multiple));
        }
    }

    private static class MultipleAssert extends ModelContainerAssert<MultipleAssert, MultipleWidget> {
        protected MultipleAssert(MultipleWidget multiple) {
            super(multiple, MultipleAssert.class);
        }
    }
}  // end class UiModelElementsTest
