#Reporting


## Custom Pdf reports.
Sui Generis offers a PDF reporting API.
Currently that API is based on @inline(externalLinks.md#apache_fop)

API Can be found in [tekgenesis.report.fo](javadoc/index.html) package.
The start point is FopBuilder class. This class is used as a start point.
This builder receives as argument a Document, and the document is built using SimpleDocumentBuilder
and following the structure found in apache documentation. 

**Sample Generation**
see [mail report sample](samples/mailReport.html)

