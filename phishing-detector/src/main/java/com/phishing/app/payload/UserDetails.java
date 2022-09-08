package com.phishing.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private Set<String> roles = new HashSet<>();
    private String idNumber;
    private String cellNumber;

}