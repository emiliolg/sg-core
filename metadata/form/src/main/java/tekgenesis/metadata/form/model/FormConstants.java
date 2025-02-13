
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import org.jetbrains.annotations.NonNls;

import tekgenesis.common.core.Constants;

/**
 * Constants to use in Forms.
 */
public class FormConstants {

    //~ Constructors .................................................................................................................................

    private FormConstants() {}

    //~ Static Fields ................................................................................................................................

    public static final String BIG_COMBO = "big-combo";

    public static final String OPEN = "open";

    public static final String CURRENT_FORM_ID = "Current-Form-Id";

    public static final String MODAL_LOGIN = "modal-login";

    public static final String DRAG_STARTED = "drag-started";

    public static final String CLICKABLE_TABLE = "clickable-table";

    public static final String MUTED = "muted";

    public static final String ANCHOR_DISABLED = "anchor-disabled";

    public static final String PROGRESS_BAR = "progress-bar";

    public static final String FLOATING_MODAL = "floating-modal";

    public static final String MODAL_DIALOG = "modal-dialog floating-modal";

    public static final String VERTICAL_LABEL_FIELD = "vertical-label-field";

    public static final String NUMBER_COLUMN_CLASS = "numberColumn";

    public static final int MAX_EM = 80;

    public static final int DEFAULT_LENGTH = 20;

    public static final int    GRID          = 12;
    public static final String COL_12        = "col-sm-12";
    public static final String OFFSET_PREFIX = "col-sm-offset-";
    public static final String WITH_OFFSET   = "with-offset";
    public static final String WIDGET        = "widget";
    public static final String DATA_TOGGLE   = "data-toggle";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String DROPDOWN = "dropdown";

    public static final String DISPLAY_WIDGET = "display-widget";

    @NonNls public static final String CLOSE_STRONG_TAG = "</strong>";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String NUMBER_STYLE = "number";

    public static final String INPUT_GROUP_ADDON = "input-group-addon";

    public static final String FORM_CONTROL = "form-control";

    public static final String BTN_DEFAULT = "btn-default";
    public static final String TIMES       = "&times;";

    @NonNls public static final String VIEW_DISABLED = "view-disabled";

    @NonNls public static final String PULL_RIGHT = "pull-right";

    @NonNls public static final String STRONG_TAG = "<strong>";

    @NonNls public static final String TITLE_TAG         = "<title>";
    @NonNls public static final String TITLE_CLOSING_TAG = "</title>";

    @NonNls public static final String ALIGNED_ICONS = "aligned-icons";

    @NonNls public static final String DROPDOWN_SUBMENU   = "dropdown-submenu";
    @NonNls public static final String DROPDOWN_OPEN_LEFT = "open-left";

    @NonNls public static final String FOCUSED = "focused";

    @NonNls public static final String DROP_TARGET = "drop-target";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String STYLE_ATTR = "style";

    @NonNls public static final String FLEX_BOX_CONTAINER = "flexBoxContainer";

    @NonNls public static final String MODAL_CLASS_NAME = "fade";

    @NonNls public static final String BLANK = "_blank";

    @NonNls public static final String CLICKABLE = "clickable";

    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String         UPLOAD = "upload";

    @NonNls public static final String DROPDOWN_MENU = "dropdown-menu";

    @NonNls public static final String PAGE_HEADER   = "page-header";
    @NonNls public static final String LINK_DISABLED = "link-disabled";
    @NonNls public static final String INPUT_PREPEND = "input-prepend";
    @NonNls public static final String INPUT_GROUP   = "input-group";
    @NonNls public static final String INPUT_APPEND  = "input-append";
    @NonNls public static final String ADD_ON        = "add-on";
    @NonNls public static final String MARGIN_0      = "margin0";

    @NonNls public static final String BLOCK_INPUT = "block-input";

    public static final String ELLIPSIS = "...";

    @NonNls public static final String ROTATE_90    = "rotate90";
    @NonNls public static final String COLLAPSED    = "collapsed";
    @NonNls public static final String COLLAPSE     = "collapse";
    @NonNls public static final String COLLAPSE_BTN = "collapse-btn";

    @NonNls public static final String ROTATE_STYLE = "rotate45";

    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String         PROGRESS = "progress";

    @NonNls public static final String RADIO_GROUP  = "radioGroup";
    @NonNls public static final String RADIO_OPTION = "radioOption";

    @NonNls public static final String MODAL_MENU = "modal-menu";

