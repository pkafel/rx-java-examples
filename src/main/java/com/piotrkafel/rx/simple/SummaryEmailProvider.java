package com.piotrkafel.rx.simple;


import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;

public class SummaryEmailProvider {

    private final EmailSender emailSender;

    public SummaryEmailProvider(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(FileGenerationData data, long maxTimeForGeneration, TimeUnit timeUnit) {
        Observable.amb(Observable.just(data).map(new Func1<FileGenerationData, GenerationResult.STATUS>() {
            @Override
            public GenerationResult.STATUS call(FileGenerationData data) {
                return GenerationResult.STATUS.GENERATED;
            }
        }), Observable.just(GenerationResult.STATUS.TIMEOUT).delay(maxTimeForGeneration, timeUnit)).doOnNext(new Action1<GenerationResult.STATUS>() {
            @Override
            public void call(GenerationResult.STATUS s) {
                if(s == GenerationResult.STATUS.TIMEOUT) {
                    emailSender.sendEmailWithLink(null);
                } else {
                    emailSender.sendEmailWithAttachment(null);
                }
            }
        });
    }

    private static class GenerationResult {

        public enum STATUS {TIMEOUT, GENERATED};
    }
}
