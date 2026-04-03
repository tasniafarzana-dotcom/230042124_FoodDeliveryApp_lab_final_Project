package org.fooddelivery.service;

import java.util.Optional;

import org.fooddelivery.model.DeliveryRider;
import org.fooddelivery.model.User;
import org.fooddelivery.repository.IUserRepository;
import org.fooddelivery.repository.RiderRepository;
import org.fooddelivery.repository.UserRepository;
import org.fooddelivery.util.IdGenerator;
import org.fooddelivery.util.ValidationUtils;

public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final RiderRepository riderRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
        this.riderRepository = new RiderRepository();
    }

    @Override
    public User register(String name, String email, String phone, String password, String role) {
        if (!ValidationUtils.isValidEmail(email))
            throw new IllegalArgumentException("Invalid email format");
        if (!ValidationUtils.isValidPhone(phone))
            throw new IllegalArgumentException("Invalid phone number");
        if (!ValidationUtils.isNotEmpty(name))
            throw new IllegalArgumentException("Name cannot be empty");
        if (userRepository.findByEmail(email).isPresent())
            throw new IllegalArgumentException("Email already registered");

        String id = IdGenerator.generateUserId();
        User user = new User(id, name, email, phone, password, role);
        userRepository.save(user);

        
        if ("DELIVERY_RIDER".equals(role)) {
            DeliveryRider rider = new DeliveryRider(id, name, phone);
            riderRepository.save(rider);
        }

        return user;
    }

    @Override
    public Optional<User> login(String email, String password) {
        if (!ValidationUtils.isValidEmail(email))
            throw new IllegalArgumentException("Invalid email format");
        return userRepository.findByEmail(email)
                .filter(user -> user.getPasswordHash().equals(password));
    }
}