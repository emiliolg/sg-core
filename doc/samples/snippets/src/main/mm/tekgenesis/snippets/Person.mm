package tekgenesis.snippets;

//#address
entity Address {
    person: Person;
    street: String;
}
//#address
//#person
entity Person
	primary_key ssn
	described_by firstName, lastName
	searchable
{
	ssn "SSN #"        : Int(9);
	firstName "First name" : String(30);
	lastName "Last name"   : String(30);
	birthday "Birthday"   : Date;
	addresses          : Address*;
	sex                : Sex, default FEMALE;
}
//#person
//#personForm
form PersonForm "Person" : Person
{
	header { message(title); };

	horizontal {
		firstName;
		lastName;
	};
	ssn, display;
	birth "Date of birth" : birthday, date_picker;
	gender "Gender" : sex;

	footer {
		button(save);
		button(cancel);
		button(delete);
	};
}
//#personForm