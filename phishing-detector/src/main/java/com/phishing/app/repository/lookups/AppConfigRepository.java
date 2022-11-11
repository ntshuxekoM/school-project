package com.phishing.app.repository.lookups;
import com.phishing.app.model.lookups.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {
    AppConfig findByCode(String code);
}
