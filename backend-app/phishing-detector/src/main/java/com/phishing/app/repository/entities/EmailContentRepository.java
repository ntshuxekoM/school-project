package com.phishing.app.repository.entities;

import com.phishing.app.model.entities.EmailContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailContentRepository extends JpaRepository<EmailContent, Long> {
}