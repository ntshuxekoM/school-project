package com.phishing.app.repository.entities;

import com.phishing.app.model.entities.EmailContentArchive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailContentArchiveRepository extends JpaRepository<EmailContentArchive, Long> {

}