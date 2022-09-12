package com.phishing.app.repository.entities;

import com.phishing.app.model.entities.PhishingSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PhishingSiteRepository extends JpaRepository<PhishingSite, Long> {

    long count();

    @Query("SELECT COUNT(createdDate) from PhishingSite o where EXTRACT(month FROM o.createdDate) = EXTRACT(month FROM sysdate())")
    int countForMonth();
}