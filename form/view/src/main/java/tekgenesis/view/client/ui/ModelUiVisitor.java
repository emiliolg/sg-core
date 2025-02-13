
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;

import static tekgenesis.common.Predefined.unreachable;

/**
 * Model ui view visitor. Traverses over ui hierarchy.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "EmptyMethod", "OverlyComplexClass" })
public interface ModelUiVisitor {

    //~ Methods ......................................................................................................................................

    default void traverse(Iterable<WidgetUI> children) {
        for (final WidgetUI widget : children)
            visitChild(widget);
    }  // end method traverse

    /** Visit FormUI. */
    default void visit(FormUI form) {
        traverse(form);
    }

    /** Visit WidgetUI. */
    default void visit(WidgetUI widget) {}

    /** Visit HasValueUI. */
    default void visit(HasValueUI widget) {}

    /** Visit HasValueUI. */
    default void visit(HasTextLengthUI widget) {}

    /** Visit InputWidgetUI. */
    default void visit(FieldWidgetUI widget) {}

    /** Visit BaseDateUI. */
    default void visit(BaseDateUI widget) {}

    /** Visit HasOptionsUI. */
    default void visit(HasOptionsUI widget) {}

    /** Visit RangeUI. */
    default void visit(RangeUI widget) {}

    /** Visit RangeValueUI. */
    default void visit(RangeValueUI widget) {}

    /** Visit HasRangeOptionsUI. */
    default void visit(HasRangeOptionsUI widget) {}

    /** Visit CheckBoxGroupUI. */
    default void visit(CheckBoxGroupUI widget) {}

    /** Visit ContainerUI. */
    default void visit(ContainerUI container) {
        traverse(container);
    }

    /** Visit DialogGroupUI. */
    default void visit(DialogGroupUI dialog) {
        traverse(dialog);
    }

    /** Visit PopoverUI. */
    default void visit(PopoverUI widget) {
        traverse(widget);
    }

    /** Visit TableUI. */
    default void visit(TableUI table) {}

    /** Visit WidgetDefUI. */
    default void visit(WidgetDefUI widget) {
        traverse(widget);
    }

    /** Visit SectionUI. */
    default void visit(SectionUI section) {}

    /** Visit ChartUI. */
    default void visit(ChartUI chart) {}

    /** Visit UploadUI. */
    default void visit(UploadUI upload) {}

    /** Visit TextAreaUI. */
    default void visit(TextAreaUI area) {}

    /** Visit RichTextAreaUI. */
    default void visit(RichTextAreaUI area) {}

    /** Visit TextAreaUI. */
    default void visit(MessageUI message) {}

    /** Visit TextFieldUI. */
    default void visit(TextFieldUI field) {}

    /** Visit DisplayUI. */
    default void visit(DisplayUI field) {}

    /** Visit MultipleDisplayUI. */
    default void visit(MultipleDisplayUI field) {}

    /** Visit TagsUI. */
    default void visit(TagsUI field) {}

    /** Visit TagsComboBoxUI. */
    default void visit(TagsComboBoxUI field) {}

    /** Visit TagsSuggestBoxUI. */
    default void visit(TagsSuggestBoxUI field) {}

    /** Visit TagsSuggestBoxUI. */
    default void visit(SuggestUI field) {}

    /** Visit LabelUI. */
    default void visit(LabelUI label) {}

    /** Visit ButtonUI. */
    default void visit(ButtonUI button) {}

    /** Visit SubFormUI. */
    default void visit(AnchoredSubformUI subForm) {}

    /** Visit Inline SubFormUI. */
    default void visit(InlineSubformUI subForm) {}

    /** Visit DatePickerUI. */
    default void visit(DatePickerUI picker) {}

    /** Visit ColorPickerUI. */
    default void visit(ColorPickerUI colorPicker) {}

    /** Visit DoubleDateBoxUI. */
    default void visit(DoubleDateBoxUI picker) {}

    /** Visit TimePickerUI. */
    default void visit(TimePickerUI picker) {}

    /** Visit DateBoxUI. */
    default void visit(DateBoxUI box) {}

    /** Visit ComboDateBoxUI. */
    default void visit(ComboDateBoxUI box) {}

    /** Visit DateTimeBoxUI. */
    default void visit(DateTimeBoxUI box) {}

    /** Visit ComboBoxUI. */
    default void visit(ComboBoxUI combo) {}

    /** Visit ListBoxUI. */
    default void visit(ListBoxUI list) {}

    /** Visit SuggestBoxUI. */
    default void visit(SuggestBoxUI suggest) {}

    /** Visit RadioGroupUI. */
    default void visit(RadioGroupUI radio) {}

    /** Visit RatingUI. */
    default void visit(RatingUI radio) {}

    /** Visit ToggleButtonUI. */
    default void visit(ToggleButtonUI toggle) {}

    /** Visit CheckBoxUI. */
    default void visit(CheckBoxUI check) {}

    /** Visit MailFieldUI. */
    default void visit(MailFieldUI email) {}

    /** Visit PasswordTextFieldUI. */
    default void visit(PasswordTextFieldUI password) {}

    /** Visit GalleryUI. */
    default void visit(GalleryUI galleryUI) {}

    /** Visit ImageShowcaseUI. */
    default void visit(ImageShowcaseUI showcaseUI) {}

    /** Visit ImageUI. */
    default void visit(ImageUI imageUI) {}

    /** Visit VideoUI. */
    default void visit(VideoUI videoUI) {}

    /** Visit BreadcrumbUI. */
    default void visit(BreadcrumbUI breadcrumbUI) {}

    /** Visit TreeViewUI. */
    default void visit(TreeViewUI treeViewUI) {}

    /** Visit MapUI. */
    default void visit(MapUI mapUI) {}

    /** Visit ProgressBarUI. */
    default void visit(ProgressBarUI progressBarUI) {}

    /** Visit AnchorUI. */
    default void visit(AnchorUI anchorUI) {}

    /** Visit DynamicUI. */
    default void visit(DynamicUI dynamicUI) {}

    /** Visit HasLinkUI. */
    default void visit(HasLinkUI linkUI) {}

    /** Visit MultipleUI. */
    default void visit(MultipleUI multipleUI) {}

    /** Visit IFrame. */
    default void visit(IFrameUI iframeUI) {}

    /** Visit child case. */
    @SuppressWarnings({ "OverlyLongMethod", "OverlyComplexMethod", "ConstantConditions", "OverlyCoupledMethod" })
    default void visitChild(@NotNull final BaseWidgetUI widget) {
        if (widget instanceof HasOptionsUI) visit((HasOptionsUI) widget);
        if (widget instanceof HasRangeOptionsUI) visit((HasRangeOptionsUI) widget);
        if (widget instanceof HasValueUI) visit((HasValueUI) widget);
        if (widget instanceof HasTextLengthUI) visit((HasTextLengthUI) widget);
        if (widget instanceof FieldWidgetUI) visit((FieldWidgetUI) widget);
        if (widget instanceof HasLinkUI) visit((HasLinkUI) widget);
        if (widget instanceof MultipleUI) visit((MultipleUI) widget);
        if (widget instanceof SuggestUI) visit((SuggestUI) widget);
        if (widget instanceof BaseDateUI) visit((BaseDateUI) widget);
        if (widget instanceof WidgetUI) visit((WidgetUI) widget);

        switch (widget.getModel().getWidgetType()) {
        case BUTTON:
            visit((ButtonUI) widget);
            break;
        case CHECK_BOX:
            visit((CheckBoxUI) widget);
            break;
        case COMBO_BOX:
            visit((ComboBoxUI) widget);
            break;
        case DATE_TIME_BOX:
            visit((DateTimeBoxUI) widget);
            break;
        case DATE_BOX:
            visit((DateBoxUI) widget);
            break;
        case COMBO_DATE_BOX:
            visit((ComboDateBoxUI) widget);
            break;
        case DATE_PICKER:
            visit((DatePickerUI) widget);
            break;
        case DOUBLE_DATE_BOX:
            visit((DoubleDateBoxUI) widget);
            break;
        case TIME_PICKER:
            visit((TimePickerUI) widget);
            break;
        case VERTICAL:
        case HORIZONTAL:
        case INPUT_GROUP:
        case HEADER:
        case FOOTER:
        case TABS:
        case DROPDOWN:
            visit((ContainerUI) widget);
            break;
        case DIALOG:
            visit((DialogGroupUI) widget);
            break;
        case POPOVER:
            visit((PopoverUI) widget);
            break;
        case TABLE:
            visit((TableUI) widget);
            break;
        case WIDGET:
            visit((WidgetDefUI) widget);
            break;
        case CHART:
            visit((ChartUI) widget);
            break;
        case LABEL:
            visit((LabelUI) widget);
            break;
        case LIST_BOX:
            visit((ListBoxUI) widget);
            break;
        case PICK_LIST:
            visit((PickListUI) widget);
            break;
        case RADIO_GROUP:
            visit((RadioGroupUI) widget);
            break;
        case RATING:
            visit((RatingUI) widget);
            break;
        case TEXT_AREA:
            visit((TextAreaUI) widget);
            break;
        case RICH_TEXT_AREA:
            visit((RichTextAreaUI) widget);
            break;
        case MESSAGE:
            visit((MessageUI) widget);
            break;
        case TEXT_FIELD:
            visit((TextFieldUI) widget);
            break;
        case DISPLAY:
            if (widget.getModel().isMultiple()) visit((MultipleDisplayUI) widget);
            else visit((DisplayUI) widget);
            break;
        case TAGS:
            visit((TagsUI) widget);
            break;
        case TAGS_COMBO_BOX:
            visit((TagsComboBoxUI) widget);
            break;
        case TAGS_SUGGEST_BOX:
            visit((TagsSuggestBoxUI) widget);
            break;
        case CHECK_BOX_GROUP:
            visit((CheckBoxGroupUI) widget);
            break;
        case SUGGEST_BOX:
        case SEARCH_BOX:
            visit((SuggestBoxUI) widget);
            break;
        case TOGGLE_BUTTON:
            visit((ToggleButtonUI) widget);
            break;
        case MAIL_FIELD:
            visit((MailFieldUI) widget);
            break;
        case PASSWORD_FIELD:
            visit((PasswordTextFieldUI) widget);
            break;
        case GALLERY:
            visit((GalleryUI) widget);
            break;
        case UPLOAD:
            visit((UploadUI) widget);
            break;
        case SHOWCASE:
            visit((ImageShowcaseUI) widget);
            break;
        case IMAGE:
            visit((ImageUI) widget);
            break;
        case BREADCRUMB:
            visit((BreadcrumbUI) widget);
            break;
        case TREE_VIEW:
            visit((TreeViewUI) widget);
            break;
        case MAP:
            visit((MapUI) widget);
            break;
        case VIDEO:
            visit((VideoUI) widget);
            break;
        case PROGRESS:
            visit((ProgressBarUI) widget);
            break;
        case SUBFORM:
            if (widget.getModel().isInline()) visit((InlineSubformUI) widget);
            else visit((AnchoredSubformUI) widget);
            break;
        case ANCHOR:
            visit((AnchorUI) widget);
            break;
        case DYNAMIC:
            visit((DynamicUI) widget);
            break;
        case SECTION:
            visit((SectionUI) widget);
            break;
        case COLOR_PICKER:
            visit((ColorPickerUI) widget);
            break;
        case IFRAME:
            visit((IFrameUI) widget);
            break;
        case FORM:
            throw unreachable();
        default:
            throw new IllegalArgumentException(Constants.TYPE_NOT_SUPPORTED + widget.getModel().getWidgetType());
        }
    }  // end method visitChild
}  // end interface FormViewVisitor
