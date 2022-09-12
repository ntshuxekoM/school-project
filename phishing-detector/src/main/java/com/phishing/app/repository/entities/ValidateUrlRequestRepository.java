package com.phishing.app.repository.entities;

import com.phishing.app.model.entities.ValidateUrlRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ValidateUrlRequestRepository extends JpaRepository<ValidateUrlRequest, Long> {

    long countByUrl(String url);

    long count();

    @Query("SELECT COUNT(createdDate) from ValidateUrlRequest o where EXTRACT(month FROM o.createdDate) = EXTRACT(month FROM sysdate())")
    int countForMonth();

}