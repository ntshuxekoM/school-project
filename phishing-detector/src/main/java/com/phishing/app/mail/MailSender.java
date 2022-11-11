package com.phishing.app.mail;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.phishing.app.model.entities.EmailContent;
import com.phishing.app.model.entities.EmailContentArchive;
import com.phishing.app.repository.entities.EmailContentArchiveRepository;
import com.phishing.app.repository.entities.EmailContentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class MailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EmailContentRepository emailContentRepository;

    @Autowired
    private EmailContentArchiveRepository emailContentArchiveRepository;

    @Autowired
    private JavaMailSender emailSender;

    protected final Log logger = LogFactory.getLog(this.getClass());

    public void sendEmailContent() {

        try {

            logger.info("***************SENDING EMAILS*****************");
            List<EmailContent> emailContentList = emailContentRepository.findAll();

            List<EmailContent> sentEmailContentList = new ArrayList<>();

            logger.info("TOTAL EMAILS: ".concat(String.valueOf(emailContentList.size())));

            if (emailContentList != null && !emailContentList.isEmpty()) {
                for (EmailContent emailContent : emailContentList) {
                    try {
                        sendHtmlEmil(buildMail(emailContent));
                        sentEmailContentList.add(emailContent);
                    } catch (Exception e) {
                        logger.info("Error when sending email with ID " + emailContent.getId()
                            + ": ".concat(e.getMessage()));
                        emailContent.setErrorMessage("Error Message: ".concat(e.getMessage()));
                        int retryCount = 1;
                        if (emailContent.getRetryCount() != null) {
                            retryCount = emailContent.getRetryCount() + 1;
                        }
                        if (retryCount == 10) {
                            sentEmailContentList.add(emailContent);
                        }
                        emailContent.setRetryCount(retryCount);
                        emailContentRepository.save(emailContent);
                        e.printStackTrace();
                    }
                }

                emailContentRepository.deleteAll(sentEmailContentList);
                sentEmailContentList.forEach(emailContent -> {
                    EmailContentArchive emailContentArchive = new EmailContentArchive();
                    copyProperties(emailContent, emailContentArchive);
                    emailContentArchiveRepository.save(emailContentArchive);
                });

            } else {
                logger.info("***************NO PENDING EMAILS AVAILABLE*****************");
            }

        } catch (Exception e) {
            logger.info("Error when sending emails: ".concat(e.getMessage()));
            e.printStackTrace();
        }

    }

    public void sendHtmlEmil(Mail mail) throws Exception {

        logger.info("***************SEND HTML EMIL*****************");
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(mail.getSubject());
            helper.setText(generateMailHtml(mail.getContent()), true);
            helper.setTo(mail.getTo());
            helper.setFrom(mail.getFrom());
            helper.setBcc(mail.getCc());
            emailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("We are unable to send email at the moment. Error Message: ".concat(
                e.getMessage()));
        }

    }

    private Mail buildMail(EmailContent emailContent) {
        Mail mail = new Mail();
        mail.setContent(emailContent.getContentMssg());
        mail.setFrom(emailContent.getFromEmail());
        mail.setTo(buildStringArray(emailContent.getToEmail()));
        mail.setSubject(emailContent.getSubject());
        mail.setCc(buildStringArray(emailContent.getCcEmails()));
        return mail;
    }

    private String[] buildStringArray(String value) {
        if (value != null) {
            return value.split(" ");
        } else {
            String[] elements = {};
            return elements;
        }
    }

    public String generateMailHtml(String body) {
        final String templateFileName = "content";//Name of the HTML template file without extension

        String output = this.templateEngine.process(templateFileName,
            new Context(Locale.getDefault(), null));

        output = output.replace("#BODY#", body);
        return output;
    }

}
