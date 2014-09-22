package com.piotrkafel.rx.simple;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AmbExampleTest {

    @Test
    public void getOddNumbers() {
        // Given
        final long oddDelay = 50;
        final long evenDelay = 10;

        // When
        final Iterable<Integer> integers = new AmbExample().getNumbers(oddDelay, evenDelay);

        // Then
        assertNotNull(integers);
        for (Integer integer : integers) {
            assertTrue(integer % 2 == 0);
        }
    }

    @Test
    public void getEvenNumbers() {
        // Given
        final long oddDelay = 50;
        final long evenDelay = 100;

        // When
        final Iterable<Integer> integers = new AmbExample().getNumbers(oddDelay, evenDelay);

        // Then
        assertNotNull(integers);
        for (Integer integer : integers) {
            assertTrue(integer % 2 != 0);
        }
    }
}
