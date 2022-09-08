package com.phishing.app.controller;

import com.phishing.app.model.entities.User;
import com.phishing.app.payload.MessageResponse;
import com.phishing.app.payload.ValidateUrlRequest;
import com.phishing.app.repository.entities.RoleRepository;
import com.phishing.app.repository.entities.UserRepository;
import com.phishing.app.utils.Constant;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;
import javax.validation.Valid;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/phishing-detector")
public class AppController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/validate-url")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> validateUrl(@Valid @RequestBody ValidateUrlRequest request) {
        LOGGER.info("Validate Url Request: {}", request);
        try {
            Optional<User> optionalUser = userRepository.findById(request.getUserId());
            if (optionalUser.isPresent()) {
                //Validating URL
                UrlValidator urlValidator = new UrlValidator();
                if (urlValidator.isValid(request.getUrl())) {
                    //TODO check if url is valid or not
                    if (totalPhishingWords(request.getUrl()) > 3 && isSuspicious(
                        request.getUrl())) {
                        return ResponseEntity.ok().body(new MessageResponse(false, "Invalid URL"));
                    } else {
                        return ResponseEntity.ok().body(new MessageResponse(true, "Valid URL"));
                    }
                } else {
                    LOGGER.error("Invalid URL: {}", request.getUrl());
                    return ResponseEntity.badRequest()
                        .body(new MessageResponse(false, "Invalid URL"));
                }
            } else {
                LOGGER.error("Invalid user ID: {}", request.getUserId());
                return ResponseEntity.badRequest()
                    .body(new MessageResponse(false, "Invalid user ID"));
            }
        } catch (Exception e) {
            LOGGER.error("Error: {}", e);
            return ResponseEntity.internalServerError()
                .body(new MessageResponse(false, "Service unavailable!"));
        }
    }

    private static int totalPhishingWords(String link) throws Exception {
        int count = 0;

        URL url = new URL(link);
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            if (scanner.next() != null && !scanner.next().trim().isEmpty()) {
                for (String word : Constant.PHISHING_WORDS) {
                    if (scanner.next().trim().toLowerCase().contains(word.toLowerCase())) {
                        count++;
                        LOGGER.info(
                            "Phishing word detected. [phishing word: {}, Scanned word: {}, Url: {}]",
                            word, scanner.next().trim(), link);
                    }
                }
            }

        }

        return count;
    }

    private boolean isSuspicious(String url) {
        //TODO https://developers.google.com/safe-browsing/v4/get-started
        return false;
    }


}
