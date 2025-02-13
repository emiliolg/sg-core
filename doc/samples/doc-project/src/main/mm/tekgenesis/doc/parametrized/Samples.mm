package tekgenesis.doc.parametrized;
//#sampleForm
form SampleForm
    parameters name, created
{
    name : String, default "Ruby";
    created : Int, default 2014;
    expiration : Int, is created + 1;
    email : String, optional;
}
//#sampleForm