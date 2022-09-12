package com.phishing.app.payload;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardData implements Serializable {

    private CardData blackListedUrlsCard;
    private CardData usersCard;
    private CardData urlVerificationCard;
    private CardData phishingUrlsCard;
    private List<UserDetails> userDetailsList;
    private List<Site> siteList;
}
