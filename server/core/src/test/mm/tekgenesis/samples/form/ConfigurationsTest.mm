package tekgenesis.samples.form;

form ChartConfigTest
{
    chart "Chart" : chart(column) {
        key : internal;
        label : String;
        value "Int" : Int, axis 1;
        percentage "Percentage" : Decimal(5,2), axis 2;
    };
}

