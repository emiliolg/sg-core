
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.multiple;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.MultipleWidget;

import static tekgenesis.common.core.Option.some;

/**
 * Interface for all multiple model lenses.
 */
public interface MultipleModelLens {

    //~ Methods ......................................................................................................................................

    /** Decorate given lens. */
    MultipleModelLens decorate(MultipleModelLens base);

    /** Map item slot to ui child section index. */
    Option<Integer> mapItemToSection(int item);

    /** Map ui child section index to model item slot. */
    int mapSectionToItem(final int section);

    /** Called to make a lens react. */
    void react(@NotNull LensEvent event);

    /** Sections size. */
    int size();
    RowModel getItemModel(int item);
    @NotNull MultipleWidget getMultipleWidget();

    //~ Inner Classes ................................................................................................................................

    abstract class CompoundLens implements MultipleModelLens {
        protected MultipleModelLens origin;

        CompoundLens() {
            origin = new UnreachableLens(getClass().getCanonicalName());
        }

        /** Starts multiple model. */
        public MultipleModelLens decorate(MultipleModelLens base) {
            origin = base;
            return this;
        }

        @Override public final Option<Integer> mapItemToSection(int item) {
            return origin.mapItemToSection(item).flatMap(this::itemToSection);
        }

        @Override public final int mapSectionToItem(int section) {
            return origin.mapSectionToItem(sectionToItem(section));
        }

        @Override public final void react(@NotNull LensEvent event) {
            origin.react(event);
            doReact(event);
        }

        @Override public int size() {
            return origin.size();
        }

        @Override public final RowModel getItemModel(int item) {
            return origin.getItemModel(item);
        }

        @NotNull @Override public final MultipleWidget getMultipleWidget() {
            return origin.getMultipleWidget();
        }

        protected abstract void doReact(LensEvent event);
        protected abstract Option<Integer> itemToSection(int item);
        protected abstract int sectionToItem(int section);
    }  // end class CompoundLens

    class ModelLens implements MultipleModelLens {
        @NotNull private final MultipleModel model;

        public ModelLens(@NotNull final MultipleModel model) {
            this.model = model;
        }

        @Override public MultipleModelLens decorate(MultipleModelLens ignore) {
            return this;
        }

        @Override public Option<Integer> mapItemToSection(int item) {
            return some(item);
        }

        @Override public int mapSectionToItem(int section) {
            return section;
        }

        @Override public void react(@NotNull LensEvent event) {}

        @Override public int size() {
            return model.size();
        }

        @Override public RowModel getItemModel(int item) {
            return model.getRow(item);
        }

        @NotNull @Override public MultipleWidget getMultipleWidget() {
            return model.getMultipleWidget();
        }
    }

    class UnreachableLens implements MultipleModelLens {
        private final String owner;

        public UnreachableLens(String owner) {
            this.owner = owner;
        }

        @Override public MultipleModelLens decorate(MultipleModelLens base) {
            throw unreachable("decorate");
        }

        @Override public Option<Integer> mapItemToSection(int item) {
            throw unreachable("mapItemToSection");
        }

        @Override public int mapSectionToItem(int section) {
            throw unreachable("mapSectionToItem");
        }

        @Override public void react(@NotNull LensEvent event) {
            throw unreachable("react");
        }

        @Override public int size() {
            throw unreachable("size");
        }

        @Override public RowModel getItemModel(int item) {
            throw unreachable("getItemModel");
        }

        @NotNull @Override public MultipleWidget getMultipleWidget() {
            throw unreachable("getMultipleWidget");
        }

        private IllegalStateException unreachable(String text) {
            return Predefined.unreachable(owner + "-" + text);
        }
    }
}  // end interface MultipleModelLens
