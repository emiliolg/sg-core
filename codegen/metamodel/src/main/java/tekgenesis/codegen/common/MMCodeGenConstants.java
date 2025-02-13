
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.common;

import org.jetbrains.annotations.NonNls;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.QName;

import static tekgenesis.common.core.Strings.pluralize;
import static tekgenesis.common.core.Strings.quoted;

/**
 * Constants and Utils to generate the MetaModel Code.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public interface MMCodeGenConstants {

    //~ Instance Fields ..............................................................................................................................

    @NonNls String ACTIONS_CLASS = "tekgenesis.form.Actions";
    @NonNls String ACTIONS_FIELD = "actions";

    @NonNls String ADD_LISTENER = "addListener";
    String         ALIAS        = "alias";

    String         INDEX_METHOD        = "index";
    @NonNls String PERSISTENCE_PACKAGE = "tekgenesis.persistence.";
    @NonNls String AUDITABLE_INSTANCE  = PERSISTENCE_PACKAGE + "AuditableInstance";
    String         BUILD_METHOD        = "build";
    @NonNls String BUILDER             = "builder";

    @NonNls String CALL_CLASS    = "tekgenesis.common.service.Call";
    @NonNls String CASE_INSTANCE = "tekgenesis.workflow.CaseInstance";
    /** The method name to invoke when the user submits the form. */
    @NonNls String COPY_TO_METHOD_NAME     = "copyTo";
    @NonNls String CREATE_ENTITY_SEQ       = "createEntitySeq";
    @NonNls String CREATE_INNER_ENTITY_SEQ = "createInnerEntitySeq";

    @NonNls String DATA_CLASS_NAME = "Data";
    @NonNls String DATA_METHOD     = "data";
    @NonNls String DB_TABLE        = PERSISTENCE_PACKAGE + "DbTable";

    @NonNls String DEPRECABLE_INSTANCE  = PERSISTENCE_PACKAGE + "DeprecableInstance";
    @NonNls String DEPRECABLE_VAR       = "deprecable";
    @NonNls String DEPRECATE_METHOD     = "deprecate";
    @NonNls String DOC_EXT              = ".html";
    @NonNls String ENTITY_INSTANCE      = PERSISTENCE_PACKAGE + "EntityInstance";
    @NonNls String ENTITY_LISTENER      = PERSISTENCE_PACKAGE + "EntityListener";
    @NonNls String ENTITY_LISTENER_TYPE = PERSISTENCE_PACKAGE + "EntityListenerType";
    @NonNls String ENTITY_REF           = PERSISTENCE_PACKAGE + "EntityRef";
    @NonNls String ENTITY_SEQ           = PERSISTENCE_PACKAGE + "EntitySeq";

    @NonNls String ENTITY_TABLE       = PERSISTENCE_PACKAGE + "EntityTable";
    @NonNls String ENTITY_TABLE_INNER = PERSISTENCE_PACKAGE + "InnerEntityTable";
    @NonNls String ESCAPE_CHAR        = "tekgenesis.common.core.Strings.escapeCharOn";
    @NonNls String FACTORY_CLASS      = "tekgenesis.service.Factory";
    @NonNls String FEEDBACK           = "feedback";
    @NonNls String FIELDS_ENUM        = "Field";
    @NonNls String FOR_EACH           = "forEach";

    @NonNls String FOR_UPDATE_SUFFIX  = "ForUpdate";
    @NonNls String FORM_ACTION        = "tekgenesis.form.Action";
    @NonNls String FORM_INSTANCE      = "tekgenesis.form.FormInstance";
    @NonNls String FORM_LISTENER      = "tekgenesis.form.extension.FormListener";
    @NonNls String FORM_LISTENER_TYPE = "tekgenesis.form.extension.FormListenerType";
    @NonNls String FORM_MESSAGE       = "tekgenesis.form.Message";
    @NonNls String FORM_ROW_INSTANCE  = "tekgenesis.form.FormRowInstance";
    @NonNls String FORM_TABLE         = "tekgenesis.form.FormTable";
    @NonNls String GET_CURRENT        = "getCurrent";
    @NonNls String GET_NAME           = "getName";
    @NonNls String HANDLER_INSTANCE   = "tekgenesis.form.HandlerInstance";
    @NonNls String HAS_IMAGE_INSTANCE = PERSISTENCE_PACKAGE + "HasImage";

    @NonNls String HTML_BUILDER_CLASS     = "tekgenesis.service.html.HtmlBuilder";
    @NonNls String HTML_FACTORY_CLASS     = "tekgenesis.service.html.HtmlFactory";
    @NonNls String HTML_METHOD            = "html";
    @NonNls String IMMUTABLE_LIST         = "tekgenesis.common.collections.ImmutableList.of";
    @NonNls String IMPORTER               = PERSISTENCE_PACKAGE + "etl.Importer";
    @NonNls String IMPORTER_TASK_INSTANCE = "tekgenesis.task.ImporterTaskInstance";
    @NonNls String INITIALIZE_ANNOTATION  = PERSISTENCE_PACKAGE + "Initialize";
    @NonNls String INNER_ENTITY_SEQ       = PERSISTENCE_PACKAGE + "InnerEntitySeq";

    @NonNls String INNER_INSTANCE          = PERSISTENCE_PACKAGE + "InnerInstance";
    @NonNls String IS_DEFINED              = "isDefined";
    @NonNls String IS_PRESENT              = "isPresent";
    @NonNls String JADE_METHOD             = "jade";
    @NonNls String JOIN_IMPORT             = "tekgenesis.common.core.Strings.join";
    String         LABEL                   = "label";
    @NonNls String LIFECYCLE_TASK_INSTANCE = "tekgenesis.task.LifecycleTaskInstance";
    @NonNls String LISTENER                = "listener";
    @NonNls String LISTENER_TYPE           = "listenerType";
    @NonNls String MERGE_METHOD            = "merge";
    @NonNls String METAMODEL               = "metamodel";
    @NonNls String MSG                     = "msg";
    @NonNls String MUSTACHE_METHOD         = "mustache";

    //J-
    String BASE = "Base";
    String REMOTE = "Remote";
    String WORK_ITEM = "WorkItem";



    String F_VAR = "f";
    String LISTENERS_VAR = "listeners";
    String PARAM_VAR = "parameters";
    String ROW_VAR = "row";
    String ROW_CLASS_SUFFIX = "Row";
    String ROW_BASE_CLASS_SUFFIX = "RowBase";

    String FIELD = "field";
    String FIELDS = "fields";
    String FILTERS = "Filters";

    String INSTANCE = "instance";
    String NAVIGATION = "navigation";
    String NAVIGATE = "navigate";
    String PK = "pk";
    String ITEM = "item";
    String TASK = "task";
    String ITEMS = pluralize(ITEM);
    String COMPARATOR = "comparator";

    String CREATION = "creation";

    String CLOSED = "closed";

    String BUSINESS_KEY = "businessKey";

    String ASSIGNEE = "assignee";
    String AS_STRING = "asString";

    String REPORTER = "reporter";

    String ORG_UNIT_NAME = "ouName";

    String ORG_UNIT = "orgUnit";

    String ORGANIZATIONAL_UNIT = "organizationalUnit";


    String USER = "user";
    String ASSIGNEES = "assignees";
    String ASSIGNMENT = "assignment";
    String LIST_BY_ASSIGNEES = "listByAssignees";
    String PARENT_CASE = "parentCase";
    String PRIMARY_KEY_METHOD = "primaryKey";


    @NonNls String VALUES_METHOD = "values";

    @NonNls String FLUSH_METHOD = "flush";

    /**
     * Methods of EntityInstance
     */
    String DELETE_METHOD = "delete";
    String INSERT_METHOD = Constants.INSERT;
    String INSERT_DO_NOT_GENERATE = "insertDoNotGenerate";
    String KEY_AS_STRING_METHOD = "keyAsString";
    String KEY_OBJECT_METHOD = "keyObject";
    String PERSIST_METHOD = "persist";
    String UPDATE_METHOD = Constants.UPDATE;
    String TABLE = "table";
    String IMAGE_PATH_METHOD = "imagePath";

    /**
     * Static Methods of EntityInstance
     */
    String FIND_METHOD = "find";
    String FIND_OR_FAIL_METHOD = "findOrFail";
    String FIND_PERSISTED_METHOD = "findPersisted";
    String FIND_PERSISTED_OR_FAIL_METHOD = "findPersistedOrFail";
    String CRITERIA_FOR_METHOD = "criteriaFor";
    String SEARCH_RESULT = "tekgenesis.index.SearchResult";
    String SEARCH_METHOD = "search";
    String FIND_OR_CREATE = "findOrCreate";
    String FIND_BY_STRING_METHOD = "findByString";
    String F_CREATE_BY_STRING_METHOD = "findOrCreateByString";
    String STR_TO_KEY = "strToKey";
    String SET_PRIMARY_KEY = "setPrimaryKey";
    String FIND_WHERE_METHOD = "findWhere";
    String LIST_WHERE_METHOD = "listWhere";
    String LIST_METHOD = "list";
    String LIST_FROM_STRINGS_METHOD = "listFromStringKeys";

    String QUERY_METHOD = "query";
    String VALUE_METHOD = "value";
    String TEXT = "text";


    /**
     * Deprecated static methods of EntityInstance
     */
    String LIST_BY_METHOD = "listBy";
    String FIND_BY_METHOD = "findBy";


    String ROW_MAPPER_METHOD = "rowMapper";
    String ROW_MAPPER_CLASS = "tekgenesis.database.RowMapper";

    // SearchableDSL Fields

    String SEARCHABLE_FIELD = "tekgenesis.index.SearchableField.";

    @NonNls String SEARCHABLE_INT_FIELD = SEARCHABLE_FIELD + "Int";
    @NonNls String SEARCHABLE_LONG_FIELD = SEARCHABLE_FIELD + "LongFld";
    @NonNls String SEARCHABLE_REAL_FIELD = SEARCHABLE_FIELD + "Real";

    @NonNls String SEARCHABLE_DECIMAL_FIELD = SEARCHABLE_FIELD + "Decimal";
    @NonNls String SEARCHABLE_STRING_FIELD = SEARCHABLE_FIELD + "Str";
    @NonNls String SEARCHABLE_BOOLEAN_FIELD = SEARCHABLE_FIELD + "Bool";
    @NonNls String SEARCHABLE_ENTITY_FIELD = SEARCHABLE_FIELD + "Ent";
    @NonNls String SEARCHABLE_DATE_FIELD = SEARCHABLE_FIELD + "Date";
    @NonNls String SEARCHABLE_ENUM_FIELD = SEARCHABLE_FIELD + "Enum";
    @NonNls String SEARCHABLE_MANY_FIELD = SEARCHABLE_FIELD + "Many";
    @NonNls String SEARCHABLE_MANY_ENT_FIELD = SEARCHABLE_FIELD + "ManyEnt";

    @NonNls String SEARCHABLE_DATE_TIME_FIELD = SEARCHABLE_FIELD + "DTime";

    //Field methods

    @NonNls String ENTITY_METHOD = "entityField";
    @NonNls String LONG_METHOD = "longField";
    @NonNls String DATE_METHOD = "dateField";
    @NonNls String BOOL_METHOD = "boolField";
    @NonNls String STR_METHOD = "strField";
    @NonNls String REAL_METHOD = "realField";
    @NonNls String DECIMAL_METHOD = "decimalField";
    @NonNls String INT_METHOD = "intField";
    @NonNls String ENUM_METHOD = "enumField";

    // QueryFields

    String TABLE_FIELD = PERSISTENCE_PACKAGE + "TableField.";
    @NonNls String INT_FIELD = TABLE_FIELD + "Int";
    @NonNls String LONG_FIELD = TABLE_FIELD + "LongFld";
    @NonNls String REAL_FIELD = TABLE_FIELD + "Real";

    @NonNls String DECIMAL_FIELD = TABLE_FIELD + "Decimal";
    @NonNls String STRING_FIELD = TABLE_FIELD + "Str";
    @NonNls String CLOB_FIELD = TABLE_FIELD + "Clob";
    @NonNls String BOOLEAN_FIELD = TABLE_FIELD + "Bool";
    @NonNls String DATE_FIELD = TABLE_FIELD + "Date";

    @NonNls String DATE_TIME_FIELD = TABLE_FIELD + "DTime";
    @NonNls String ENUMERATION_FIELD = TABLE_FIELD + "Enum";
    @NonNls String ENUMERATION_SET_FIELD = TABLE_FIELD + "EnumerationSet";
    @NonNls String RESOURCE_FIELD = TABLE_FIELD + "Res";
    @NonNls String RESOURCE = "resource";
    @NonNls String PATH = "path";
    @NonNls String CALL = "call";
    @NonNls String PARAM_METHOD = "param";

    String FILTER_FIELD = "tekgenesis.form.filter.Filter.";
    @NonNls String INT_FILTER = FILTER_FIELD + "IntegerFilter";
    @NonNls String REAL_FILTER = FILTER_FIELD + "RealFilter";
    @NonNls String DECIMAL_FILTER = FILTER_FIELD + "DecimalFilter";
    @NonNls String STRING_FILTER = FILTER_FIELD + "StringFilter";
    @NonNls String BOOLEAN_FILTER = FILTER_FIELD + "BooleanFilter";
    @NonNls String DATE_FILTER = FILTER_FIELD + "DateFilter";
    @NonNls String DATE_TIME_FILTER = FILTER_FIELD + "DateTimeFilter";
    @NonNls String ENUMERATION_FILTER = FILTER_FIELD + "EnumFilter";
    @NonNls String ENTITY_FILTER = FILTER_FIELD + "EntityFilter";
    @NonNls String FILTER_OPTIONS = "tekgenesis.form.filter.Filter.CustomOptions";

    @NonNls String FACTORY = "factory";
    @NonNls String RESULTS = "results";
    @NonNls String INVOKER = "invoker";
    @NonNls String LOCALE = "locale";
    @NonNls String OF_NULLABLE = "ofNullable";
    @NonNls String EXCEPTION = "exception";
    @NonNls String ERROR_ARG = "error";
    @NonNls String ARGS = "args";
    @NonNls String MODULE = "module";
    @NonNls String SERVICES = "services";
    @NonNls String ANGULAR = "angular";

    @NonNls @SuppressWarnings("DuplicateStringLiteralInspection") String CONFIG = "config";
    @NonNls String OBJECT_ARRAY = "Object...";

    @NonNls String TITLE = "title";

    @NonNls String PRIORITY_CODE = "priorityCode";
    @NonNls String PRIORITY = "priority";

    @NonNls String OPTIONS = "tekgenesis.form.filter.Options";


    @NonNls String OPTIONS_ARGS = "options";

    /**
     * Arguments.
     */
    @NonNls String KEY_ARG = "key";
    @NonNls String CRITERIA_ARG = "condition";
    @NonNls String CRITERIA_CLASS = PERSISTENCE_PACKAGE + "Criteria";

    @NonNls String WEAKER_ACCESS = quoted("WeakerAccess");
    @NonNls String JAVA_DOC = quoted("JavaDoc");

    @NonNls String DUPLICATED_STRING = quoted("DuplicateStringLiteralInspection");

    // DO NOT ADD EVERYTHING HERE, should be kept as small as possible
    // (try to generate clean code, or suppress the warning for the specific method/field)

    String MAGIC_NUMBER = quoted("MagicNumber");
    String FIELD_MAY_BE_FINAL = quoted("FieldMayBeFinal");
    String ASSIGNMENT_TO_NULL = quoted("AssignmentToNull");
    @NonNls String[] COMMON_SUPPRESSED_WARNINGS = {
            DUPLICATED_STRING,
            WEAKER_ACCESS,
            FIELD_MAY_BE_FINAL,
            quoted("FieldCanBeLocal"),
            quoted("ConstantConditions"),
            quoted("ClassWithTooManyMethods"),
            quoted("ClassWithTooManyFields"),
            MAGIC_NUMBER,
            quoted("MethodOverridesStaticMethodOfSuperclass"),
            quoted("ParameterHidesMemberVariable"),
            quoted("FieldNameHidesFieldInSuperclass"),
            quoted("OverlyComplexClass"),
            quoted("RedundantCast"),
            quoted("UnusedReturnValue")
    };

    @NonNls String NULLABLE_PROBLEMS = quoted("NullableProblems");

    @NonNls String UTILITY_CLASS_WITHOUT_CONSTRUCTOR = quoted("UtilityClassWithoutPrivateConstructor");
    @NonNls String UNUSED_PARAMETERS = quoted("UnusedParameters");
    @NonNls String EMPTY_METHOD_WARNING = quoted("EmptyMethod");
    @NonNls String LOCAL_VARIABLE_HIDES_MEMBER_VARIABLE = quoted("LocalVariableHidesMemberVariable");

    @NonNls String UNUSED_DECLARATION = quoted("UnusedDeclaration");
    @NonNls String RAW_TYPES = quoted("rawtypes");
    @NonNls String INSTANCE_VARIABLE_MAY_NOT_BE_INITIALIZED = quoted("InstanceVariableMayNotBeInitialized");

    //J+
    @NonNls String NOTIFIER_CLASS            = "tekgenesis.notification.push.workflow.WorkItemNotifier";
    @NonNls String OPEN_DATA_CLASS_NAME      = "OpenData";
    @NonNls String OPTIONAL_WIDGET           = "tekgenesis.form.OptionalWidget";
    @NonNls String ORGANIZATIONAL_UNIT_CLASS = "tekgenesis.metadata.authorization.OrganizationalUnit";
    @NonNls String OUTPUT_DIR                = "outputDir";
    @NonNls String PERMISSION                = "Permission";
    @NonNls String PERSISTABLE_INSTANCE      = PERSISTENCE_PACKAGE + "PersistableInstance";
    @NonNls String PROCESSOR_TASK_INSTANCE   = "tekgenesis.task.ProcessorTaskInstance";

    @NonNls String REMOVE_LISTENER         = "removeListener";
    @NonNls String REMOVE_METHOD           = "remove";
    @NonNls String RESULT_CLASS            = "tekgenesis.service.Result";
    @NonNls String RESULTS_CLASS           = "tekgenesis.service.Results";
    @NonNls String ROLE_ASSIGNMENT_CLASS   = "tekgenesis.metadata.authorization.RoleAssignment";
    @NonNls String ROUTES                  = "Routes";
    @NonNls String SCHEDULED_TASK_INSTANCE = "tekgenesis.task.ScheduledTaskInstance";

    @NonNls String SEARCHER_SUFFIX          = "Searcher";
    @NonNls String SELECT                   = PERSISTENCE_PACKAGE + "Select";
    @NonNls QName  SELECT_FROM_METHOD       = QName.createQName(PERSISTENCE_PACKAGE + "Sql.selectFrom");
    @NonNls String SERVICE_HANDLER_INSTANCE = "tekgenesis.service.HandlerInstance";

    @NonNls String SPLIT_ARRAY_IMPORT    = "tekgenesis.common.core.Strings.splitToArray";
    String         STATUS_ARG            = "status";
    @NonNls String STRING_METHOD         = "str";
    @NonNls String STRUCT_METHOD         = "struct";
    @NonNls String SUGGESTION_CLASS      = "tekgenesis.form.Suggestion";
    @NonNls String TABLE_BASE_CLASS_NAME = "TableBase";
    @NonNls String TABLE_CLASS_NAME      = "Table";
    @NonNls String TABLE_FACTORY         = PERSISTENCE_PACKAGE + "TableFactory";

    @NonNls String UI_MODEL_ACCESSOR  = "tekgenesis.form.UiModelAccessor";
    @NonNls String UPDATABLE_INSTANCE = PERSISTENCE_PACKAGE + "UpdatableInstance";

    @NonNls String USER_CLASS = "tekgenesis.metadata.authorization.User";

    @NonNls String WHERE                = "where";
    @NonNls String WIDGET_CONFIGURATION = "tekgenesis.form.configuration.WidgetConfiguration";
    @NonNls String WIDGET_INSTANCE      = "tekgenesis.form.WidgetInstance";
    @NonNls String WORK_ITEM_INSTANCE   = "tekgenesis.workflow.WorkItemInstance";
    @NonNls String WORK_ITEM_PRIORITY   = "tekgenesis.workflow.WorkItemPriority";
    @NonNls String WORK_ITEM_TABLE      = PERSISTENCE_PACKAGE + "WorkItemTable";
}  // end class MMCodeGenConstants
