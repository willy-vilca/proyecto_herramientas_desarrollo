package SistemaContador.service.impl;

import SistemaContador.model.User;
import SistemaContador.repository.UserRepository;
import SistemaContador.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User login(String email, String password) {

        return userRepository
                .findByEmailAndPassword(email,password)
                .orElse(null);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User register(User user) {

        return userRepository.save(user);
    }
}
