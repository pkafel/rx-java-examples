package com.piotrkafel.rx.simple;

import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AmbExampleTest {

    private static final Integer[] oddNumbers = {1, 3, 5}, evenNumbers = {2, 4, 6};

    @Test
    public void getOddNumbers() {
        // Given
        final long oddDelay = 50, evenDelay = 10;

        final Observable<Integer> odd = Observable.from(oddNumbers).delay(oddDelay, TimeUnit.MILLISECONDS);
        final Observable<Integer> even = Observable.from(evenNumbers).delay(evenDelay, TimeUnit.MILLISECONDS);

        // When
        final Iterable<Integer> integers = Observable.amb(odd, even).toBlocking().toIterable();

        // Then
        assertOutput(integers, integer -> integer % 2 == 0);
    }

    @Test
    public void getEvenNumbers() {
        // Given
        final long oddDelay = 50;
        final long evenDelay = 100;

        // When
        final Observable<Integer> odd = Observable.from(oddNumbers).delay(oddDelay, TimeUnit.MILLISECONDS);
        final Observable<Integer> even = Observable.from(evenNumbers).delay(evenDelay, TimeUnit.MILLISECONDS);

        // When
        final Iterable<Integer> integers = Observable.amb(odd, even).toBlocking().toIterable();

        // Then
        assertOutput(integers, integer -> integer % 2 != 0);
    }

    private void assertOutput(Iterable<Integer> integers, Predicate<Integer> predicate) {
        assertNotNull(integers);
        for (Integer integer : integers) {
            assertTrue(predicate.test(integer));
        }
    }
}
