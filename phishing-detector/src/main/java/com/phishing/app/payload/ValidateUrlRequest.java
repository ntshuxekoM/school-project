package com.phishing.app.payload;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateUrlRequest implements Serializable {

    @NotNull
    private Long userId;
    @NotBlank
    private String url;

}