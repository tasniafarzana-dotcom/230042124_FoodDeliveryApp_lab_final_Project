package org.fooddelivery.api;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.fooddelivery.model.User;
import org.fooddelivery.service.AuthService;
import org.fooddelivery.service.IAuthService;

@WebService(targetNamespace = "http://api.fooddelivery.org/")
public class AuthSoapService {
    private final IAuthService authService;

    public AuthSoapService() {
        this.authService = new AuthService();
    }

    @WebMethod
    public User register(
            @WebParam(name = "name") String name,
            @WebParam(name = "email") String email,
            @WebParam(name = "phone") String phone,
            @WebParam(name = "password") String password,
            @WebParam(name = "role") String role
    ) {
        return authService.register(name, email, phone, password, role);
    }

    @WebMethod
    public User login(
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password
    ) {
        return authService.login(email, password)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }
}