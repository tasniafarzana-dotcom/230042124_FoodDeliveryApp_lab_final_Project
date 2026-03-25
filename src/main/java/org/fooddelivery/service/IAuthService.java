package org.fooddelivery.service;

import org.fooddelivery.model.User;

import java.util.Optional;

public interface IAuthService {
    User register(String name, String email, String phone, String password, String role);
    Optional<User> login(String email, String password);
}
