
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.form.ReflectedFormInstance.checkPublicNotStatic;

/**
 * Form navigate action.
 */
@SuppressWarnings("ParameterHidesMemberVariable")
public class SwipeImpl extends ActionImpl implements Swipe {

    //~ Instance Fields ..............................................................................................................................

    private int                 fetchSize  = 3;
    private boolean             fullscreen;
    private int                 height;
    private final List<Integer> indexesMap;

    private final Class<? extends SwipeLoader<?>> loaderClass;

    private boolean   loop       = false;
    private int       marginTop;
    private final int startIndex;
    private int       width;

    //~ Constructors .................................................................................................................................

    private SwipeImpl(Class<? extends SwipeLoader<?>> loaderClass, int startIndex, List<Integer> indexesMap) {
        super(ActionType.SWIPE);
        checkPublicNotStatic(loaderClass);
        this.loaderClass = cast(loaderClass);
        this.startIndex  = startIndex;
        this.indexesMap  = indexesMap;
    }

    //~ Methods ......................................................................................................................................

    @Override public Swipe cyclic() {
        loop = true;
        return this;
    }

    @NotNull @Override public Swipe dimension(int width, int height) {
        this.width  = width;
        this.height = height;
        return this;
    }

    @NotNull @Override public Swipe fetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    @NotNull @Override public Swipe fullscreen() {
        fullscreen = true;
        return this;
    }

    @NotNull @Override public Swipe marginTop(int marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    /** Returns true if dimension is set by the user. */
    public boolean isDimensionDefined() {
        return width != 0 && height != 0;
    }

    /** Returns the fetch size of this swipe. */
    public int getFetchSize() {
        return fetchSize;
    }

    /** Returns popup panels height. */
    public int getHeight() {
        return height;
    }

    /** Returns the map that is used to remap indexes. */
    public List<Integer> getIndexesMap() {
        return indexesMap;
    }

    /** Resolves which index are needed. */
    public Seq<Integer> getIndexesToLoad() {
        return Colls.seq(resolveIndexesToLoad());
    }

    /** Returns which class is used to load (fetch) more models for this swipe. */
    public Class<? extends SwipeLoader<?>> getLoaderClass() {
        return loaderClass;
    }

    /** Get margin top. */
    public int getMarginTop() {
        return marginTop;
    }

    /** Returns true if popup panels must be displayed in full screen mode. */
    public boolean isFullscreen() {
        return fullscreen;
    }

    /** Returns true if this swipe is configured to cyclic. */
    public boolean isLoop() {
        return loop;
    }

    /** Returns the start index of this swipe. */
    public int getStartIndex() {
        return startIndex;
    }

    /** Returns popup panels width. */
    public int getWidth() {
        return width;
    }

    private List<Integer> resolveIndexesToLoad() {
        final int           fetch  = Math.min(fetchSize, indexesMap.size());
        final List<Integer> result = new ArrayList<>(fetch);

        // add the index before the current
        if (fetch > 2) {
            if (startIndex > 0) result.add(indexesMap.get(startIndex - 1));
            else if (loop) result.add(indexesMap.get(indexesMap.size() - 1));
        }

        // add the next indexes
        int i = startIndex;
        while (result.size() < fetch) {
            if (i >= indexesMap.size()) {
                if (loop) i = 0;  // start over
                else break;       // end
            }

            result.add(indexesMap.get(i));
            i++;
        }

        return result;
    }

    //~ Methods ......................................................................................................................................

    /** Swipe based on table rows. */
    public static Swipe swipe(Class<? extends SwipeLoader<?>> loaderClass, FormTable<?> table) {
        final List<Integer> indexesMap = new ArrayList<>(table.size());

        int                 i             = 0;
        final MultipleModel multipleModel = ((FormTableImpl<?>) table).getMultipleModel();

        for (final RowModel rowModel : multipleModel) {
            if (rowModel.getFilterData().isAccepted()) indexesMap.add(i);
            i++;
        }

        final int startIndex = indexesMap.indexOf(table.getCurrentIndex().get());
        return new SwipeImpl(loaderClass, startIndex, indexesMap);
    }

    /** From current index, load size indexes. */
    public static Swipe swipe(Class<? extends SwipeLoader<?>> loaderClass, int startIndex, int size) {
        final List<Integer> indexesMap = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            indexesMap.add(i);

        return new SwipeImpl(loaderClass, startIndex, indexesMap);
    }
}  // end class SwipeImpl
