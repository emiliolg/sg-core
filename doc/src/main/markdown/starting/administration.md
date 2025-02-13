# Administration

Sui Generis provides a Console built-in web application to monitor the general status for clusters and servers. Tasks, caches and indexes can be monitored and managed as well.

# Authentication and Authorization

Sui Generis uses shiro as authentication and authorization framework. This allows to use different sources for authentication, such as: LDAP, Facebook, built-in.

The authorization is handled using Sui Generis entities. The are three main entities: User, Role, Permission.

The roles are assigned to the users and have a list of permissions. Permissions are given for every application Form:

* *read*: Read an instance in read-only mode.
* *query*: Search for an instance using the default searchbox of a Form.
* *create*: Create a new instance.
* *update*: Update an instance.
* *delete*: Delete an instance.
* *handle_deprecated*: Deprecate/activate and search for deprecated instances.

Custom permissions can be defined in the .mm at Form level.

# SSL support

To enable https in your Sui Generis instance follow these steps:

```
export JAVA_OPTIONS=-Xbootclasspath/p:<Sui Generis installation dir>/lib/boot/alpn-boot-X.X.X.vXXXXXXXX.jar
 
suigeneris  start -https-port 8443 --key-store keystore:password:password
```

If you want to start it on default 443 port you probably need to run it as super user.

You need to have a keystore with a valid SSL certificate.

For testing purposes you can generate one with the following steps:

```
keytool -keystore keystore -alias jetty -genkey -keyalg RSA
```

#Sui Generis Properties

Sui Generis default properties file is "SuiGeneris.properties", it can also be overridden setting the property suigen.properties to the vm.
Multiple properties can be set on this file, for instance, the ones used to set [metrics](../metrics.html#configuration) or [mail](../mail.html#configuring-mails) or [task properties](../tasks.html).
All properties implement the tekgenesis.common.env.Properties interface, to see full listing, check  [Sui Generis API Documentation](../javadoc/index.html)
