
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import tekgenesis.common.env.i18n.I18nMessages;
import tekgenesis.common.env.i18n.I18nMessagesFactory;

/**
 * Metadata Form Messages class.
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public interface MetadataFormMessages extends I18nMessages {

    //~ Instance Fields ..............................................................................................................................

    MetadataFormMessages MSGS = I18nMessagesFactory.create(MetadataFormMessages.class);

    //~ Methods ......................................................................................................................................

    /**  */
    @DefaultMessage("Apr")
    String april();

    /**  */
    @DefaultMessage("Aug")
    String august();

    /**  */
    @DefaultMessage("Cant convert value {0} of type {1}. Expecting {2}.")
    String cantConvert(String value, String type, String fromType);

    /**  */
    @DefaultMessage("This entity ''{0}'' instance was changed while you were updating.")
    String changedEntity(String className);

    /**  */
    @DefaultMessage("Value is already taken in column.")
    String columnValueTaken();

    /**  */
    @DefaultMessage("Dec")
    String december();

    /**  */
    @DefaultMessage("Date is not valid.")
    String disabledDate();

    /**  */
    @DefaultMessage("Feb")
    String february();

    /**  */
    @DefaultMessage("{0} doesnt allowed associated primary key.")
    String formCannotBePopulated(String formName);

    /**  */
    @DefaultMessage("Invalid range value ")
    String invalidRangeValue();

    /**  */
    @DefaultMessage("Invalid range values: 'to' value must be greater than 'from' ")
    String invalidRangeValues();

    /**  */
    @DefaultMessage("Invalid value {0}, it is not in ''{1}'' options")
    String invalidValueForOptionWidget(String value, String widget);

    /**  */
    @DefaultMessage("Jan")
    String january();

    /**  */
    @DefaultMessage("Jul")
    String july();

    /**  */
    @DefaultMessage("Jun")
    String june();

    /**  */
    @DefaultMessage("Lower than expected password strength.")
    String lowerPasswordStrength();

    /**  */
    @DefaultMessage("Mar")
    String march();

    /**  */
    @DefaultMessage("May")
    String may();

    /**  */
    @DefaultMessage("{0} not found.")
    String notFound(String name);

    /**  */
    @DefaultMessage("{0} with id {1} not found.")
    String notFoundWithPk(String name, String pk);

    /**  */
    @DefaultMessage("Nov")
    String november();

    /**  */
    @DefaultMessage("Oct")
    String october();

    /**  */
    @DefaultMessage("Required value.")
    String requiredValue();

    /**  */
    @DefaultMessage("Sep")
    String september();

    /**  */
    @DefaultMessage("Oooops! Something went wrong")
    String somethingWentWrong();

    /**  */
    @DefaultMessage("Value length {0} exceeds its limit of {1} ")
    String valueExceedsLimit(Integer actualLength, Integer requiredLength);

    /**  */
    @DefaultMessage("Value length ({0},{1}) exceeds its limit of ({2},{3}) ")
    String valueExceedsLimitDecimal(Integer precision, Integer scale, Integer actualPrecision, Integer actualScale);

    /**  */
    @DefaultMessage("Value length {0} is smaller than {1} ")
    String valueSmallerThan(Integer actualLength, Integer requiredLength);
}  // end interface MetadataFormMessages
