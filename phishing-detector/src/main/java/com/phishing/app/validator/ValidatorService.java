package com.phishing.app.validator;

import com.phishing.app.model.entities.User;
import com.phishing.app.payload.MessageResponse;
import com.phishing.app.payload.SignupRequest;
import com.phishing.app.payload.UserDetails;
import com.phishing.app.repository.entities.UserRepository;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidatorService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> validateRegisterUser(SignupRequest signUpRequest) {

        if (!EmailValidator.getInstance().isValid(signUpRequest.getEmail())) {
            return buildBadRequestResponse("Error: Invalid email address!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return buildBadRequestResponse("Error: Email is already in use!");
        }

        if (userRepository.existsByIdNumber(signUpRequest.getIdNumber())) {
            return buildBadRequestResponse("Error: ID number is already in use!");
        }

        if (!checkRsaId(signUpRequest.getIdNumber())) {
            return buildBadRequestResponse("Error: Invalid RSA ID number!");
        }

        if (signUpRequest.getName().length() < 2 || NumberUtils.isNumber(signUpRequest.getName())) {
            return buildBadRequestResponse("Error: Invalid name!");
        }

        if (signUpRequest.getSurname().length() < 2 || NumberUtils.isNumber(
            signUpRequest.getSurname())) {
            return buildBadRequestResponse("Error: Invalid surname!");
        }

        String[] cellPrefix = {"06", "07", "08", "01"};
        if (!NumberUtils.isNumber(signUpRequest.getCellNumber())
            || signUpRequest.getCellNumber().length() != 10 || !Arrays.asList(cellPrefix)
            .contains(signUpRequest.getCellNumber().substring(0, 2))) {
            return buildBadRequestResponse("Error: Invalid cell number!");
        }

        ResponseEntity<?> validatePassword = validatePassword(signUpRequest.getPassword(),
            signUpRequest.getName(), signUpRequest.getSurname());

        if (!validatePassword.getStatusCode().equals(HttpStatus.OK)) {
            return validatePassword;
        }

        return ResponseEntity.ok(new MessageResponse(true, "Validation successful!"));
    }


    public ResponseEntity<?> validateUpdateUser(UserDetails userDetails) {

        if (!EmailValidator.getInstance().isValid(userDetails.getEmail())) {
            return buildBadRequestResponse("Error: Invalid email address!");
        }

        Optional<User> user = userRepository.findByEmail(userDetails.getEmail());
        if (user.isPresent() && !user.get().getId().equals(userDetails.getId())) {
            return buildBadRequestResponse("Error: Email is already in use!");
        }

        user = userRepository.findByIdNumber(userDetails.getIdNumber());
        if (user.isPresent() && !user.get().getId().equals(userDetails.getId())) {
            return buildBadRequestResponse("Error: ID number is already in use!");
        }

        if (!checkRsaId(userDetails.getIdNumber())) {
            return buildBadRequestResponse("Error: Invalid RSA ID number!");
        }

        if (userDetails.getName().length() < 2 || NumberUtils.isNumber(userDetails.getName())) {
            return buildBadRequestResponse("Error: Invalid name!");
        }

        if (userDetails.getSurname().length() < 2 || NumberUtils.isNumber(
            userDetails.getSurname())) {
            return buildBadRequestResponse("Error: Invalid surname!");
        }

        String[] cellPrefix = {"06", "07", "08", "01"};
        if (!NumberUtils.isNumber(userDetails.getCellNumber())
            || userDetails.getCellNumber().length() != 10 || !Arrays.asList(cellPrefix)
            .contains(userDetails.getCellNumber().substring(0, 2))) {
            return buildBadRequestResponse("Error: Invalid cell number!");
        }

        return ResponseEntity.ok(new MessageResponse(true, "Validation successful!"));
    }

    private ResponseEntity<?> buildBadRequestResponse(String mssg) {
        return ResponseEntity.badRequest().body(new MessageResponse(false, mssg));
    }


    public static boolean checkRsaId(String idVal) {
        if (idVal == null || (idVal != null && idVal.trim().length() == 0)) {
            return true;
        }
        idVal = idVal.trim();
        if (idVal.length() < 13) {
            return false;
        }
        int checkDigit = ((Integer.valueOf("" + (idVal.charAt(idVal.length() - 1)))).intValue());
        String odd = "0";
        String even = "";
        int evenResult = 0;
        int result;
        for (int c = 1; c <= idVal.length(); c++) {
            if (c % 2 == 0) {
                even += idVal.charAt(c - 1);
            } else {
                if (c == idVal.length()) {
                    continue;
                } else {
                    odd = "" + (Integer.valueOf("" + odd).intValue() + Integer.valueOf(
                        "" + (idVal.charAt(c - 1))));
                }
            }
        }
        String evenS = "" + (Integer.valueOf(even) * 2);
        for (int r = 1; r <= evenS.length(); r++) {
            evenResult += Integer.valueOf("" + evenS.charAt(r - 1));
        }
        result = (Integer.valueOf(odd) + Integer.valueOf(evenResult));
        String resultS = "" + result;
        resultS =
            "" + (10 - (Integer.valueOf("" + (resultS.charAt(resultS.length() - 1)))).intValue());
        if (resultS.length() > 1) {
            resultS = "" + resultS.charAt(resultS.length() - 1);
        }
        return Integer.valueOf(resultS) == checkDigit;
    }

    public ResponseEntity<?> validatePassword(String newPassword, String name, String surname) {
        if (newPassword != null) {
            if (newPassword.equals(name) || newPassword.equals(surname)) {
                return buildBadRequestResponse(
                    "Error: Password cannot be your Firstname or Surname!");
            }

            if (newPassword.length() < 8 || newPassword.length() < 8) {
                return buildBadRequestResponse("Error: Password must be 8 characters long!");
            }

            String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
            // Create a Pattern object
            Pattern r = Pattern.compile(regex);
            Matcher m = r.matcher(newPassword);
            if (!m.find()) {
                return buildBadRequestResponse(
                    "Error: Your password must contain a minimum of eight characters, at least one uppercase letter, one lowercase letter, one number and one special character!");
            }
        }
        return ResponseEntity.ok(new MessageResponse(true, "Validation successful"));
    }

}
