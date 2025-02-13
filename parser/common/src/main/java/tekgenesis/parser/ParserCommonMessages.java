
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser;

import tekgenesis.common.env.i18n.I18nMessages;
import tekgenesis.common.env.i18n.I18nMessagesFactory;

/**
 * A set of internationalized messages for the Parser.
 */
public interface ParserCommonMessages extends I18nMessages {

    //~ Instance Fields ..............................................................................................................................

    ParserCommonMessages MSGS = I18nMessagesFactory.create(ParserCommonMessages.class);

    //~ Methods ......................................................................................................................................

    /**  */
    @DefaultMessage("any of {0}")
    String anyOf(String tokens);

    /**  */
    @DefaultMessage("Expecting {0}, but {1} found.")
    String expected(String expectedToken, String currentToken);

    /**  */
    @DefaultMessage("Parser in loop.")
    String loop();

    /**  */
    @DefaultMessage("Unexpected {0}.")
    String unexpected(String current);

    /**  */
    @DefaultMessage("Expecting a metamodel after documentation, but {0} found.")
    String unexpectedAfterDoc(String currentToken);
}
