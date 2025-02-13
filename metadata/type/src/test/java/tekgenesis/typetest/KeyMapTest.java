
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.typetest;

import org.junit.Test;

import tekgenesis.common.core.Tuple;
import tekgenesis.model.KeyMap;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("JavaDoc")
public class KeyMapTest {

    //~ Methods ......................................................................................................................................

    @Test public void keyMapTest() {
        final KeyMap keyMap = KeyMap.singleton(Tuple.tuple("key1", "v1"));

        assertThat(keyMap.get("key1")).isEqualTo("v1");

        keyMap.put("key2", "v2");

        assertThat(keyMap.size()).isEqualTo(2);

        keyMap.put("key1", "v3");

        assertThat(keyMap.get("key1")).isEqualTo("v3");

        assertThat(keyMap.entrySet().size()).isEqualTo(2);
    }
}
