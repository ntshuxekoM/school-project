package com.phishing.app.payload;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureExtraction implements Serializable {

    private boolean urlSecured;
    private boolean urlContainPhishingWords;
    private boolean urlContainIP;
    private boolean urlContainAtSymbol;
    private boolean invalidUrlLength;
    private boolean urlContainForwardSlashInWrongPosition;
    private boolean urlContainDash;

}

