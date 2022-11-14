
package com.phishing.app.bean;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "threatType",
    "platformType",
    "threat",
    "cacheDuration",
    "threatEntryType"
})
@Generated("jsonschema2pojo")
public class Match {

    @JsonProperty("threatType")
    private String threatType;
    @JsonProperty("platformType")
    private String platformType;
    @JsonProperty("threat")
    private Threat threat;
    @JsonProperty("cacheDuration")
    private String cacheDuration;
    @JsonProperty("threatEntryType")
    private String threatEntryType;

    @JsonProperty("threatType")
    public String getThreatType() {
        return threatType;
    }

    @JsonProperty("threatType")
    public void setThreatType(String threatType) {
        this.threatType = threatType;
    }

    @JsonProperty("platformType")
    public String getPlatformType() {
        return platformType;
    }

    @JsonProperty("platformType")
    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    @JsonProperty("threat")
    public Threat getThreat() {
        return threat;
    }

    @JsonProperty("threat")
    public void setThreat(Threat threat) {
        this.threat = threat;
    }

    @JsonProperty("cacheDuration")
    public String getCacheDuration() {
        return cacheDuration;
    }

    @JsonProperty("cacheDuration")
    public void setCacheDuration(String cacheDuration) {
        this.cacheDuration = cacheDuration;
    }

    @JsonProperty("threatEntryType")
    public String getThreatEntryType() {
        return threatEntryType;
    }

    @JsonProperty("threatEntryType")
    public void setThreatEntryType(String threatEntryType) {
        this.threatEntryType = threatEntryType;
    }

}
