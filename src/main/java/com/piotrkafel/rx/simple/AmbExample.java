package com.piotrkafel.rx.simple;


import rx.Observable;

import java.util.concurrent.TimeUnit;

public class AmbExample {

    public Iterable<Integer> getNumbers(long oddDelayInMilis, long evenDelayInMilis) {
        final Observable<Integer> odd = Observable.from(new Integer[]{1, 3, 5}).delay(oddDelayInMilis, TimeUnit.MILLISECONDS);
        final Observable<Integer> even = Observable.from(new Integer[]{2, 4, 6}).delay(evenDelayInMilis, TimeUnit.MILLISECONDS);

        return Observable.amb(odd, even).toBlocking().toIterable();
    }
}
