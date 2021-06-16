package com.example.tombolator.tombolas;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TombolaTest {

    @Test
    public void testEnumType() {

        assertEquals(3, Tombola.Type.values().length);

        assertEquals(0, Tombola.Type.REUSE.ordinal());
        assertEquals(1, Tombola.Type.REMOVE.ordinal());
        assertEquals(2, Tombola.Type.DELETE.ordinal());
    }
}