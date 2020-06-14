package io.github.jrbase.client.utils.sets;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SetToolsTest {

    @Test
    public void makeRandomSets() {
        Set<String> sets = new HashSet<>();
        sets.add("a");
        sets.add("b");
        sets.add("c");
        sets.add("d");
        sets.add("e");
        List<String> strings = SetTools.makeRandomSets(sets, 10);
        assertEquals(5, strings.size());

    }

    @Test
    public void makeRandomSets2() {
        Set<String> sets = new HashSet<>();
        sets.add("a");
        sets.add("b");
        sets.add("c");
        sets.add("d");
        sets.add("e");

        List<String> strings = SetTools.makeRandomSets(sets, 2);
        assertEquals(2, strings.size());
    }
}
