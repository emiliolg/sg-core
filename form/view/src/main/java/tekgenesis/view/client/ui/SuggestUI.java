
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SuggestOracle;

import org.jetbrains.annotations.Nullable;

import tekgenesis.type.assignment.AssignmentType;
import tekgenesis.view.client.formatter.InputHandler;

/**
 * Interface for Suggest UI widgets.
 */
public interface SuggestUI extends BaseWidgetUI {

    //~ Methods ......................................................................................................................................

    /** Adds a create new SelectionEvent handler. */
    void addCreateNewHandler(final ValueChangeHandler<String> handler);

    /** Returns true if can search for deprecable instances. */
    boolean canSearchDeprecable();

    /** Returns true if model has an on_new method defined. */
    boolean hasOnNewMethod();

    /** Returns true if the widget has on suggest. */
    boolean hasOnSuggest();

    /** Returns true if the widget has on suggest sync. */
    boolean hasOnSuggestSync();

    /** Returns true if suggest is bounded to deprecable entity. */
    boolean isBoundedToDeprecable();

    /** Get suggest filter expression. */
    Iterable<AssignmentType> getFilterExpression();

    /** Sets suggest filter expression. */
    void setFilterExpression(@Nullable Iterable<AssignmentType> filter);

    /** Sets the logged user permission of deprecation handling. */
    void setHandleDeprecated(boolean b);

    /** Gets the associated input handler associated with this SuggestUI. */
    InputHandler<?> getInputHandler();

    /** Sets on suggest expression result that will be passed to the user when searching. */
    void setOnSuggestExpression(Object result);

    /** Gets suggest oracle of SuggestUI. */
    SuggestOracle getSuggestOracle();
}  // end interface SuggestUI
