package com.phishing.app.model.entities;

import com.phishing.app.model.common.AbstractEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_content_archive")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailContentArchive extends AbstractEntity {

    @Column(name = "from_email")
    private String fromEmail;

    @Column(name = "to_email")
    private String toEmail;

    @Column(name = "cc_email")
    private String ccEmails;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content_mssg", length = 5000)
    private String contentMssg;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "error_message", length = 5000)
    private String errorMessage;

    @Column(name = "run_date", length = 19)
    private Date runDate;

}