package com.cagla.library_automation_backend.service;

import com.cagla.library_automation_backend.entity.UserAccount;
import com.cagla.library_automation_backend.exception.BadRequestException;
import com.cagla.library_automation_backend.repository.UserAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public AuthService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public boolean login(String username, String password) {
        UserAccount user = userAccountRepository.findByUsernameAndPassword(
                username,
                password
        );

        return user != null;
    }

    public UserAccount register(UserAccount userAccount) {
        if (userAccount.getUsername() == null || userAccount.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Kullanıcı adı boş olamaz.");
        }

        if (userAccount.getPassword() == null || userAccount.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Şifre boş olamaz.");
        }

        return userAccountRepository.save(userAccount);
    }
}