package com.phishing.app.mail;

import com.phishing.app.repository.lookups.AppConfigRepository;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Slf4j
@Configuration
public class EmailConfig {

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Bean
    public JavaMailSender getMailSender() throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(appConfigRepository.findByCode("MAIL_USER").getValue());
        if (SystemUtils.IS_OS_WINDOWS) {
            log.info("************THE APPLICATION IS RUNNING ON WINDOWS OS************");
            mailSender.setPassword(appConfigRepository.findByCode("WINDOWS_MAIL_PASS").getValue());
        } else {
            log.info("************THE APPLICATION IS RUNNING ON LINUX BASED OS************");
            mailSender.setPassword(appConfigRepository.findByCode("LINUX_MAIL_PASS").getValue());
        }
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    /**
     * HTML Email config
     */
    @Bean
    public ITemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("emailTemplate/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }


}