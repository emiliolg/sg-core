package tekgenesis.school;

entity CourseHomeWork searchable described_by course,year,givenDate
{
    course:Course;
    year: Int;
    givenDate: DateTime;
    dueDate:DateTime;
    homeworkSheets:entity HomeWorkData*{
        fileReference:String;
    };
}

entity Test searchable described_by course,year,date{
    course:Course;
    year: Int;
    date:DateTime;
    testData:entity TestData*{
        fileReference:String;
    };
}
entity HomeWorkGrade searchable described_by homeWork,homeWorkStudent{
    homeWork:CourseHomeWork;
    homeWorkStudent:Student;
    homeWorkGrade:Int;
    homeWorkGradeBase:Int;
}

entity TestGrade searchable described_by test,student{
    test:Test;
    student:Student;
    grade:Int;
    gradeBase:Int;
}

form CourseHomeWorkForm entity CourseHomeWork;


form CourseHomeWorkListing "Course Home Work Listing"

{
    header {
        message(title), col 12;
    };
    courseHomeWorks    : CourseHomeWork, table, on_change saveCourseHomeWork, on_load loadCourseHomeWorks {
        "Id"              : id, internal, optional;
        "Course"          : course;
        "Year"            : year, mask decimal;
        "Given Date"      : givenDate;
        "Due Date"        : dueDate;
    };
    horizontal, style "margin-top-20" {
        button(add_row, courseHomeWorks), disable when forbidden(create), style "margin-right-5";
        button(remove_row, courseHomeWorks), disable when forbidden(delete), on_click removeCourseHomeWork;
    };
}


form TestForm "Test Form"
    entity Test
//    parameters year
{
    header {
        message(entity), col 8;
        search_box, col 4, style "pull-right";
    };
    "Id"        : id, internal, optional;
    "Course"    : course;
    "Year"      : year, mask decimal;
    "Date"      : date;
    "Test Data" : testData, table {
        "File Reference" : fileReference;
    };
    horizontal, style "margin-top-20" {
        button(add_row, testData), style "margin-right-5";
        button(remove_row, testData);
    };
    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form TestFormListing "Test Form Listing"

{
    header {
        message(title), col 12;
    };
    tests    : Test, table, on_change saveTest, on_load loadTests {
        "Id"        : id, internal, optional;
        "Course"    : course;
        "Year"      : year, mask decimal;
        "Date"      : date;
    };
    horizontal, style "margin-top-20" {
        button(add_row, tests), disable when forbidden(create), style "margin-right-5";
        button(remove_row, tests), disable when forbidden(delete), on_click removeTest;
    };
}


form CourseHomeWorkGradeForm entity HomeWorkGrade;
form TestGradeForm entity TestGrade;
form StudentGradingForm {

someSection: section {

        mysub: subform(CourseHomeWorkForm), inline;
        myText "Display It": String, display;
    };
}
//#ReportCard
form StudentReportForm{
formStudent: Student,on_change studentChanged;
homeworkAverage:Real,display , hide when formStudent==null;
homeworkGrades: HomeWorkGrade,  table, hide when formStudent==null {
    homeWork;
    homeWorkGrade;
    homeWorkGradeBase;
};
testAverage:Real,display, hide when formStudent==null;
testGrades: TestGrade, table , hide when formStudent==null{
    test;
    grade;
    gradeBase;
};
}
//#ReportCard

//link Google "Google" = "http://www.google.com";

//link Some = TestForm(year = 2005);

menu GradingMenu{
    CourseHomeWorkForm;
    CourseHomeWorkListing;
    TestForm;
    TestFormListing;
    CourseHomeWorkGradeForm;
    TestGradeForm;
    StudentReportForm;
}


