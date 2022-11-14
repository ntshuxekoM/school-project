
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
    "clientId",
    "clientVersion"
})
@Generated("jsonschema2pojo")
public class Client {

    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("clientVersion")
    private String clientVersion;

    @JsonProperty("clientId")
    public String getClientId() {
        return clientId;
    }

    @JsonProperty("clientId")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @JsonProperty("clientVersion")
    public String getClientVersion() {
        return clientVersion;
    }

    @JsonProperty("clientVersion")
    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }


}
