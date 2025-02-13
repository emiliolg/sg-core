
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.shiro;

/**
 * Shiro configuration sections.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public enum ShiroConfigSection {

    //~ Enum constants ...............................................................................................................................

    MAIN("main"), USERS("users"), ROLES("roles"), URLS("urls");

    //~ Instance Fields ..............................................................................................................................

    private final String name;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    ShiroConfigSection(String name) {
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Shiroconfig section name.
     *
     * @return  section name
     */
    public String getName() {
        return name;
    }
}
