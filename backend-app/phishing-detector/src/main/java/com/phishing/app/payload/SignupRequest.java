package com.phishing.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest implements Serializable {
    @NotBlank
    @Size(max = 250)
    @Email
    private String email;
    private String idNumber;
    private String name;
    private String surname;
    private String cellNumber;
    private String password;
}