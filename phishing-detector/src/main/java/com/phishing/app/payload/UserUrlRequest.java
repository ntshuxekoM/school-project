package com.phishing.app.payload;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUrlRequest implements Serializable {

    private String url;
    private Boolean safeSite;

}