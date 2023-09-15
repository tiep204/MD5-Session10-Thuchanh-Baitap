package ra.service;

import ra.model.domain.User;
import ra.model.dto.request.RegisterRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll();
    Optional<User> findByUsername(String name);
    User save (RegisterRequest registerRequest);
    boolean exitByUsername(String name);
}