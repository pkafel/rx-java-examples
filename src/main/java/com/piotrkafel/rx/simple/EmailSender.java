package com.piotrkafel.rx.simple;


import java.io.File;
import java.net.URL;

public interface EmailSender {

    void sendEmailWithAttachment(File attachment);

    void sendEmailWithLink(URL url);
}
