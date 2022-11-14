
package com.phishing.app.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "threatTypes",
    "platformTypes",
    "threatEntryTypes",
    "threatEntries"
})
@Generated("jsonschema2pojo")
public class ThreatInfo {

    @JsonProperty("threatTypes")
    private List<String> threatTypes = null;
    @JsonProperty("platformTypes")
    private List<String> platformTypes = null;
    @JsonProperty("threatEntryTypes")
    private List<String> threatEntryTypes = null;
    @JsonProperty("threatEntries")
    private List<ThreatEntry> threatEntries = null;

    @JsonProperty("threatTypes")
    public List<String> getThreatTypes() {
        return threatTypes;
    }

    @JsonProperty("threatTypes")
    public void setThreatTypes(List<String> threatTypes) {
        this.threatTypes = threatTypes;
    }

    @JsonProperty("platformTypes")
    public List<String> getPlatformTypes() {
        return platformTypes;
    }

    @JsonProperty("platformTypes")
    public void setPlatformTypes(List<String> platformTypes) {
        this.platformTypes = platformTypes;
    }

    @JsonProperty("threatEntryTypes")
    public List<String> getThreatEntryTypes() {
        return threatEntryTypes;
    }

    @JsonProperty("threatEntryTypes")
    public void setThreatEntryTypes(List<String> threatEntryTypes) {
        this.threatEntryTypes = threatEntryTypes;
    }

    @JsonProperty("threatEntries")
    public List<ThreatEntry> getThreatEntries() {
        return threatEntries;
    }

    @JsonProperty("threatEntries")
    public void setThreatEntries(List<ThreatEntry> threatEntries) {
        this.threatEntries = threatEntries;
    }

}
