package com.cagla.library_automation_backend.repository;

import com.cagla.library_automation_backend.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByUsernameAndPassword(String username, String password);
}