#School 

This project will serve as a guide through the most used features of the Framework.

We start by defining the need for a school student followup system.

## Entities
We start by defining the model entities for our system.
In particular in this case, we can start with the Student, and the courses.
@TODOinline(school.mm#Student)
@TODOinline(school.mm#Course)

In both cases, we start with simple entities, in the case of the students, we have a clear primary key, 
in case of the course, we just let the key be automatically generated.

##Basic Forms for Create and Update
We add for both of these entities basic default forms.
So Both entites can be created and modified.
@TODOinline(school.mm#StudentForm)
@TODOinline(school.mm#CourseForm)

To be able to visualize both, we add a School Menu, with both forms.
@TODOinline(school.mm#School)


We also add a Family entity, since it is important to know if several kids belong to the same family.

Now we start adding the HomeWork and Tests.


##Mocking Data
To have initial test data, we add a lifecycle task MockPopulateTask... 
@TODOinline(mock.mm#MockPopulateTask) 

##Listing Forms
We also add the first listing forms so we could see the created data...
@TODOinline(grading.mm#CourseListing)

Now lets start with some use cases. For instance, a report for each student. We se the student report in the following form:
@TODOinline(mock.mm#ReportCard)
This form shows all grades and average for each homework and test for the selected student. You can see that only the student selection field is visible at first. So that a student can be selected.
Also you can see that when the field changes the studentChanged method is invoked:
@TODOinline(StuentForm.java#Action studentChanged)
the method loads both homework and test grades onto table. For more details open the School sample project.
 

For instance, we should be able to see for each grade a complete student listing.. 



 

