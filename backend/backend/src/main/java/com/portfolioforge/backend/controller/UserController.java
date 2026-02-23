package com.portfolioforge.backend.controller;

import com.portfolioforge.backend.model.User;
import com.portfolioforge.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // GET /api/user/me?email=john@gmail.com
    // Temporary until JWT is set up
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User u = user.get();
            return ResponseEntity.ok(Map.of(
                    "id",       u.getId(),
                    "name",     u.getName(),
                    "email",    u.getEmail(),
                    "picture",  u.getPicture() != null ? u.getPicture() : "",
                    "phone",    u.getPhone() != null ? u.getPhone() : ""
            ));
        }
        return ResponseEntity.notFound().build();
    }

    // PUT /api/user/update
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        if (body.containsKey("phone")) {
            user.setPhone(body.get("phone"));
        }
        if (body.containsKey("name")) {
            user.setName(body.get("name"));
        }

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "User updated successfully",
                "status",  "success"
        ));
    }
}