package tekgenesis.showcase;

handler HtmlHandler
{
	"/showcase/main" : Html, main;
	"/showcase/template" : Html, template;
}

handler AppHtmlHandler secure_by applicationtoken
{
    "/showcase/main/secure" : Html, main;
	"/showcase/template/secure" : Html, template;
}

handler UnrestrictedHtmlHandler unrestricted
{
    "/showcase/main/unrestricted" : Html, main;
	"/showcase/template/unrestricted" : Html, template;
}