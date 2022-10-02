package com.phishing.app.repository.entities;

import com.phishing.app.model.entities.User;
import com.phishing.app.model.entities.ValidateUrlRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ValidateUrlRequestRepository extends JpaRepository<ValidateUrlRequest, Long> {

    long countByUrl(String url);

    long count();

    @Query("SELECT COUNT(createdDate) from ValidateUrlRequest o where EXTRACT(month FROM o.createdDate) = EXTRACT(month FROM sysdate())")
    int countForMonth();

    long countByCreateUser(User user);
    List<ValidateUrlRequest> findFirst5ByCreateUser(User user);
    List<ValidateUrlRequest> findByCreateUser(User user);

}