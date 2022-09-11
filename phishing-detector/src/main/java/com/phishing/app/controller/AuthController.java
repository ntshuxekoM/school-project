package com.phishing.app.controller;

import com.phishing.app.model.entities.EmailContent;
import com.phishing.app.model.entities.Role;
import com.phishing.app.model.entities.User;
import com.phishing.app.model.enums.ERole;
import com.phishing.app.payload.JwtResponse;
import com.phishing.app.payload.LoginRequest;
import com.phishing.app.payload.MessageResponse;
import com.phishing.app.payload.SignupRequest;
import com.phishing.app.repository.entities.EmailContentRepository;
import com.phishing.app.repository.entities.RoleRepository;
import com.phishing.app.repository.entities.UserRepository;
import com.phishing.app.security.jwt.JwtUtils;
import com.phishing.app.security.services.impl.UserDetailsImpl;
import com.phishing.app.utils.Constant;
import com.phishing.app.validator.ValidatorService;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EmailContentRepository emailContentRepository;
    @Autowired
    private ValidatorService validatorService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        LOGGER.info("JWT: {}", jwt);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(), roles, userDetails.getFullName()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        ResponseEntity<?> validationResponse = validatorService.validateRegisterUser(signUpRequest);
        if (!validationResponse.getStatusCode().equals(HttpStatus.OK)) {
            return validationResponse;
        }

        // Create new user's account
        User user = new User();
        user.setIdNumber(signUpRequest.getIdNumber());
        user.setName(signUpRequest.getName());
        user.setSurname(signUpRequest.getSurname());
        user.setCellNumber(signUpRequest.getCellNumber());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = new HashSet<>();
        strRoles.add("user");//Adding default user role
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        saveEmailContent(user, signUpRequest.getPassword());

        return ResponseEntity.ok(new MessageResponse(true, "User registered successfully!"));
    }

    private void saveEmailContent(User user, String password) {
        try {
            LOGGER.info("Saving email content for [Name: {}, Surname: {}, email: {}]",
                user.getName(), user.getSurname(), user.getEmail());

            String welcome = "<p>Hi #NAME#,</p>"
                + "<br/>"
                + "<p>Your account is created successfully, below please find your login details.</p>"
                + "<br/>"
                + "<p><b>User Name: </b>  " + user.getEmail() + "</p>"
                + "<p><b>Password: </b>  " + password + "</p>"
                + "<p>Thank you for joining us</p>"
                + "<p>Our Best</p>"
                + "<p>Phishing Detractor Team</p>"
                + "<br/>";
            welcome = welcome.replaceAll("#NAME#", user.getName() + " " + user.getSurname());
            EmailContent emailContent = new EmailContent();
            emailContent.setFromEmail(Constant.APP_EMAIL);
            emailContent.setToEmail(user.getEmail());
            emailContent.setSubject("Phishing Detractor Registration");
            emailContent.setContentMssg(welcome);
            emailContent.setRetryCount(0);
            emailContent.setRunDate(new Date());
            emailContent.setCreateUser(user);
            emailContent.setLastUpdatedUser(user);
            emailContentRepository.save(emailContent);
        } catch (Exception e) {
            LOGGER.error("Error when saving registration email email: ", e);
        }
    }


}