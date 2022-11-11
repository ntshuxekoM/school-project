package com.phishing.app.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private MailSender mailSender;

    @Scheduled(fixedDelay = 2000)
    public void scheduleTaskWithFixedDelay() {
        mailSender.sendEmailContent();
    }

}