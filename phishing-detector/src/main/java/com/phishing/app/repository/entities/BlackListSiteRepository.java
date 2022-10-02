package com.phishing.app.repository.entities;

import com.phishing.app.model.entities.BlackListSite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlackListSiteRepository extends JpaRepository<BlackListSite, Long> {

    long count();

    @Query("SELECT COUNT(createdDate) from BlackListSite o where EXTRACT(month FROM o.createdDate) = EXTRACT(month FROM sysdate())")
    int countForMonth();
    List<BlackListSite> findFirst5ByOrderBySiteName();
}