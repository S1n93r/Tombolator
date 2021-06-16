package com.example.tombolator.tombolas;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TombolaTest {

    @Test
    public void testEnumType() {

        assertEquals(3, Tombola.TombolaTypeConverter.values().length);

        assertEquals(0, Tombola.TombolaTypeConverter.REUSE.ordinal());
        assertEquals(1, Tombola.TombolaTypeConverter.REMOVE.ordinal());
        assertEquals(2, Tombola.TombolaTypeConverter.DELETE.ordinal());
    }
}