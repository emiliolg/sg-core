package tekgenesis.samples.form;

entity AutoincrementWithInner "Autoincrement with Inner"
{
    fieldA : String(30);

    /* Use same name on field as inner class to check code generation */
    innerA : entity InnerA* {
        innerFieldA : Int;
    };
}

form AutoincrementWithInnerForm "Autoincrement with Inner (Default)" entity AutoincrementWithInner;

entity BaseWithKey "Base with Key"
    primary_key pkA, pkB
    searchable
{
    pkA : String(30);
    pkB : String(30);
    fieldA : String(30);
    withKeyBackRef "Back reference" : AutoincrementWithMultiple;
}

entity BaseWithKeyUsingRef "Base with Key using Back Reference"
    primary_key pkC, pkD, backRef
{
    pkC : String(30);
    pkD : String(30);
    fieldB : String(30);
    backRef "Back reference" : AutoincrementWithMultiple;
}

entity BaseWithAutoincrement "Base with Autoincrement"
    searchable
{
    fieldC : String(30);
    withAutoincrementBackRef "Back reference" : AutoincrementWithMultiple;
}

entity AutoincrementWithMultiple "Autoincrement with Multiple"
{
    fieldD : String(30); /* fieldA || fieldB won't compile. We shouldn't use prefixes for Fields enum? */

    /* Use same name on field as the multiple class to check code generation */
    baseWithKey : BaseWithKey*;

    baseWithKeyUsingRef : BaseWithKeyUsingRef*;

    baseWithAutoincrement : BaseWithAutoincrement*;
}

form AutoincrementWithMultipleForm "Autoincrement With Multiple Form" entity AutoincrementWithMultiple;

entity EntityWithKeyOfSameType "Base with key using same type"
    primary_key fieldH, fieldI, fieldJ
{
    fieldH : BaseWithComplexMultiple;
    fieldI : Int;
    fieldJ : BaseWithComplexMultiple;
    fieldK : String(30);
}

entity BaseWithComplexMultiple "Autoincrement With Complex Multiple"
    primary_key fieldE, fieldF
    searchable
{
    fieldE : String(30);
    fieldF : String(30);
    fieldG : String(30);

    multipleWithSameType : EntityWithKeyOfSameType* using fieldH;
}

form BaseWithComplexMultipleForm "Base With Complex Multiple Form" entity BaseWithComplexMultiple;

entity BaseEntitiesWithKeys ""
    primary_key entA, entB
{
    entA:BaseWithKey;
    entB:BaseWithKey;
}

form BaseEntitiesWithKeysForm "Base Entities With Keys Form" entity BaseEntitiesWithKeys;

form BaseWithListingEntitiesWithKeysForm listing BaseEntitiesWithKeys;

entity BaseEntitiesPartialKeys ""
    primary_key entA
{
    entA:BaseWithKey;
    entB:BaseWithAutoincrement;
}

form BaseWithListingEntitiesWithPartialKeysForm listing BaseEntitiesPartialKeys;

entity BaseEntitiesWithAutIncKey ""
    primary_key entA, entB
{
    entA:BaseWithKey;
    entB:BaseWithAutoincrement;
}

form BaseEntitiesWithAutIncKeyForm listing BaseEntitiesWithAutIncKey;

form BaseEntitiesDiffBindingNameForm  {
    myTable "" : BaseWithKey, table(12) {
        primaryKeyA : pkA;
        primaryKeyB : pkB;
    };
    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, myTable), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, myTable);
    };
}

form BaseEntitiesBindingEntityNotUsingFieldsForm  {
      myTable "" : BaseWithKey, table(12) {
            acode "Code"                : String(3),display;
            aname "Name"                : String(150),display;
            aicon "Icon"                : image,optional, style "thumb";
      };
      horizontal, style "margin-top-20" {
            addRowBottom "Add" : button(add_row, myTable), style "margin-right-5";
            removeRowBottom "Remove" : button(remove_row, myTable);
      };
}

entity InsuranceCoverage "InsuranceCoverage" primary_key code
{
   code: String(20);
   name: String(2000);
}

form CoveragesForm "Coverage Form" entity InsuranceCoverage
{
    id: code, internal;

    coverages : InsuranceCoverage, table(50) {
        key: String (20),internal;
        code "Code"                : String (20),display;
    };
}