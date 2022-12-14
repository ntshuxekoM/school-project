package com.phishing.app.payload;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Site implements Serializable {

    private String name;
    private String url;
    private long searchCount;

}
