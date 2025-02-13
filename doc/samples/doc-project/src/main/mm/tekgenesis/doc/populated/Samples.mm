package tekgenesis.doc.populated;
//#sampleForm
form SampleForm
    primary_key name
{
    name : String, default "Ruby";
    created : Int, default 2014;
    expiration : Int, is created + 1;
    email : String, optional;
}
//#sampleForm