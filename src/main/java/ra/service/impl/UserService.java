package ra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.model.domain.Role;
import ra.model.domain.RoleName;
import ra.model.domain.User;
import ra.model.dto.request.RegisterRequest;
import ra.repository.IUserRepository;
import ra.service.IRoleService;
import ra.service.IUserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public User save(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("user loi");
        }
        // lay ra danh sach cac quyen va chuyen thanh doi tuong user
        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            roles.add(roleService.findByUsername(RoleName.ROLE_USER));
        } else {
            registerRequest.getRoles().stream().forEach(
                    s -> {
                        switch (s) {
                            case "admin":
                                roles.add(roleService.findByUsername(RoleName.ROLE_ADMIN));
                            case "seller":
                                roles.add(roleService.findByUsername(RoleName.ROLE_SELLER));
                            case "user":
                                roles.add(roleService.findByUsername(RoleName.ROLE_USER));
                        }
                    }
            );
        }
        User user = User.builder()
                .name(registerRequest.getName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .status(true)
                .roles(roles)
                .build();
        return userRepository.save(user);
    }

    @Override
    public boolean exitByUsername(String name) {
        return userRepository.existsByUsername(name);
    }
}