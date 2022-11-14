
package com.phishing.app.bean;

import java.util.HashMap;
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
    "client",
    "threatInfo"
})
@Generated("jsonschema2pojo")
public class SafeBrowsingRequest {

    @JsonProperty("client")
    private Client client;
    @JsonProperty("threatInfo")
    private ThreatInfo threatInfo;

    @JsonProperty("client")
    public Client getClient() {
        return client;
    }

    @JsonProperty("client")
    public void setClient(Client client) {
        this.client = client;
    }

    @JsonProperty("threatInfo")
    public ThreatInfo getThreatInfo() {
        return threatInfo;
    }

    @JsonProperty("threatInfo")
    public void setThreatInfo(ThreatInfo threatInfo) {
        this.threatInfo = threatInfo;
    }

}
