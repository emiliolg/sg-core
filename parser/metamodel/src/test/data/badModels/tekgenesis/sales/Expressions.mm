
package tekgenesis.sales.basic;

form UnresolverReference "UnresolverReference"
{
	a : Int, default notFound+2;
}

form IncompatibleRefType "IncompatibleRefType"
{
	date : Date;
	a : Int, default date;
}

form IncompatibleConstantType "IncompatibleConstantType"
{
	a : Int, default "2";
	b : Int, default "2d";
}

form InvalidExpression "InvalidExpression"
{
	a : Int, default 2+1;
	b : Int, default (2+) + 1;
}

form InvalidExpressionB "InvalidExpressionB"
{
	a : Int, default 2+1;
	b : Int, default (+2) + 1;
}

form InvalidParentheses "InvalidParentheses"
{
	a : Int, default (+2 + 1;
}

form RefCycle "RefCycle"
{
	a : Int, default b;
	b : Int, default a;
}

entity CheckedEntity "Checked Entity"
    primary_key a
    described_by a
{
	a "Id" : Int, check a > 0 : "Must be possitive";
	b "Id2" : Int, check (a > 0 : "Must be possitive", b > 0 : "Must be possitive");
}

form InherithA "Checked Form Valid"
    entity CheckedEntity
{
	aInForm "Id in Form" : a;
}

form InherithB "Checked Form Invalid"
    entity CheckedEntity
{
	bInForm "Id in Form" : b;
}

form Forbidden "InvalidPermission"
	permissions save
{
	a : Int, disable when forbidden(save);
	b : Int, disable when forbidden(create);
	c : Int, disable when forbidden(print);
}
