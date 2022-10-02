package com.phishing.app.service;

import com.phishing.app.model.entities.BlackListSite;
import com.phishing.app.model.entities.Role;
import com.phishing.app.model.entities.User;
import com.phishing.app.model.entities.ValidateUrlRequest;
import com.phishing.app.model.enums.ERole;
import com.phishing.app.payload.CardData;
import com.phishing.app.payload.Site;
import com.phishing.app.payload.UserDetails;
import com.phishing.app.payload.UserUrlRequest;
import com.phishing.app.repository.entities.BlackListSiteRepository;
import com.phishing.app.repository.entities.PhishingSiteRepository;
import com.phishing.app.repository.entities.UserRepository;
import com.phishing.app.repository.entities.ValidateUrlRequestRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlackListSiteRepository blackListSiteRepository;
    @Autowired
    private ValidateUrlRequestRepository validateUrlRequestRepository;
    @Autowired
    private PhishingSiteRepository phishingSiteRepository;

    public boolean hasAdminRight(Set<Role> roles) {
        boolean isAdmin = false;
        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                if (role.getName() == ERole.ROLE_ADMIN) {
                    isAdmin = true;
                    break;
                }
            }
        }
        return isAdmin;
    }

    public CardData getBlackListedUrlsCard() {
        CardData data = new CardData();
        data.setTotal(blackListSiteRepository.count());
        data.setSubTotal(blackListSiteRepository.countForMonth());
        data.setSubTotalTitle("Added this month");
        return data;
    }

    public CardData getUserCard() {
        CardData data = new CardData();
        data.setTotal(userRepository.count());
        data.setSubTotal(userRepository.countForMonth());
        data.setSubTotalTitle("Registered this month");
        return data;
    }

    public CardData getVerificationCard() {
        CardData data = new CardData();
        data.setTotal(validateUrlRequestRepository.count());
        data.setSubTotal(validateUrlRequestRepository.countForMonth());
        data.setSubTotalTitle("Requests for this month");
        return data;
    }

    public CardData getPhishingUrlsCard() {
        CardData data = new CardData();
        data.setTotal(phishingSiteRepository.count());
        data.setSubTotal(phishingSiteRepository.countForMonth());
        data.setSubTotalTitle("Detected this month");
        return data;
    }

    public List<UserDetails> getUserDetailsList() {
        List<UserDetails> userDetailsList = null;
        List<User> userList = userRepository.findFirst5ByOrderByName();
        if (userList != null && !userList.isEmpty()) {
            userDetailsList = new ArrayList<>();
            for (User user : userList) {
                Set<String> roles = new HashSet<>();
                for (Role role : user.getRoles()) {
                    roles.add(role.getName().name());
                }
                userDetailsList.add(new UserDetails(user.getId(), user.getName(), user.getSurname(),
                    user.getEmail(), roles, user.getIdNumber(), user.getCellNumber(),
                    (int) validateUrlRequestRepository.countByCreateUser(user)));
            }

        }
        return userDetailsList;
    }

    public List<Site> getSiteList() {
        List<Site> list = null;
        List<BlackListSite> siteList = blackListSiteRepository.findFirst5ByOrderBySiteName();
        if (siteList != null && !siteList.isEmpty()) {
            list = new ArrayList<>();
            for (BlackListSite bSite : siteList) {
                list.add(new Site(bSite.getSiteName(), bSite.getUrl(),
                    validateUrlRequestRepository.countByUrl(bSite.getUrl())));
            }
        }
        return list;
    }


    public List<UserUrlRequest> getUserUrlRequests(User user) {

        List<UserUrlRequest> userUrlRequestList = new ArrayList<>();
        List<ValidateUrlRequest> list = validateUrlRequestRepository.findFirst5ByCreateUser(user);
        if (list != null) {
            list.forEach(validateUrlRequest -> userUrlRequestList.add(
                new UserUrlRequest(validateUrlRequest.getUrl(),
                    validateUrlRequest.getSafeSite())));
        }
        return userUrlRequestList;
    }
}
