
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.InputStream;
import java.io.Reader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import tekgenesis.common.collections.ImmutableIterator;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.persistence.etl.XmlStreamParser.Event.START_ELEMENT;

/**
 * A Wrapper of a {@link XMLStreamReader} with some additional methods to simplify parsing.
 */
@SuppressWarnings("WeakerAccess")
public class XmlStreamParser {

    //~ Instance Fields ..............................................................................................................................

    private Event current = null;

    private final XMLStreamReader xr;

    //~ Constructors .................................................................................................................................

    /** Create a Parser. */
    public XmlStreamParser(InputStream reader) {
        final XMLInputFactory xf = XMLInputFactory.newInstance();

        try {
            xr = xf.createXMLStreamReader(reader);
            advance();
        }
        catch (final XMLStreamException e) {
            throw new XMLException(e);
        }
    }

    /** Create a Parser. */
    public XmlStreamParser(Reader reader) {
        final XMLInputFactory xf = XMLInputFactory.newInstance();

        try {
            xr = xf.createXMLStreamReader(reader);
            advance();
        }
        catch (final XMLStreamException e) {
            throw new XMLException(e);
        }
    }

    //~ Methods ......................................................................................................................................

    /** Advance to the next Event. */
    @SuppressWarnings({ "WeakerAccess", "UnusedReturnValue" })
    public Event advance() {
        try {
            current = xr.hasNext() ? Event.valueOf(xr.next()) : Event.EOF;
            return current;
        }
        catch (final XMLStreamException e) {
            throw new XMLException(e);
        }
    }

    /** Advance to any of the specified events. */
    public Event advanceTo(Event... events) {
        advance();
        while (!eof() && !currentAnyOf(events))
            advance();
        return current;
    }

    /** Advance to EOF. */
    public void advanceToEof() {
        while (!eof())
            advance();
    }

    /** Close the parser. */
    public void close() {
        try {
            xr.close();
        }
        catch (final XMLStreamException e) {
            throw new XMLException(e);
        }
    }

    /** Check given type matches current token. */
    public final boolean current(Event eventType) {
        return getCurrent() == eventType;
    }

    /** Ask ig the next event is any of the specified. */
    public final boolean currentAnyOf(Event... eventType) {
        if (eof()) return false;
        for (final Event t : eventType) {
            if (getCurrent() == t) return true;
        }
        return false;
    }

    /** Is Parser at end of file ? */
    public boolean eof() {
        return current == Event.EOF;
    }

    /** Get the value of the specified attribute. */
    public String getAttrValue(String id) {
        return xr.getAttributeValue("", id);
    }

    /** Get the current token. */
    public final Event getCurrent() {
        return current;
    }

    /** Get the local name of the current element. */
    public String getName() {
        return xr.getLocalName();
    }

    /** Get the local name of the current text. */
    public String getText() {
        final String text = xr.getText();
        if (text == null || isEmpty(text.trim())) return null;
        else return text.trim();
    }

    Iterable<Event> iterable(Event first, Event last) {
        return () ->
               new ImmutableIterator<Event>() {
                @Override public boolean hasNext() {
                    return advanceTo(first, last) == first;
                }
                @Override public Event next() {
                    return current;
                }
            };
    }

    /**
     * check that the current element is an start element with the specified name if not advance
     * until an element is found or eof is reached.
     */
    void syncToStartElement(String elementName) {
        while (!eof() && !(current(START_ELEMENT) && getName().equals(elementName)))
            advance();
    }

    //~ Enums ........................................................................................................................................

    public enum Event {
        EOF(0), START_ELEMENT(XMLStreamConstants.START_ELEMENT), END_ELEMENT(XMLStreamConstants.END_ELEMENT),
        PROCESSING_INSTRUCTION(XMLStreamConstants.PROCESSING_INSTRUCTION), CHARACTERS(XMLStreamConstants.CHARACTERS),
        COMMENT(XMLStreamConstants.COMMENT), SPACE(XMLStreamConstants.SPACE), START_DOCUMENT(XMLStreamConstants.START_DOCUMENT),
        END_DOCUMENT(XMLStreamConstants.END_DOCUMENT), ENTITY_REFERENCE(XMLStreamConstants.ENTITY_REFERENCE), ATTRIBUTE(XMLStreamConstants.ATTRIBUTE),
        DTD(XMLStreamConstants.DTD), CDATA(XMLStreamConstants.CDATA), NAMESPACE(XMLStreamConstants.NAMESPACE),
        NOTATION_DECLARATION(XMLStreamConstants.NOTATION_DECLARATION), ENTITY_DECLARATION(XMLStreamConstants.ENTITY_DECLARATION);

        Event(int eventType) {
            assert ordinal() == eventType;
        }

        static Event valueOf(int eventType) {
            return VALUES[eventType];
        }

        private static final Event[] VALUES = values();
    }
}  // end class XmlStreamParser
