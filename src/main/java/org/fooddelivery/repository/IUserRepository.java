package org.fooddelivery.repository;

import org.fooddelivery.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends IRepository<User> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
}