
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.i18n;

import org.jetbrains.annotations.Nullable;

import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.common.env.i18n.I18nMessages;
import tekgenesis.common.env.i18n.I18nMessagesFactory;

/**
 * Plugin messages.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public interface PluginMessages extends I18nMessages {

    //~ Instance Fields ..............................................................................................................................

    PluginMessages MSGS = I18nMessagesFactory.create(PluginMessages.class);

    //~ Methods ......................................................................................................................................

    /**  */
    @DefaultMessage("Action Name")
    String actionName();

    /**  */
    @DefaultMessage("Actions")
    String actions();

    /** Annotation telling you to add a missing a method in a specific class. */
    @DefaultMessage("Add method {0} to Class {1}")
    String addMethodTo(String method, String clazz);

    /** Add form or menu to menu annotation message. */
    @DefaultMessage("Add to menu")
    String addToMenuAnnotation();

    /**  */
    @DefaultMessage("Program Arguments: ")
    String argumentsMessage();

    /** Bad chronological expression. */
    @DefaultMessage("Bad chronological expression")
    String badChronologicalExpression();

    /** Buttons for table annotation message. */
    @DefaultMessage("Generate buttons for table")
    String buttonsForTableAnnotation();

    /**  */
    @DefaultMessage("Cannot resolve field ")
    String cannotResolveField();

    /**  */
    @DefaultMessage("Cannot resolve import")
    String cannotResolveImport();

    /** Chronological expression. */
    @I18nMessages.DefaultMessage("Chronological expression")
    String chronologicalExpression();

    /**  */
    @DefaultMessage("Create new Menu")
    String createMenuAnnotation();

    /**
     * Message to create a missing inner class, most of all used in the case of new multiples added
     * to the Meta Model File.
     */
    @DefaultMessage("Create missing inner class {0} on {1}")
    String createMissingInnerClass(String expecting, @Nullable String found);

    /**  */
    @DefaultMessage("Data: ")
    String dataMessage();

    /** Default form for entity annotation message. */
    @DefaultMessage("Generate Default Form")
    String defaultFormForEntityAnnotation();

    /** Default listing for entity annotation message. */
    @DefaultMessage("Generate Default Listing")
    String defaultListingForEntityAnnotation();

    /** Default widget for model annotation message. */
    @DefaultMessage("Generate Default Widget")
    String defaultWidgetForModelAnnotation();

    /**  */
    @DefaultMessage("Description")
    @SuppressWarnings("DuplicateStringLiteralInspection")
    String description();

    /**  */
    @DefaultMessage("Field Name")
    String fieldName();

    /**  */
    @DefaultMessage("Field Option node, has no option defined!")
    String fieldOptionNodeHasNoOption();

    /**  */
    @DefaultMessage("Fields")
    String fields();

    /**  */
    @DefaultMessage("Metamodels")
    String fixFamily();

    /** MM Graph for entity annotation message. */
    @DefaultMessage("Show graph for Entity")
    String graphForEntityAnnotation();

    /** Hint to implement missing methods. */
    @DefaultMessage("Implement missing methods")
    String implementMissingMethods();

    /**  */
    @DefaultMessage("Import MetaModel")
    String importMetaModel();

    /** Invalid port number message. */
    @DefaultMessage("Invalid Port number")
    String invalidPortNumber();

    /**  */
    @DefaultMessage("Invalid SDK defined, a Sui Generis SDK must be defined")
    String invalidSuiGenesisSdk();

    /**  */
    @DefaultMessage(
                    "A Sui Generis SDK is required for code generation, compiling, debugging, " +
                    "and running applications, as well as for the standard Sui Generis SDK classes resolution."
                   )
    String invalidSuiGenesisSdkDefinedDetails();

    /**  */
    @DefaultMessage("The following permissions must be defined to perform the following operations:")
    String kindPermissions();

    /** Remove label message. */
    @DefaultMessage("{0} will be ignored")
    String labelIgnored(String label);

    /**  */
    @DefaultMessage("Launch Browser:")
    String launchBrowserMessage();

    /** Localize meta model annotation message. */
    @DefaultMessage("Localize Meta Model")
    String localizeMetaModelAnnotation();

    /** Error message outside of scope. */
    @DefaultMessage("Method reference outside from scope")
    String methodOutsideFromScope();

    /** Missing cache data class. */
    @DefaultMessage("Data class is not defined")
    String missingDataClass();

    /** Missing method annotation message. */
    @DefaultMessage("Missing method {0} in class {1}")
    String missingMethodAnnotation(String method, String clazz);

    /**  */
    @DefaultMessage("Missing package")
    String missingPackage();

    /** Missing table annotation message. */
    @DefaultMessage("Missing table {0} in class {1}")
    String missingTableAnnotation(String table, String clazz);

    /** Sui Generis module to run message. */
    @DefaultMessage("Sui Generis module to run:")
    String moduleToRunMessage();

    /**  */
    @DefaultMessage("No arguments defined for type")
    String noArgumentsDefinedForType();

    /** No mm directory marked as source message. */
    @DefaultMessage("No mm directory marked as source on module '{0}'")
    String noMMDirectoryMarkedAsSource(String module);

    /**  */
    @DefaultMessage("No module found")
    String noModuleFound();

    /**  */
    @DefaultMessage("No module specified for configuration")
    String noModuleSpecified();

    /** No Sui Generis module specified for configuration message. */
    @DefaultMessage("No Sui Generis module specified for configuration")
    String noSuiGenerisModuleSpecified();

    /** No Sui Generis SDK configured for module message. */
    @DefaultMessage("No Sui Generis SDK configured for module {0}")
    String noSuiGenerisSdkConfigured(String module);

    /**  */
    @DefaultMessage("Operation")
    String operation();

    /**  */
    @DefaultMessage("Overview")
    String overview();

    /**  */
    @DefaultMessage("Package name {0} does not correspond to the file {1}")
    String packageNameNotCorrespondToPath(String domain, String path);

    /**  */
    @DefaultMessage("Parsed Field Option node with invalid option Id")
    String parsedFieldOptionNodeInvalidId();

    /**  */
    @DefaultMessage(MMCodeGenConstants.PERMISSION)
    String permission();

    /**  */
    @DefaultMessage("Permissions")
    String permissions();

    /**  */
    @DefaultMessage("Properties file: ")
    String propertiesFileMessage();

    /** Remove label message. */
    @DefaultMessage("Remove Label")
    String removeLabel();

    /** Replace qualified name with 'import'. */
    @DefaultMessage("Replace qualified name with 'import'")
    String replaceQualifiedNameWithImport();

    /**  */
    @DefaultMessage("Required")
    String required();

    /**  */
    @DefaultMessage("Resolve reference")
    String resolveReference();

    /**  */
    @DefaultMessage("Sanitize Sui Generis SDK")
    String sanitizeSuiGenesisSdk();

    /**  */
    @DefaultMessage("Setup Sui Generis SDK")
    String setupSuiGenesisSdk();
    /** Show future schedules. */
    @I18nMessages.DefaultMessage("Show future schedules")
    String showFutureSchedules();
    /** To use on the scheduleIntentionForm. */
    @I18nMessages.DefaultMessage("Show less dates")
    String showLessDates();
    /** To use on the scheduleIntentionForm. */
    @I18nMessages.DefaultMessage("Show more dates")
    String showMoreDates();

    /**  */
    @DefaultMessage("Sui Generis SDK is missing required libraries")
    String suiGenesisSdkMissingLibraries();

    /**  */
    @DefaultMessage("Sui Generis SDK changed its libraries dependencies and some required libraries are missing")
    String suiGenesisSdkMissingLibrariesDetails();

    /**  */
    @DefaultMessage("Sui Generis SDK is not defined")
    String suiGenesisSdkNotDefined();

    /**  */
    @DefaultMessage("Sui Generis SDK is now update to #{0} ({1} libraries added)")
    String suiGenesisSdkSanitized(String build, Integer librariesAdded);

    /** Error message for tables outside of scope. */
    @DefaultMessage("Table reference outside Form scope")
    String tableOutsideFromScope();

    /**  */
    @DefaultMessage("VM Options: ")
    String vmOptionsMessage();

    /**  */
    @DefaultMessage("Widget not on ui context")
    String widgetNotOnUiContext();

    /** Message of a wrong method declaration. */
    @DefaultMessage("Wrong method declaration. Expecting {0} found {1}")
    String wrongMethodDeclaration(String expecting, String found);

    /** Wrong default encoding for properties files. */
    @DefaultMessage("Wrong encoding for properties files. Should be {0}")
    String wrongPropertiesEncoding(String encoding);

    /** Wrong default encoding for properties files details. */
    @DefaultMessage("Encoding in properties files should be {0} by default. You have it set differently, this will set it right")
    String wrongPropertiesEncodingDetails(String encoding);

    /** Wrong SDK configured for module message. */
    @DefaultMessage("Wrong SDK configured for module {0}. Expected Sui Generis SDK.")
    String wrongSdkConfiguredForModule(String module);

    /** Wrong default encoding for properties files details. */
    @DefaultMessage("Set default encoding to {0}")
    String setCorrectDefaultEncoding(String encoding);

    /**  */
    @DefaultMessage("Set Package name to {0}")
    String setPackageName(String path);
}  // end interface PluginMessages
