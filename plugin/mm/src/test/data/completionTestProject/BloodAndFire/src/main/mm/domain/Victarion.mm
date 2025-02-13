package domain;

entity Victarion "Victarion"
	described_by name
{
	name "Name" : String(20);
}
form VictarionForm entity Victarion;

<caret>

menu VictarionMenu
{
	VictarionForm;
}