package com.phishing.app.controller;

import com.phishing.app.model.entities.Role;
import com.phishing.app.model.entities.User;
import com.phishing.app.model.enums.ERole;
import com.phishing.app.payload.MessageResponse;
import com.phishing.app.payload.UserDetails;
import com.phishing.app.payload.ValidateUrlRequest;
import com.phishing.app.repository.entities.RoleRepository;
import com.phishing.app.repository.entities.UserRepository;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.beans.BeanUtils.copyProperties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/phishing-detector")
public class AppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/test/hello/v1")
    public String hello() {

        Role role1 = new Role();
        role1.setName(ERole.ROLE_USER);
        roleRepository.saveAndFlush(role1);

        role1 = new Role();
        role1.setName(ERole.ROLE_ADMIN);
        roleRepository.saveAndFlush(role1);

        return "Ya runner!!";
    }

    @PostMapping("/validate-url")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> validateUrl(@Valid @RequestBody ValidateUrlRequest request) {
        LOGGER.info("Validate Url Request: {}", request);
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isPresent()) {
            //Validating URL
            UrlValidator urlValidator = new UrlValidator();
            if(urlValidator.isValid(request.getUrl())){
                //TODO check if url is valid or not
                return ResponseEntity.ok().body(new MessageResponse(true, "Valid URL"));
            }else{
                LOGGER.error("Invalid URL: {}", request.getUrl());
                return ResponseEntity.badRequest().body(new MessageResponse(false, "Invalid URL"));
            }
        } else {
            LOGGER.error("Invalid user ID: {}", request.getUserId());
            return ResponseEntity.badRequest().body(new MessageResponse(false, "Invalid user ID"));
        }
    }

}
