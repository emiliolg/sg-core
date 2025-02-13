package tekgenesis.showcase;

menu ShowcaseMenu "Showcase Gallery"
{
    WidgetShowcaseMenu;
    ExamplesMenu;
    BootstrapMenu;
}

link InlineHtml "Inline html fragment" = "#fragment/external";
link Google "Go to Google" = "http://www.google.com";

menu WidgetShowcaseMenu "Widgets Showcase"
{
    ChartShowcase;
    ClickChartShowcase;
    ChartDataShowcase;
    InlineHtml;
    IFrameShowcase;
    Google;
    DateShowcaseForm;
    DisplayShowcase;
    ExpressionShowcase;
    GroupShowcase;
    ValidateGroupForm;
    SubformsShowcase;
    SubformsExpressions;
    SuggestBoxShowcase;
    ClassroomForm;
    TableGroupHideShowcase;
    TableLocalGlobalOptionsShowcase;
    TextFieldShowcaseForm;
    ValidationShowcase;
    MailFieldShowcaseForm;
    TextShowcaseForm;
    ViewShowcaseForm;
    LinkShowcaseForm;
    MapForm;
    MapDragForm;
    MapConfigurationForm;
    SectionForm;
    DynamicForm;
    DeprecableEntityForm;
    AnotherDeprecableEntityForm;
    NumbersForm;
    WidgetShowcase;
	OptionWidgetsForm;
    InsideScreenForm;
    UploadForm;
    CustomMaskForm;
    DialogsForm;
    StyleShowcase;
	InlineSubformShowcase;
	CrazyEventsShowcase;
	NavigateWithParameters;
	SubformClearForm;
	ImageVariantForm;
	IconShowcaseForm;
}

menu BootstrapMenu "Bootstrap Showcase"
{
    Bs3;
    GroupShowcaseForm;
    HideShowcaseForm;
    AddressShowcaseForm;
    ClientShowcase;
}

menu ExamplesMenu "Examples" {
    GantTableForm;
    UserDetailsForm;
    CarForm;
    CarListForm;
    CarFilterForm;
    TableFilter;
    ImageForm;
    HtmlGeneratorForm;
    ItemForm;
    ItemViewer;
    NodeForm;
    FilterNodeForm;
    RelatedTablesForm;
    SimpleEntityForm;
    AnotherSimpleEntityForm;
    Table;
    TestForm;
    FlightForm;
    Flights;
    Car1992;
    Car1994;
}

link Car1992 "Car 1992" = CarForm(year=1992, engine=GAS, air=true, bluetooth=false, price=32);

link Car1994 "Car 1994" = CarForm(year=1994, engine=GAS, air=false, bluetooth=true, price=35);
