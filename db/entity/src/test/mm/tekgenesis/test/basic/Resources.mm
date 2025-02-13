package tekgenesis.test.basic schema BasicTest;

entity ResA "Res A"
	described_by name
{
	name "Name" : String(20);
	res  "Resource" : Resource, optional;
}


entity ResB "Res B"
	described_by name
{
	name "Name" : String(20);
	res1  "Resource 1" : Resource, optional;
	res2  "Resource 2" : Resource, optional;
}