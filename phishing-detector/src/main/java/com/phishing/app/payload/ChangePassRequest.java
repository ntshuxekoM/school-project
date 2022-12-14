package com.phishing.app.payload;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassRequest implements Serializable {

    @NotNull
    private Long userId;
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

}