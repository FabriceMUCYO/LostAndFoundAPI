package rw.ac.auca.lostandfound.controller;

import jakarta.validation.Valid;
import rw.ac.auca.lostandfound.model.User;
import rw.ac.auca.lostandfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (!isValidRole(user.getRole())) {
            return ResponseEntity.badRequest().body("Role must be STUDENT, STAFF, or ADMIN");
        }
        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()))) {
            return ResponseEntity.badRequest().body("A user with this email already exists");
        }
        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (!isValidRole(updatedUser.getRole())) {
                        return ResponseEntity.badRequest().body("Role must be STUDENT, STAFF, or ADMIN");
                    }
                    updatedUser.setId(id);
                    User saved = userRepository.save(updatedUser);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isValidRole(String role) {
        return "STUDENT".equalsIgnoreCase(role)
                || "STAFF".equalsIgnoreCase(role)
                || "ADMIN".equalsIgnoreCase(role);
    }
}