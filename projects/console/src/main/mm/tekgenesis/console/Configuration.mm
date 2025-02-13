package tekgenesis.console;

enum LOG_LEVEL
{
    OFF;
    ERROR;
    WARN;
    INFO;
    DEBUG;
    TRACE;
    ALL;
}

form LoggerConfiguration
	on_load loadConfiguration
	primary_key currentNode
{
 	disable : Boolean, internal,  default true;
	currentNode:String, hide;
	logInfoAvailable : Boolean, internal, default false;
	noMapInfoAvailable : message(info),  is "There is not information available for logging configuration",  style "margin-bottom-10",  hide when logInfoAvailable;

	rootLoggerLevel "Root Log Level": LOG_LEVEL;
	logFileName "Log Filename": text_field;
	maxFileSize "Max File Size" : text_field;
	logDir "Log Directory": text_field;
	maxDays "Max Days": text_field;
	consoleOutput "Console Ouput": check_box;
	xmlOutput "Xml Output": check_box;
	fileOutput "File Output": check_box;
	debugAll "Debug All": check_box;
	debugTekgenesis "Debug Tekgenesis":check_box;
	contextName "Context Name": text_field;

	vGelf "Gelf": vertical
	{
		gelfServer "Server" : text_field;
		gelfServerPort "Port" : text_field;
		gelfFacility "Facility" : text_field;
		gelfOutput "Output": check_box;
	};

	loggers "Loggers": vertical {
		loggerSearch "Search": text_field(255), on_change filterLoggers;
		loggersTable : table(8)
		{
			loggerName "Name": String(128), display;
			loggerDirtyValue : Boolean, internal, default false;
			loggerLevel "Level" :LOG_LEVEL, on_change applyLogLevel;
		};
	};

	footer,  hide when disable {
		applyChange "Apply" : button, on_click applyLoggingChanges;
		cancelChanges "Cancel": button, on_click cancelLoggingChanges, content_style "margin-left-10";
	};
}

form ConfigurationForm "Configuration"
{
    disable : Boolean, internal, default true;

    configurationTab : tabs,on_change tabSelection {

        vTools "Http": vertical
        {
            horizontal , style " marginBottom15"{
                xNodeHeaderLabel "Add X-Id-Ref Header To Responses": label;
                horizontal,style xNodeHeader ? "marginLeft10  switch-container switch-container-green" : "marginLeft10  switch-container switch-container-gray"
                {
                    horizontal, style xNodeHeader ?  "switch switch-right" : "switch switch-left"  { };
                    xNodeHeader:toggle_button, style "hidden-but", on_ui_change applyXNodeHeader;
                    xNodeHeaderSwitch :display, is xNodeHeader ? "On" : "Off" ,style "switch-text";
                };
            };
        };
        vLogging "Logging": vertical
        {
            logInfoAvailable : Boolean,internal,default false;
            noMapInfoAvailable : message(info), is "There is not information available for logging configuration", style "margin-bottom-10", hide when logInfoAvailable;

            rootLoggerLevel "Root Log Level": LOG_LEVEL;
            logFileName "Log Filename": text_field;
            maxFileSize "Max File Size" : text_field;
            logDir "Log Directory": text_field;
            maxDays "Max Days": text_field;
            consoleOutput "Console Ouput": check_box;
            xmlOutput "Xml Output": check_box;
            fileOutput "File Output": check_box;
            debugAll "Debug All": check_box;
            debugTekgenesis "Debug Tekgenesis":check_box;
            contextName "Context Name": text_field;

            vGelf "Gelf": vertical
            {
                gelfServer "Server" : text_field;
                gelfServerPort "Port" : text_field;
                gelfFacility "Facility" : text_field;
                gelfOutput "Output": check_box;
            };

            loggers "Loggers": vertical {
                loogerSearch "Search": text_field(255),on_change filterLoggers;
                loggersTable : table(8)
                {
                    loggerName "Name": String(128),display;
                    loggerDirtyValue : Boolean,hide,default false;
                    loggerLevel "Level" :LOG_LEVEL,on_change applyLogLevel;
                };
            };

            footer, hide when disable {
                applyChange "Apply" : button,on_click applyLoggingChanges;
                cancelChanges "Cancel": button,on_click cancelLoggingChanges,content_style "margin-left-10";
            };
        };

        vProps "Properties": vertical{
            searchPropsBox "Search" : text_field(255),optional, on_change filterProperties;
            propertyTable : table(10),sortable {
                endpoint : String ,internal;
                clazz : String ,internal;
                attrName : String, internal;
                editProp : Boolean, internal;
                propName "Name": String,display;
                propValue "Value": text_field,disable when !editProp, on_change updatePropertyValue;
                editPropValue "" : button, icon edit, on_click  editPropertyValue;
            };
        };

        vOthers "MBeans": vertical
        {
            servicesTab : table (8),sortable,on_click selectMBean, style "table-hover"{
                serviceId: String,internal;
                domain "Domain": String,display;
                type "Type": String, display;
                name "Name": String, display;
            };

            selectedServiceId: String, internal;

            servTab : tabs {
                hAttrs "Attributes": vertical  {
                    loading:Boolean,internal;
                    isDirty: Boolean,internal;
                    beansAtts :table(10),row_style attRowStyle {
                        changed: Boolean,internal,default false;
                        isWritable: Boolean,internal;
                        attRowStyle :internal;
                        attName : display;
                        attValue: dynamic , disable when !isWritable, on_change markAttrsChanged;
                    };
                    applyAttrChanges "Apply" : button, on_click applyChanges, disable when !isDirty,hide when disable;
                };

                hOps "Operations": vertical  {
                    beansOps :table (10),style "table-hover",row_style opRowStyle {
                        cantArgs: Int,internal;
                        opRowStyle :internal;
                        signature: String,internal;
                        opName "Operation Name": display;
                        opArg1 : text_field(128),disable when cantArgs < 1,hide when cantArgs < 1;
                        opArg2 : text_field(128),disable when cantArgs < 2,hide when cantArgs < 2;
                        opArg3 : text_field(128),disable when cantArgs < 3,hide when cantArgs < 3;
                        opArg4 : text_field(128),disable when cantArgs < 4,hide when cantArgs < 4;
                        opArg5 : text_field(128),disable when cantArgs < 5,hide when cantArgs < 5;
                        opInvoke "Invoke": button ,on_click invokeOperation, hide when disable;
                    };
                };
            };

        };
    };
}