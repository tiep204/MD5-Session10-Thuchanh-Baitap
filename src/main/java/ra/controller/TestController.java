package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.model.domain.User;
import ra.service.IUserService;

import java.util.List;

@RestController
@RequestMapping("/api/v4/test")
@CrossOrigin("*")
public class TestController {
    @Autowired
    IUserService userService;
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.findAll());
    }
}