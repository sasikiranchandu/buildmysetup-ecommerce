package com.vube.buildMySetup.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vube.buildMySetup.model.User;
import com.vube.buildMySetup.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<User> findByName(String name) {
        return userRepo.findByName(name);   // ✅ FIXED
    }
}