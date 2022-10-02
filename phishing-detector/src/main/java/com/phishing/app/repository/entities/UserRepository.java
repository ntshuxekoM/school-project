package com.phishing.app.repository.entities;

import com.phishing.app.model.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByIdNumber(String idNumber);

    Optional<User> findByIdNumber(String idNumber);

    long count();

    @Query("SELECT COUNT(createdDate) from User o where EXTRACT(month FROM o.createdDate) = EXTRACT(month FROM sysdate())")
    int countForMonth();

    List<User> findFirst5ByOrderByName();
}