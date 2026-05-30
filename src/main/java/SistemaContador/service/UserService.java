package SistemaContador.service;

import SistemaContador.model.User;

public interface UserService {

    User login(String email,String password);

    boolean emailExists(String email);

    User register(User user);
}
