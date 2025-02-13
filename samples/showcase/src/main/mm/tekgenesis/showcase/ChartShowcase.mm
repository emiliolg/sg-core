package tekgenesis.showcase;

form ChartShowcase "Charts"
    on_load data
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ChartShowcase", style "pull-right";
    };

    "Append Chart Values" : button, on_click more;

    ref "Some value" : Real, default 0;

    div "Other values" : Real, default 1;

    stack "Stack?" : toggle_button, on_change stack, style "marginRight10", default true;

    horizontal {
        line "Line" : chart(line) {
            a : String;
            beavis "Beavis" : Real;
            butthead "Butthead" : Real, is beavis + ref;
        };

        column "Column (No hover, no legend)" : chart(column) {
            b : String;
            cain "Cain" : Real, is abel / div;
            abel "Abel" : Real;
        };
	};

    label "Label" : String, default "Label";

	bar "Bar" : chart(bar) {
        c : String, is label + " " + tom;
        tom "Tom" : Real;
        jerry "Jerry" : Real;
	};

	"Next Rnds" : button, on_click nextRnds;

	pie "Pie" : chart(pie) {
        d : String, is label + " " + superman;
        superman "Superman" : Real;
        key : internal;
	};

    horizontal, style "navbar-fixed-bottom" {
        resolution3 "1280x720" : button, style "pull-right marginRight10", on_click size1280;
        resolution2 "854x480" : button, style "pull-right", on_click size854;
        resolution1 "640x480" : button, style "pull-right", on_click size640;
        resolution0 "Auto" : button, style "pull-right", on_click sizeAuto;
    };
}

form ClickChartShowcase "Charts Click"
    on_load data
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ClickChartShowcase", style "pull-right";
    };

    series1Label "Series1 Label" : text_field, default "Men";
    series2Label "Series2 Label" : text_field, default "Women";

    menOnOff "Men" : Boolean, default true, label_expression series1Label;
    womenOnOff "Women" : Boolean, default true, label_expression series2Label;

    label "Chart Label" : text_field, default "Chart";

    column "Chart" : chart(column), on_click doClick, label_expression label {
        month : String;
        men "Men" : Real, hide_column when !menOnOff, label_expression series1Label;
        key : internal;
        women "Women" : Real, hide_column when !womenOnOff, label_expression series2Label;
    };

    currentMonth : message(info);

    reload "Reload" : button, on_click reload;

    line "Line With Optionals" : chart(line), on_click lineClick {
        chartLabel: String;
        serie1 : Decimal(20,2), optional;
        serie2 : Decimal(20,2), optional;
        serie3 : Decimal(20,2), optional;
    };
}

form DrillDownChart {

    header { message(title); };

    series1Label : String, internal;
    series2Label : String, internal;

    back "Back" : button, on_click back;

    column "Chart" : chart(column), on_click drilldown {
        key : internal;
        label : String;
        series1 : Real, label_expression series1Label;
        series2 : Real, label_expression series2Label;
    };
}

form MiniChart "Mini Chart"
    on_load load
    on_schedule refresh 10
{
    sessions "Sessions" : chart(line) { sessionsCount : Int; };
    currentSessionOpened "Current sessions" : Int, display;
}

enum SampleSeriesMode {
    STACKED;
    OVERLAPPED;
    SIDE_BY_SIDE;
}

form ChartDualAxis
    on_load data
{
    header { message(title); };

    chart "Chart" : chart(column) {
        key : internal;
        label : String;
        value "Int" : Int, axis 1;
        percentage "Percentage" : Decimal(5,2), axis 2;
    };
}

form ChartDataShowcase "Charts Data"
    on_load data
{
    header {
        message(title);
        "View Source" : label, link "/sg/view/source/tekgenesis.showcase.ChartDataShowcase", style "pull-right";
    };

    size "Size" : horizontal, style "marginRight10" {
        resolution3 "1280x720" : button, style "marginRight10", on_click size1280;
        resolution2 "854x480" : button, style "marginRight10", on_click size854;
        resolution1 "640x480" : button, style "marginRight10", on_click size640;
        resolution0 "320x240" : button, on_click size320;
    };

    options "Options" : horizontal {
        barWidth : Real, no_label, check barWidth <= 1 : "Must be less or equals to 1.", default 0.8, on_ui_change changeWidth, hint "Bar Width";
        seriesMode "Series Mode" : SampleSeriesMode, on_ui_change stack, default STACKED, no_label;
        hoverable "Hoverable?" : toggle_button, on_ui_change hoverable, style "marginRight10", default true;
        legend "Legend?" : toggle_button, on_ui_change legend, style "marginRight10", default true;
        showLabels "Show labels on hover?" : toggle_button, style "marginRight10", on_ui_change showLabels, default false;
        verticalLabels "Vertical labels?" : toggle_button, on_ui_change toVerticalLabels, default false;
        stepped "Stepped?" : toggle_button, on_ui_change toStepped, default false;
    };

    colors "Colors" : horizontal {
        defaultColors "Default" : button, style "marginRight10", on_click defaultColors;
        red "Red" : button, style "marginRight10", on_click red;
        blue "Blue" : button, style "marginRight10", on_click blue;
        green "Green" : button, style "marginRight10", on_click green;
        palette1 "Palette1" : button, style "marginRight10", on_click palette1;
        palette2 "Palette2" : button, style "marginRight10", on_click palette2;
        palette3 "Palette3" : button, on_click palette3;
    };

    series "Series" : horizontal {
        showInt "Int" : Boolean, default true;
        showReal "Real" : Boolean, default true;
        showBigDecimal "Big Decimal" : Boolean, default true;
    };

    column "Chart" : chart(line), on_click doClick {
        key : internal;
        label : String;
        integer "Int" : Int, hide_column when !showInt;
        real "Real" : Real, hide_column when !showReal;
        bigDecimal "BigDecimal" : Decimal(5,2), hide_column when !showBigDecimal;
    };

    current : message(info);

    data "Reload data" : button, on_click reloadData;
}

form ChartLineSegments
    on_load load
{
    header {
        message(title);
    };

    segments "Chart" : chart(line), on_click doClick {
        key : internal;
        label : String;
        integer "Int" : Int, optional;
    };

    current : message(info);
}

form ToggleCharts {
    header { message(title); };

    search "Search" : button, on_click search;

    byCategoryChart "Toggle" : toggle_button, default false;

    salesByStoreChart : chart(column), hide when !byCategoryChart {
        storeKey : internal;
        storeLabel: String, default "Stores";
        salesByStoreAmount: Decimal(12, 2);
    };

    salesByCategoryChart : chart(column), hide when byCategoryChart {
        categoryKey : internal;
        categoryLabel: String, default "Categories";
        salesByCategoryAmount: Decimal(12, 2);
    };
}