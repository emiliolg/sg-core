
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.suggest;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle;

import org.jetbrains.annotations.Nullable;

/**
 * ItemSuggestion.
 */
@SuppressWarnings("FieldMayBeFinal")
public class ItemSuggestion implements IsSerializable, SuggestOracle.Suggestion {

    //~ Instance Fields ..............................................................................................................................

    private String displayString;  // used to display item in suggest list (may contain html).
    private String key;            // key...

    private String replacementString;

    //~ Constructors .................................................................................................................................

    /** Default GWT Serialization Constructor. */
    public ItemSuggestion() {
        this("", "");
    }

    /** Convenience method for creation of a suggestion. */
    public ItemSuggestion(String key, String displayString) {
        this.key           = key;
        this.displayString = displayString;
        replacementString  = displayString;
    }

    /** Convenience method for creation of a suggestion. */
    public ItemSuggestion(@Nullable String key, String displayString, String replacementString) {
        this.key               = key;
        this.displayString     = displayString;
        this.replacementString = replacementString;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemSuggestion that = (ItemSuggestion) o;

        return displayString.equals(that.displayString) && key.equals(that.key) && replacementString.equals(that.replacementString);
    }

    @Override public int hashCode() {
        int result = displayString.hashCode();
        result = 31 * result + key.hashCode();
        result = 31 * result + replacementString.hashCode();
        return result;
    }

    public String getDisplayString() {
        return displayString;
    }

    /** Returns this item's key. */
    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    /** Returns this item's key. */
    public String getKey() {
        return key;
    }

    // used to replace the item upon selection of it in the list.
    public String getReplacementString() {
        return replacementString;
    }
}  // end class ItemSuggestion