    @NonNls public static final String CAPTION    = "caption";
    @NonNls public static final String UPLOAD_IMG = " uploadImg";

    @NonNls public static final String BORDER_TOP = "borderTop";

    @NonNls public static final String TAB_LIMIT = "tabLimit";

    @NonNls public static final String TREEVIEW = "treeview";
    @NonNls public static final String TREENODE = "treeNode";

    @NonNls public static final String Z_HIDDEN      = "zHidden";
    @NonNls public static final String MODAL_BODY    = "modal-body";
    @NonNls public static final String INPUT_XXLARGE = "input-xxlarge";

    @NonNls public static final String NO_IMAGE_URL      = "/public/sg/img/noImage.jpg";
    @NonNls public static final String MAIN_THUMBNAIL    = "thumbnail mainThumb";
    @NonNls public static final String NO_MAIN_THUMBNAIL = "noMainThumb";
    @NonNls public static final String THUMBNAIL         = "thumbnail";
    @NonNls public static final String THUMBNAILS        = "thumbnails";

    @NonNls public static final String YOUTUBE_EMBED       = "https://www.youtube.com/embed/";
    @NonNls public static final String YOUTUBE_EMBED_SHORT = "youtube.com/embed/";
    @NonNls public static final String VIMEO_EMBED         = "http://player.vimeo.com/video/";
    @NonNls public static final String VIMEO_EMBED_SHORT   = "player.vimeo.com/video";
    @NonNls public static final int    YOUTUBE_ID_LENGTH   = 11;
    @NonNls public static final int    DEFAULT_WIDTH       = 640;
    @NonNls public static final int    DEFAULT_HEIGHT      = 480;

    /** The prefix for auto generated widget ids. */
    @NonNls public static final char AUTO_ID_PREFIX = '$';

    /** The method name to invoke when the user populates a form. */
    @NonNls public static final String POPULATE_METHOD_NAME = "populate";

    /** Table mergeInto method name. */
    @NonNls public static final String MERGE_INTO_METHOD_NAME = "mergeInto";

    /** The an index arg var name. */
    @NonNls public static final String INDEX = Constants.INDEX;

    /** The method name to invoke when the user updates a form instance. */
    @NonNls public static final String FIND_METHOD_NAME = "find";

    /** The method name to invoke when the user submits the form (for protected fields). */
    @NonNls public static final String COPY_TO_PROTECTED = "copyToProtectedFields";

    /** The method name to invoke when configuring widgets. */
    @NonNls public static final String CONFIGURATION_METHOD_NAME = "configuration";

    /** The method name to invoke when configuring widgets. */
    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String         CONFIG_METHOD_NAME = "config";

    /** The method name to invoke prepended with the table name. */
    @NonNls public static final String CREATE_OR_UPDATE_METHOD_NAME = "createOrUpdate";

    /** The name of the model Field in the generated FormBase. */
    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String         MODEL_FIELD_NAME = "model";

    /** Image type. */
    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String         IMAGE = "image";

    /** The active component Style name. */
    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String         ACTIVE_STYLE = "active";

    /** The table active row style name. */
    @NonNls public static final String TABLE_ACTIVE_ROW_STYLE = ACTIVE_STYLE + " alert";

    /** The disabled component Style name. */
    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String         DISABLED_STYLE = "disabled";

    /** The close button Style name. */
    @NonNls public static final String CLOSE_BUTTON = "close";

    /** The close button Style name. */
    @NonNls public static final String CLOSE_ICON = "×";

    /** The primary Button Style name. */
    @NonNls public static final String BORDER = "border";

    /** The primary Button Style name. */
    @NonNls public static final String BTN_PRIMARY = "btn-primary";

    /** The info Button Style name. */
    @NonNls public static final String BTN_INFO = "btn-info";

    /** The danger Button Style name. */
    @NonNls public static final String BTN_DANGER = "btn-danger";

    /** The success Button Style name. */
    @NonNls public static final String BTN_SUCCESS = "btn-success";

    /** The Xs Button Style name. */
    @NonNls public static final String BTN_XS = "btn btn-xs";

    /** The Table Pagination Centered Style name. */
    @NonNls public static final String PAGINATION = "pagination";

    /** FlexBox Modal StyleName. */
    @NonNls public static final String FLEX_BOX = "flexBox";

    @NonNls public static final String FORM_LIFECYCLE_COOKIE = "FORM_LIFECYCLE";

    @NonNls public static final String VIEW_IMG_USERS_JPG = "/public/sg/img/users.jpg";
}  // end class FormConstants
