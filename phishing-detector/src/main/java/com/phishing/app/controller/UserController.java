package com.phishing.app.controller;

import com.phishing.app.model.entities.Role;
import com.phishing.app.model.entities.User;
import com.phishing.app.payload.ChangePassRequest;
import com.phishing.app.payload.MessageResponse;
import com.phishing.app.payload.UserDetails;
import com.phishing.app.repository.entities.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.beans.BeanUtils.copyProperties;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/find_users/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findUsers(@PathVariable("id") Long id) {
        LOGGER.info("Finding user by ID: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            UserDetails userDetails = new UserDetails();
            copyProperties(optionalUser.get(), userDetails);
            Set<String> roles = new HashSet<>();
            for (Role role : optionalUser.get().getRoles()) {
                roles.add(role.getName().name());
            }
            userDetails.setRoles(roles);
            LOGGER.info("User Details: {}", userDetails);
            return ResponseEntity.ok(userDetails);
        } else {
            LOGGER.error("User not found, User ID: {}", id);
            return ResponseEntity.badRequest().body(new MessageResponse(false, "Error: user details not found"));
        }
    }

    @PostMapping("/update_users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUsers(@Valid @RequestBody UserDetails userDetails) {
        Optional<User> optionalUser = userRepository.findById(userDetails.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            copyProperties(userDetails, optionalUser.get());
            userRepository.save(user);
            return ResponseEntity.ok().body(new MessageResponse(true, "User details updated"));
        } else {
            LOGGER.error("Failed to update user details, User Details: {}", userDetails);
            return ResponseEntity.badRequest().body(new MessageResponse(false, "Invalid user ID"));
        }
    }

    @PostMapping("/change_password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassRequest changePassRequest) {
        Optional<User> optionalUser = userRepository.findById(changePassRequest.getUserId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encoder.matches(user.getPassword(), changePassRequest.getOldPassword())) {
                user.setPassword(encoder.encode(changePassRequest.getNewPassword()));
                userRepository.save(user);
                LOGGER.info("Password updated, User ID: {}", changePassRequest.getUserId());
                return ResponseEntity.ok().body(new MessageResponse(false, "Password updated successful"));
            } else {
                LOGGER.error("Invalid old password, User ID: {}", changePassRequest.getUserId());
                return ResponseEntity.badRequest().body(new MessageResponse(false, "Invalid old password"));
            }
        } else {
            LOGGER.error("User not found, User ID: {}", changePassRequest.getUserId());
            return ResponseEntity.badRequest().body(new MessageResponse(false, "Invalid user ID"));
        }
    }

}