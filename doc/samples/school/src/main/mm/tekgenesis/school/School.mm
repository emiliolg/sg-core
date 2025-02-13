package tekgenesis.school;

entity Family searchable primary_key familyId{
    familyId:Int;
    familyName:String;
}

//#Student
entity Student
primary_key  id searchable described_by lastNames,names, id{
    family: Family;
    id: String;
    names: String;
    lastNames: String;
    sex: Sex;
    startYear: Int;
    birthday: Date;
    currentCourse: Course;
}
//#Student
//#Course
entity Course searchable described_by description{
    grade: Int;
    division: String;
    description: String;
}
//#Course
enum Sex{
    Female;
    Male;
}


//#SchoolForm
form StudentsForm entity Student;
//#SchoolForm
form StudentsListing listing Student;

form AnotherStudentsListing "Another Students Listing"

{
    header {
        message(title), col 12;
    };
    students    : Student, table, on_change saveStudent, on_load loadStudents {
        "Family"         : family;
        "Id"             : id;
        "Names"          : names;
        "Last Names"     : lastNames;
        "Sex"            : sex;
        "Start Year"     : startYear, mask decimal;
        "Birthday"       : birthday;
        "Current Course" : currentCourse;
    };
    horizontal, style "margin-top-20" {
        button(add_row, students), disable when forbidden(create), style "margin-right-5";
        button(remove_row, students), disable when forbidden(delete), on_click removeStudent;
    };
}


form GradeStudentsListing "Grade Students Listing"
{

    header {
        message(title), col 12;
        course "Choose Grade" : Course, on_change doLoadStudents;
    };
    gradeStudents    : Student, table, hide when course==null{
        "Family"         : family;
        "Id"             : id;
        "Names"          : names;
        "Last Names"     : lastNames;
        "Sex"            : sex;
        "Start Year"     : startYear, mask decimal;
        "Birthday"       : birthday;
    };
//    horizontal, style "margin-top-20" {
//        button(add_row, students), disable when forbidden(create), style "margin-right-5";
//        button(remove_row, students), disable when forbidden(delete), on_click removeStudent;
//    };
}

//#CourseForm
form CoursesForm entity Course;
//#CourseForm
//#CourseListing
form CoursesListing listing Course;

form FamiliesForm entity Family;

form FamilyListing listing Family;
//#CourseListing

//#SchoolMenu
menu SchoolMenu{
    StudentsForm;
    CoursesForm;
    FamiliesForm;
    FamilyListing;
    CoursesListing;
    StudentsListing;
    GradeStudentsListing;
}
//#SchoolMenu

