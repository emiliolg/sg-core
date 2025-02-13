package tekgenesis.enums;

enum PersonType
with {
    ageFrom    : Int;
    ageTo      : Int;
    sex        : Sex;
    retired    : Boolean;
}
{
    BOY       : "Boy", 0, 12, M, false;
    GIRL      : "Girl", 0, 12, F, false;
    MALE_TEEN : "Young Male", 13, 18, M, false;
}

enum Sex {
    F : "Female";
    M : "Male";
}

enum WeekDay
with {
    working    : Boolean;
}
{
    SUNDAY : "Sunday", false;
    MONDAY : "Monday", true;
}
