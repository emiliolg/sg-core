
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.controller;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SuggestOracle;

import org.jetbrains.annotations.NotNull;

import tekgenesis.check.CheckType;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.configuration.DynamicConfig;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Type;
import tekgenesis.view.client.formatter.InputHandlerFactory;
import tekgenesis.view.client.ui.*;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.metadata.form.widget.ButtonType.DELETE;
import static tekgenesis.metadata.form.widget.ToggleButtonType.DEPRECATE;
import static tekgenesis.view.client.ClientUiModelContext.getRetriever;

/**
 * Utility to create the View from the Form.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "OverlyComplexClass" })  // cause we have too many widgets :-)
public class ViewCreator {

    //~ Instance Fields ..............................................................................................................................

    private final boolean hasLabel;
    private final boolean isUpdate;

    //~ Constructors .................................................................................................................................

    private ViewCreator() {
        this(false, false);
    }

    private ViewCreator(boolean withLabel, boolean isUpdate) {
        hasLabel      = withLabel;
        this.isUpdate = isUpdate;
    }

    //~ Methods ......................................................................................................................................

    MailFieldUI createMailFieldUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final MailFieldUI view = new MailFieldUI(container, model);
        populateWidget(view, model);
        view.setLength(model.getLength(), model.isExpand());
        return view;
    }

    TextFieldUI createTextFieldUI(@NotNull final ModelUI container, final Widget model, HasWidgetsUI parent) {
        return createTextFieldUI(container, model, parent, model.isSecret());
    }

    @SuppressWarnings({ "OverlyLongMethod", "OverlyComplexMethod", "WeakerAccess" })
    void createView(@NotNull final ModelUI container, @NotNull final WidgetType widgetType, @NotNull final Widget widget, boolean withLabel,
                    HasWidgetsUI parent) {
        switch (widgetType) {
        case BUTTON:
            // add the delete only for updates
            if (isUpdate || widget.getButtonType() != DELETE) append(parent, createButtonUI(container, widget));
            break;
        case CHECK_BOX:
            append(parent, createCheckBoxUI(container, widget), withLabel);
            break;
        case COMBO_BOX:
            append(parent, createComboBoxUI(container, widget), withLabel);
            break;
        case DATE_TIME_BOX:
            append(parent, createDateTimeBoxUI(container, widget), withLabel);
            break;
        case DATE_BOX:
            append(parent, createDateBoxUI(container, widget), withLabel);
            break;
        case COMBO_DATE_BOX:
            append(parent, createComboDateBoxUI(container, widget), withLabel);
            break;
        case DATE_PICKER:
            append(parent, createDatePickerUI(container, widget), withLabel);
            break;
        case DOUBLE_DATE_BOX:
            append(parent, createDoubleDateBoxUI(container, widget), withLabel);
            break;
        case TIME_PICKER:
            append(parent, createTimePickerUI(container, widget), withLabel);
            break;
        case HORIZONTAL:
        case VERTICAL:
        case HEADER:
        case FOOTER:
            final GroupUI horizontal = createGroupUI(container, widget);
            traverseOptionalLabel(container, widget, horizontal);
            append(parent, horizontal, withLabel && widget.hasLabel());
            break;
        case DROPDOWN:
            final DropDownUI dropDownUI = createDropdownUI(container, widget);
            traverse(container, widget, dropDownUI);
            append(parent, dropDownUI, false);
            break;
        case INPUT_GROUP:
            final InputGroupUI inputGroup = createInputGroupUI(container, widget);
            traverseNoLabel(container, widget, inputGroup);
            append(parent, inputGroup, withLabel);
            break;
        case DIALOG:
            final ContainerUI dialog = createDialogUI(container, widget);
            traverse(container, widget, dialog);
            append(parent, dialog, withLabel);
            break;
        case POPOVER:
            final ContainerUI popover = createPopoverUI(container, widget);
            traverse(container, widget, popover);
            append(parent, popover, withLabel);
            break;
        case TABS:
            final ContainerUI tabs = createTabsUI(container, widget);
            traverseOptionalLabel(container, widget, tabs);
            append(parent, tabs, withLabel && widget.hasLabel());
            break;
        case TABLE:
            final TableUI tableUI = createTableUI(container, (MultipleWidget) widget);
            traverse(container, widget, tableUI);
            append(parent, tableUI, withLabel);
            break;
        case SECTION:
            final SectionUI sectionUI = createSectionUI(container, (MultipleWidget) widget);
            append(parent, sectionUI, withLabel);
            break;
        case CHART:
            append(parent, createChartUI(container, (MultipleWidget) widget), withLabel);
            break;
        case LABEL:
            append(parent, createLabelUI(container, widget));
            break;
        case LIST_BOX:
            append(parent, createListBoxUI(container, widget), withLabel);
            break;
        case PICK_LIST:
            append(parent, createPickListUI(container, widget), withLabel);
            break;
        case RADIO_GROUP:
            append(parent, createRadioGroupUI(container, widget), withLabel);
            break;
        case RATING:
            append(parent, createRatingUI(container, widget), withLabel);
            break;
        case BREADCRUMB:
            append(parent, createBreadcrumbUI(container, widget), withLabel);
            break;
        case TREE_VIEW:
            append(parent, createTreeViewUI(container, widget), withLabel);
            break;
        case MAP:
            append(parent, createMapUI(container, widget), withLabel);
            break;
        case PROGRESS:
            append(parent, createProgressBarUI(container, widget), withLabel);
            break;
        case TEXT_AREA:
            append(parent, createTextAreaUI(container, widget), withLabel);
            break;
        case RANGE:
            append(parent, createRangeUI(container, widget), withLabel);
            break;
        case RANGE_VALUE:
            append(parent, createRangeValueUI(container, widget), withLabel);
            break;
        case RICH_TEXT_AREA:
            append(parent, createRichTextAreaUI(container, widget), withLabel);
            break;
        case TEXT_FIELD:
            append(parent, createTextFieldUI(container, widget, parent), withLabel);
            break;
        case COLOR_PICKER:
            append(parent, createColorPickerUI(container, widget), withLabel);
            break;
        case DISPLAY:
            append(parent, widget.isMultiple() ? createMultipleDisplayUI(container, widget) : createDisplayUI(container, widget), withLabel);
            break;
        case MESSAGE:
            append(parent, createMessageUI(container, widget), withLabel);
            break;
        case TAGS:
            append(parent, createTagsUI(container, widget), withLabel);
            break;
        case TAGS_SUGGEST_BOX:
            append(parent, createTagsSuggestBoxUI(container, widget), withLabel);
            break;
        case TAGS_COMBO_BOX:
            append(parent, createTagsComboBoxUI(container, widget), withLabel);
            break;
        case CHECK_BOX_GROUP:
            append(parent, createCheckBoxGroupUI(container, widget), withLabel);
            break;
        case MAIL_FIELD:
            append(parent, createMailFieldUI(container, widget), withLabel);
            break;
        case PASSWORD_FIELD:
            append(parent, createPasswordTextFieldUI(container, widget), withLabel);
            break;
        case SUGGEST_BOX:
        case SEARCH_BOX:
            append(parent, createSuggestBoxUI(container, widget), withLabel);
            break;
        case TOGGLE_BUTTON:
            // add the deprecate only for updates
            if (isUpdate || widget.getToggleButtonType() != DEPRECATE) append(parent, createToggleButtonUI(container, widget));
            break;
        case GALLERY:
            append(parent, createImageGalleryUI(container, widget), withLabel);
            break;
        case UPLOAD:
            append(parent, createImageUploadUI(container, widget), withLabel);
            break;
        case SHOWCASE:
            append(parent, createShowcaseUI(container, widget), withLabel);
            break;
        case IMAGE:
            append(parent, createImageUI(container, widget), withLabel);
            break;
        case VIDEO:
            append(parent, createVideoUI(container, widget), withLabel);
            break;
        case SUBFORM:
            if (widget.isInline()) append(parent, createInlineSubformUI(container, widget), false);
            else append(parent, createSubformUI(container, widget), withLabel);
            break;
        case ANCHOR:
            append(parent, createAnchorUI(container, widget), withLabel);
            break;
        case DYNAMIC:
            append(parent, createDynamicUI(container, widget), withLabel);
            break;
        case INTERNAL:
            // No UI
            break;
        case WIDGET:
            final WidgetDef   def = getRetriever().getWidget(createQName(widget.getWidgetDefinitionFqn()));
            final WidgetDefUI ui  = createWidgetDefUI(container, widget, def);
            if (widget.isRequired()) traverse(ui, def, ui);
            append(parent, ui, true);
            break;
        case IFRAME:
            append(container, createIFrameUI(container, widget), withLabel);
            break;
        case FORM:
        case NONE:
            throw new IllegalArgumentException(Constants.TYPE_NOT_SUPPORTED + widgetType);
        }
    }  // end method createView

    void populateHasInputHandler(final HasInputHandlerUI view, final Widget model) {
        Type type = model.getType();
        if (type.isArray()) type = ((ArrayType) type).getElementType();
        view.setInputHandler(InputHandlerFactory.create(model.getInputHandler(), type, model.isSigned()));
    }

    private ButtonUI createButtonUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ButtonUI view = new ButtonUI(container, model, isUpdate);
        populateWidget(view, model);
        return view;
    }

    private CheckBoxGroupUI createCheckBoxGroupUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final CheckBoxGroupUI view = new CheckBoxGroupUI(container, model);
        populateHasInputHandler(view, model);
        populateWidget(view, model);
        return view;
    }

    private ColorPickerUI createColorPickerUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ColorPickerUI view = new ColorPickerUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private ComboBoxUI createComboBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ComboBoxUI view = new ComboBoxUI(container, model);
        populateHasInputHandler(view, model);
        populateWidget(view, model);
        return view;
    }

    private DisplayUI createDisplayUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final DisplayUI view = new DisplayUI(container, model);

        populateHasInputHandler(view, model);
        populateWidget(view, model);
        view.setTextLength(model.getLength());

        return view;
    }

    private ListBoxUI createListBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ListBoxUI view = new ListBoxUI(container, model);

        populateHasInputHandler(view, model);
        populateWidget(view, model);

        view.setVisibleItemCount(model.getVisibleRows());

        return view;
    }

    private MultipleDisplayUI createMultipleDisplayUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final MultipleDisplayUI view = new MultipleDisplayUI(container, model);

        populateHasInputHandler(view, model);
        populateWidget(view, model);

        view.setLength(model.getLength());

        return view;
    }

    private PickListUI createPickListUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final PickListUI view = new PickListUI(container, model);

        populateHasInputHandler(view, model);
        populateWidget(view, model);

        return view;
    }

    private RadioGroupUI createRadioGroupUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final RadioGroupUI view = new RadioGroupUI(container, model);
        populateHasInputHandler(view, model);
        populateWidget(view, model);
        return view;
    }

    private RangeUI createRangeUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final RangeUI view = new RangeUI(container, model);

        populateHasInputHandler(view, model);
        populateWidget(view, model);

        return view;
    }

    private RangeValueUI createRangeValueUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final RangeValueUI view = new RangeValueUI(container, model);

        populateHasInputHandler(view, model);
        populateWidget(view, model);

        return view;
    }

    private RatingUI createRatingUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final RatingUI view = new RatingUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private SuggestBoxUI createSuggestBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final SuggestBoxUI view = new SuggestBoxUI(container, model);

        populateHasInputHandler(view, model);
        populateWidget(view, model);

        view.setLength(model.getLength());
        return view;
    }

    private TagsSuggestBoxUI createTagsSuggestBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final TagsSuggestBoxUI view = new TagsSuggestBoxUI(container, model);
        populateHasInputHandler(view, model);
        populateWidget(view, model);
        return view;
    }

    private TagsUI createTagsUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final TagsUI view = new TagsUI(container, model);
        populateWidget(view, model);

        populateHasInputHandler(view, model);

        view.setLength(model.getLength());

        return view;
    }

    private TextFieldUI createTextFieldUI(@NotNull final ModelUI container, @NotNull final Widget model, HasWidgetsUI parent, boolean secret) {
        final TextFieldUI view = new TextFieldUI(container, model, secret);
        populateHasInputHandler(view, model);
        populateWidget(view, model);

        view.setLength(model.getLength(), model.isExpand());
        view.setNoPaste(model.getNoPaste());

        return view;
    }

    private ToggleButtonUI createToggleButtonUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ToggleButtonUI view = new ToggleButtonUI(container, model);
        populateWidget(view, model);
        return view;
    }

    /**
     * Traverse children adding the label even if empty so it's aligned (leaving the dom structure).
     */
    private void traverse(@NotNull final ModelUI container, @NotNull final Iterable<Widget> children, @NotNull final HasWidgetsUI parent) {
        for (final Widget widget : children)
            createView(container, widget.getWidgetType(), widget, hasLabel, parent);
    }

    /** Traverse children adding with out adding the label. */
    private void traverseNoLabel(@NotNull final ModelUI container, @NotNull final Iterable<Widget> children, HasWidgetsUI parent) {
        for (final Widget widget : children)
            createView(container, widget.getWidgetType(), widget, false, parent);
    }

    /** Traverse children adding the label only if it's not empty. */
    private void traverseOptionalLabel(@NotNull final ModelUI container, @NotNull final Iterable<Widget> children, HasWidgetsUI parent) {
        for (final Widget widget : children)
            createView(container, widget.getWidgetType(), widget, hasLabel && widget.hasLabel(), parent);
    }

    //~ Methods ......................................................................................................................................

    public static WidgetUI appendLabelAndIcon(WidgetUI view, boolean isWithLabel) {
        // add label if needed
        final boolean  withLabel       = isWithLabel && !view.getModel().getNoLabel();
        final WidgetUI widgetWithLabel = withLabel ? view.withLabel() : view;

        // add icon if needed
        final boolean withIcon = view.getModel().hasIcon();
        return withIcon ? widgetWithLabel.withIcon() : widgetWithLabel;
    }

    /** Create the view for all the specified Widgets. */
    public static void createSingleView(@NotNull final ModelUI container, Widget widget, HasWidgetsUI view) {
        new ViewCreator().createView(container, widget.getWidgetType(), widget, false, view);
    }

    /** Create the view for all the specified Widgets. */
    public static void createView(@NotNull final ModelUI container, Iterable<Widget> widget, HasWidgetsUI view) {
        createView(container, widget, view, false);
    }

    /** Create the view for the whole model. */
    public static void createView(@NotNull final UiModel metadata, @NotNull final ModelUI view, boolean isUpdate) {
        new ViewCreator(true, isUpdate).traverse(view, metadata, view);
    }

    /** Create the view for all the specified Widgets. */
    public static void createView(@NotNull final ModelUI container, Iterable<Widget> widget, HasWidgetsUI view, boolean withLabel) {
        new ViewCreator(withLabel, false).traverse(container, widget, view);
    }

    private static void append(HasWidgetsUI container, WidgetUI view) {
        append(container, view, false);
    }

    private static void append(HasWidgetsUI container, WidgetUI view, boolean isWithLabel) {
        final WidgetUI widgetWithIcon = appendLabelAndIcon(view, isWithLabel || container instanceof PopoverUI);
        // append child
        container.addChild(widgetWithIcon);
    }

    private static AnchorUI createAnchorUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final AnchorUI view = new AnchorUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static BreadcrumbUI createBreadcrumbUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final BreadcrumbUI view = new BreadcrumbUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static ChartUI createChartUI(@NotNull final ModelUI container, @NotNull final MultipleWidget model) {
        final ChartUI view = new ChartUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static CheckBoxUI createCheckBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final CheckBoxUI view = new CheckBoxUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static ComboDateBoxUI createComboDateBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ComboDateBoxUI view = new ComboDateBoxUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static DateBoxUI createDateBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final DateBoxUI view = new DateBoxUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static DatePickerUI createDatePickerUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final DatePickerUI view = new DatePickerUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static DateTimeBoxUI createDateTimeBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final DateTimeBoxUI view = new DateTimeBoxUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static ContainerUI createDialogUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ContainerUI view = new DialogGroupUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static DoubleDateBoxUI createDoubleDateBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final DoubleDateBoxUI view = new DoubleDateBoxUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static DropDownUI createDropdownUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final DropDownUI view = new DropDownUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static DynamicUI createDynamicUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final DynamicUI view = new DynamicUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static GroupUI createGroupUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final GroupUI view = new GroupUI(container, model);
        populateWidget(view, model);
        return view;
    }
    private static IFrameUI createIFrameUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final IFrameUI frameUI = new IFrameUI(container, model);
        populateWidget(frameUI, model);
        return frameUI;
    }

    private static GalleryUI createImageGalleryUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final GalleryUI view = new GalleryUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static ImageUI createImageUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ImageUI view = new ImageUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static UploadUI createImageUploadUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final UploadUI view = new UploadUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static WidgetUI createInlineSubformUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final InlineSubformUI view = new InlineSubformUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static InputGroupUI createInputGroupUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final InputGroupUI view = new InputGroupUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static LabelUI createLabelUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final LabelUI view = new LabelUI(container, model);
        populateWidget(view, model);
        view.setLabel(model.getLabel());
        return view;
    }

    private static MapUI createMapUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final MapUI view = new MapUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static MessageUI createMessageUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final MessageUI view = model.getMsgType() == CheckType.TITLE || model.getMsgType() == CheckType.ENTITY
                               ? new TitleMessageUI(container, model)
                               : new WidgetMessageUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static PasswordTextFieldUI createPasswordTextFieldUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final PasswordTextFieldUI view = new PasswordTextFieldUI(container, model);
        populateWidget(view, model);
        view.setLength(model.getLength(), model.isExpand());
        return view;
    }
    private static ContainerUI createPopoverUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ContainerUI view = new PopoverUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static ProgressBarUI createProgressBarUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ProgressBarUI view = new ProgressBarUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static RichTextAreaUI createRichTextAreaUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final RichTextAreaUI view = new RichTextAreaUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static SectionUI createSectionUI(@NotNull final ModelUI container, @NotNull final MultipleWidget model) {
        final SectionUI view = new SectionUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static ImageShowcaseUI createShowcaseUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ImageShowcaseUI view = new ImageShowcaseUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static WidgetUI createSubformUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final AnchoredSubformUI view = new AnchoredSubformUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static TableUI createTableUI(@NotNull final ModelUI container, @NotNull final MultipleWidget model) {
        final TableUI view = new TableUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static ContainerUI createTabsUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final ContainerUI view = new TabGroupUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static TagsComboBoxUI createTagsComboBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final TagsComboBoxUI view = new TagsComboBoxUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static TextAreaUI createTextAreaUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final TextAreaUI view = new TextAreaUI(container, model);
        populateWidget(view, model);
        view.setVisibleLines(model.getVisibleRows());
        view.setLength(model.getLength());
        return view;
    }

    private static TimePickerUI createTimePickerUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final TimePickerUI view = new TimePickerUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static TreeViewUI createTreeViewUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final TreeViewUI view = new TreeViewUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static VideoUI createVideoUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        final VideoUI view = new VideoUI(container, model);
        populateWidget(view, model);
        return view;
    }

    private static WidgetDefUI createWidgetDefUI(@NotNull final ModelUI container, @NotNull final Widget model, WidgetDef def) {
        final WidgetDefUI view = new WidgetDefUI(container, model, def);
        populateWidget(view, model);
        return view;
    }

    private static void populateWidget(final WidgetUI view, final Widget model) {
        view.setId();
    }

    //~ Inner Classes ................................................................................................................................

    public static class Dynamic extends ViewCreator {
        private final DynamicConfig                                config;
        private final String                                       id;
        private final Type                                         modelType;
        private Option<SelectionHandler<SuggestOracle.Suggestion>> selectionHandler = Option.empty();

        private Dynamic(DynamicConfig config, String id) {
            this.config = config;
            modelType   = config.getType();
            this.id     = id;
        }

        /** Has common behaviour and adds the selection handler. */
        @Override MailFieldUI createMailFieldUI(@NotNull final ModelUI container, @NotNull final Widget model) {
            final MailFieldUI mailFieldUI = super.createMailFieldUI(container, model);
            for (final SelectionHandler<SuggestOracle.Suggestion> handler : selectionHandler)
                mailFieldUI.addSelectionHandler(handler);
            return mailFieldUI;
        }

        @Override TextFieldUI createTextFieldUI(@NotNull final ModelUI container, @NotNull final Widget model, HasWidgetsUI parent) {
            final TextFieldUI view = super.createTextFieldUI(container, model, parent, config.isSecret());
            // if (modelType.isNumber()) view.addStyleName(NUMBER_STYLE);
            if (modelType.isString()) for (final Integer integer : modelType.getLength())
                view.setLength(integer, model.isExpand());
            return view;
        }

        @Override void populateHasInputHandler(HasInputHandlerUI view, Widget model) {
            view.setInputHandler(InputHandlerFactory.create(model.getInputHandler(), modelType, config.isSigned()));
        }

        /** Sets selection handler that will be only used if dynamic is mailfield. */
        void setSelectionHandler(Option<SelectionHandler<SuggestOracle.Suggestion>> handler) {
            selectionHandler = handler;
        }

        /** Create the view for a dynamic widget. */
        public static void createView(@NotNull final ModelUI container, @NotNull final Widget widget, @NotNull final DynamicUI view,
                                      @NotNull final DynamicConfig config) {
            final Dynamic dynamic = new Dynamic(config, view.getId());
            dynamic.setSelectionHandler(view.getSelectionHandler());
            dynamic.createView(container, config.getWidget(), widget, false, view);
        }
    }  // end class Dynamic
}  // end class ViewCreator
