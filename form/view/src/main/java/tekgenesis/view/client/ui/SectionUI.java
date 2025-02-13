
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.controller.ViewCreator;
import tekgenesis.view.client.ui.base.ExtButton;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.utils.Animation;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.metadata.form.model.FormConstants.STYLE_ATTR;
import static tekgenesis.view.client.ui.TableUI.TABLE_EMPTY_MESSAGE;

/**
 * A Section UI widget.
 */
public class SectionUI extends BaseMultipleUI implements HasPlaceholderUI, MultipleUI {

    //~ Instance Fields ..............................................................................................................................

    SectionHandler handler;

    private final InlineHTML emptyMessage;

    private ScrollPanel             scroll   = null;
    private final List<SectionPart> sections;

    private final FlowPanel sectionsDiv;

    //~ Constructors .................................................................................................................................

    /** Creates a Section UI widget. */
    public SectionUI(@NotNull final ModelUI container, @NotNull final MultipleWidget model) {
        super(container, model);

        handler  = null;
        sections = new ArrayList<>();

        sectionsDiv = HtmlWidgetFactory.div();
        sectionsDiv.addStyleName("section-content");
        initWidget(sectionsDiv);

        emptyMessage = HtmlWidgetFactory.span();
        emptyMessage.setStyleName(TABLE_EMPTY_MESSAGE);
        div.add(emptyMessage);

        addStyleName("section-widget row");
        if (model.isScrollable()) buildScrollable();
    }

    //~ Methods ......................................................................................................................................

    /** Toggle section visibility. */
    @Override public void toggleFilteredSection(int section, boolean show) {
        final Element element = sectionsDiv.getWidget(section).getElement();
        if (element != null) {
            if (show) element.removeClassName("hide");
            else element.addClassName("hide");
        }
    }

    @Override public void setPlaceholder(final String placeholder) {
        emptyMessage.setText(placeholder);
    }

    /** Returns all the cells of a certain section. */
    @Override public Iterable<WidgetUI> getSection(int row) {
        return sections.get(row);
    }

    /** Returns all the cells of a certain section. */
    public WidgetUI getSectionCell(int row, @NotNull final Widget widget) {
        for (final WidgetUI cell : getSection(row)) {
            if (widget == cell.getModel()) return cell;
        }

        throw new IllegalStateException("Widget " + widget + " not found on section!");
    }

    /** Sets a styleClass to a section. */
    public void setSectionElementStyle(int section, @Nullable final String style) {
        sectionsDiv.getWidget(section).getElement().setClassName(style);
    }

    /** Add handler for section operations. */
    public void setSectionHandler(final SectionHandler h) {
        handler = h;
    }

    /** Sets a styleClass to a section. */
    public void setSectionInlineStyle(int section, @Nullable final String style) {
        sectionsDiv.getWidget(section).getElement().setAttribute(STYLE_ATTR, notNull(style));
    }

    /** Fit sections to avoid empty sections and create new ones. */
    @Override protected void fitToExactSections() {
        // Ensure exact sections
        final int expected = getSectionsCount();
        final int actual   = sections.size();

        if (expected > actual)
        // Insert blank sections
        expandSections(actual, expected - actual);
        else if (expected < actual) {
            // Remove extra sections
            final int decrease = actual - expected;
            shrinkSections(actual - decrease, decrease);
        }

        // Show/hide empty table msg
        emptyMessage.setVisible(getSectionsCount() == 0);
    }  // end method fitToExactSections

    @Override void clear() {
        super.clear();

        for (final SectionPart section : sections) {
            for (final WidgetUI ui : section)
                ui.clear();
        }
    }

    /** Get the section container. */
    FlowPanel getSectionsDiv() {
        return sectionsDiv;
    }

    private void addScrollBars(FlowPanel scrollContainer) {
        final ExtButton leftButton  = new ExtButton();
        final ExtButton rightButton = new ExtButton();
        leftButton.addStyleName("left-anchor");
        rightButton.addStyleName("right-anchor");

        leftButton.setIcon(IconType.CHEVRON_LEFT);
        rightButton.setIcon(IconType.CHEVRON_RIGHT);

        leftButton.addClickHandler(event -> Animation.scrollLeft(scroll.getElement(), -SCROLL_DIFF));
        rightButton.addClickHandler(event -> Animation.scrollRight(scroll.getElement(), SCROLL_DIFF));

        scrollContainer.insert(leftButton, 0);
        scrollContainer.insert(rightButton, 2);
    }

    private void addSectionPartWidget(@NotNull WidgetUI ui) {
        final int index = ui.getContext().getRow().get();
        while (sections.size() <= index)
            sections.add(createEmptySectionPart());  // Ensure array slot
        sections.get(index).addWidget(ui);
    }

    private void buildScrollable() {
        sectionsDiv.addStyleName("section-container");
        scroll = new ScrollPanel(sectionsDiv);
        // noinspection SpellCheckingInspection
        scroll.addStyleName("main-scroller");
        final FlowPanel scrollContainer = HtmlWidgetFactory.div();
        scrollContainer.addStyleName("scrollable-group");
        scrollContainer.add(scroll);
        addScrollBars(scrollContainer);
        div.add(scrollContainer);
    }

    private SectionPart createEmptySectionPart() {
        final SectionPart part = new SectionPart();
        part.into(sectionsDiv);
        return part;
    }

    private void createSectionPart(int index) {
        ViewCreator.createView(container(), getModel(), new SectionPartWrapper(index), true);
        if (handler != null) handler.onSectionCreated(index);
    }

    private void expandSections(int start, int amount) {
        for (int i = 0; i < amount; i++)
            createSectionPart(start + i);
    }

    /** Removes the section at the given index. */
    private void removeSection(int index) {
        sections.remove(index);
        sectionsDiv.remove(index);
    }

    /** Actual remove of the sections. */
    private void shrinkSections(int start, int amount) {
        for (int index = start + amount - 1; index >= start; index--) {
            if (handler != null) handler.onSectionDeleted(index);
            removeSection(index);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final int SCROLL_DIFF = 30;

    //~ Inner Interfaces .............................................................................................................................

    public interface SectionHandler {
        /** Item to be created on item index. */
        void onSectionCreated(int item);

        /** Item to be deleted on item index. */
        void onSectionDeleted(int item);

        class Default implements SectionHandler {
            @Override public void onSectionCreated(int item) {
                Logger.getLogger(SectionUI.class).info("SectionHandler.onSectionCreated item :: " + item);
            }
            @Override public void onSectionDeleted(int item) {
                Logger.getLogger(SectionUI.class).info("SectionHandler.onSectionDeleted item :: " + item);
            }
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class SectionPart implements Iterable<WidgetUI> {
        private final Panel container;

        private SectionPart() {
            container = HtmlWidgetFactory.div();
        }

        public void into(@NotNull final FlowPanel parent) {
            parent.add(container);
        }

        @Override public Iterator<WidgetUI> iterator() {
            return cast(container.iterator());
        }

        private void addWidget(WidgetUI child) {
            container.add(child);
        }
    }

    private class SectionPartWrapper implements HasWidgetsUI {
        private int       column;
        private final int section;

        private SectionPartWrapper(int section) {
            this.section = section;
        }

        @Override public void addChild(WidgetUI ui) {
            addSectionPartWidget(ui.withinMultiple(SectionUI.this, section, column++));
        }
    }
}
