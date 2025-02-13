
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Exception when the key that wants to be localized it's invalid.
 */
public class InvalidKeyToBeLocalized extends RuntimeException {

    //~ Instance Fields ..............................................................................................................................

    private final InvalidKeyLocalizeType type;

    //~ Constructors .................................................................................................................................

    /** @param  locale  in which properties file wasn't found. */
    public InvalidKeyToBeLocalized(String key, InvalidKeyLocalizeType type, Object... args) {
        super("The key ( " + key + " ) has an invalid format: " + type.description(args));
        this.type = type;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the type of error found. */
    @NotNull public InvalidKeyLocalizeType getType() {
        return type;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7129676170705181807L;

    //~ Enums ........................................................................................................................................

    public enum InvalidKeyLocalizeType {
        TOO_MANY_ARGUMENTS("Arguments allowed: %s. Arguments found: %s."), INVALID_KEY("This key '%s' wasn't found in .properties file."),
        FIELD_OPTION_DOESNT_EXIST("Widget '%s' doesn't exist in form '%s'."),
        FIELD_OPTION_CANNOT_LOCALIZE("This field option '%s' cannot be localized"), NOT_A_NUMBER("Third parameter must be an integer, '%s' is not"),
        NOT_THAT_MANY_CHECKS("Index out of bounds of Checks. Index: '%s'. Size: '%s'");

        private final String description;

        InvalidKeyLocalizeType(String description) {
            this.description = description;
        }

        String description(Object... args) {
            return String.format(description, args);
        }
    }
}  // end class InvalidKeyToBeLocalized
