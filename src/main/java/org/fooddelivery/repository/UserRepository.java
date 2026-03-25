package org.fooddelivery.repository;

import com.google.gson.reflect.TypeToken;
import org.fooddelivery.model.User;

import java.util.List;
import java.util.Optional;

public class UserRepository extends FileRepository<User> implements IUserRepository {

    public UserRepository() {
        super("data/users.json", new TypeToken<List<User>>(){}.getType());
    }

    @Override
    protected String getId(User user) {
        return user.getId();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public List<User> findByRole(String role) {
        return findAll().stream()
                .filter(user -> user.getRole().equalsIgnoreCase(role))
                .toList();
    }
}