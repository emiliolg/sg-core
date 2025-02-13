## Resource

Resource type represents a binary object managed by Sui Generis. To store, attach, display images or other kind of resource. You can create a Resource object, load it from a file, giving it an Id. 
And afterwards you can recover it using the ResourceHandler.
ResourceHandler is responsible for the interaction with resources, its interface is specified as:
@inline-code(db/entity/src/main/java/tekgenesis/persistence/ResourceHandler.java)

You can see an example for this behavior in the [Mail Sender Example](../samples/mailReport.html#send-by-mail-to-all-guests)
For further details on Resource behavior check package tekgenesis.common.core in [Sui Generis API Documentation](../javadoc/index.html). 


###External vs internal resources
When a resource is uploaded, it can be uploaded either by specifying an url, or an input stream (as seen in the [Mail Sender Example](../samples/mailReport.html#send-by-mail-to-all-guests))
If the resource is uploaded using an input stream the content will be stored in the database. If, on the contrary, it is uploaded by specifying an URL, only the url will be stored in the database.

###Built in GC Task
Sui Generis provides a built in task to collect unreferenced resources.
To avoid data duplication, resource ids are used as an indirection to actual resource content, so that if the same image content is uploaded twice, the content will be actually stored once, and there will be two different ids for the resources referencing that image.
To Garbage collect the document conent, once it is no longer needed a built-in task is provided.
This task runs periodically and cleans un any unreferenced resources.
For further info on tasks see [Tasks](../tasks.html)

###Linking a resource externally
Resources can be referenced externally by their sha. The URL to access the content should be similar to: 
``` http://<HOST>/sg/resource?sha= + resourceAttribute.getMaster().getSha() ``` 
 

###Using resources in mustache templates
To use a resource from a mustache template, you can use the @resource tag. 

###Using resources as templates mails

###Widgets to upload a resource

Multiple widgets allow linkage to a Resource field. 
- [Image](../forms/widgets/widgets.html#image) widget for instance, allows referencing and displaying an image from a Resource.
- [Upload](../forms/widgets/widgets.html#upload) widget for uploading or changing a resource.

When no widget is specified, default widget for resource is Upload.
