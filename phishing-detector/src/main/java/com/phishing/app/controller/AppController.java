package com.phishing.app.controller;

import static com.phishing.app.utils.Constant.SAFE_BROWSING_URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phishing.app.bean.Client;
import com.phishing.app.bean.SafeBrowsingRequest;
import com.phishing.app.bean.SafeBrowsingResponse;
import com.phishing.app.bean.ThreatEntry;
import com.phishing.app.bean.ThreatInfo;
import com.phishing.app.model.entities.BlackListSite;
import com.phishing.app.model.entities.PhishingSite;
import com.phishing.app.model.entities.User;
import com.phishing.app.payload.DashboardData;
import com.phishing.app.payload.FeatureExtraction;
import com.phishing.app.payload.MessageResponse;
import com.phishing.app.payload.Site;
import com.phishing.app.payload.UserUrlRequest;
import com.phishing.app.payload.ValidateUrlRequest;
import com.phishing.app.repository.entities.BlackListSiteRepository;
import com.phishing.app.repository.entities.PhishingSiteRepository;
import com.phishing.app.repository.entities.RoleRepository;
import com.phishing.app.repository.entities.UserRepository;
import com.phishing.app.repository.entities.ValidateUrlRequestRepository;
import com.phishing.app.service.DashboardService;
import com.phishing.app.utils.Constant;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/phishing-detector")
public class AppController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private ValidateUrlRequestRepository validateUrlRequestRepository;
    @Autowired
    private BlackListSiteRepository blackListSiteRepository;
    @Autowired
    private PhishingSiteRepository phishingSiteRepository;

    /**
     * This method returns dashboard data used for reporting This include total number of
     * blacklisted sites,registered users total number of verification requests sent,and total
     * phishing urls
     * <p>
     * Only users with general user role (ROLE_USER) can access this method
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-dashboard-data/{userId}")
    public ResponseEntity<?> getDashboardData(@PathVariable Long userId) {
        LOGGER.info("Get Dashboard data. User: {}", userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        DashboardData dashboardData = new DashboardData();
        if (optionalUser.isPresent()) {

            dashboardData.setBlackListedUrlsCard(dashboardService.getBlackListedUrlsCard());
            dashboardData.setUsersCard(dashboardService.getUserCard());
            dashboardData.setUrlVerificationCard(dashboardService.getVerificationCard());
            dashboardData.setPhishingUrlsCard(dashboardService.getPhishingUrlsCard());

            dashboardData.setSiteList(dashboardService.getSiteList());
            if (dashboardService.hasAdminRight(optionalUser.get().getRoles())) {
                dashboardData.setUserDetailsList(dashboardService.getUserDetailsList());
            } else {
                dashboardData.setUserUrlRequestList(
                    dashboardService.getUserUrlRequests(optionalUser.get()));
            }

            LOGGER.info("Get Dashboard data. User: {}, Data: {}", userId, dashboardData);
            return ResponseEntity.ok(dashboardData);

        } else {
            LOGGER.error("User not found, User ID: {}", userId);
            return ResponseEntity.badRequest()
                .body(new MessageResponse(false, "Error: user details not found"));
        }

    }


    /**
     * Find and returns all the url request sent by user
     */
    @GetMapping("/find_all_user_url_request/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findAllUserUrlRequest(@PathVariable Long userId) {
        LOGGER.info("Finding all User Url Request");

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            List<UserUrlRequest> userUrlRequestList = new ArrayList<>();
            List<com.phishing.app.model.entities.ValidateUrlRequest> list = validateUrlRequestRepository.findByCreateUser(
                optionalUser.get());
            if (list != null) {
                list.forEach(validateUrlRequest -> userUrlRequestList.add(
                    new UserUrlRequest(validateUrlRequest.getUrl(),
                        validateUrlRequest.getSafeSite())));
            }

            LOGGER.info("All User Url Request size {}", userUrlRequestList.size());
            return ResponseEntity.ok(userUrlRequestList);

        } else {
            LOGGER.error("User not found, User ID: {}", userId);
            return ResponseEntity.badRequest()
                .body(new MessageResponse(false, "Error: user details not found"));
        }
    }


    /**
     * Finds and return all the list of blacklisted sites
     */
    @GetMapping("/find_all_blacklisted_sites")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findAllBlacklistedSites() {
        LOGGER.info("Finding all blacklisted sites...");
        List<BlackListSite> siteList = blackListSiteRepository.findAll();
        if (siteList != null && !siteList.isEmpty()) {
            List<Site> list = null;
            if (siteList != null && !siteList.isEmpty()) {
                list = new ArrayList<>();
                for (BlackListSite bSite : siteList) {
                    list.add(new Site(bSite.getSiteName(), bSite.getUrl(),
                        validateUrlRequestRepository.countByUrl(bSite.getUrl())));
                }
            }
            LOGGER.info("All Blacklisted sites size {}", list.size());
            return ResponseEntity.ok(list);

        } else {
            LOGGER.error("No Blacklisted sites..");
            return ResponseEntity.badRequest()
                .body(new MessageResponse(false, "Error: No Blacklisted sites"));
        }
    }

    /**
     * This methods checks if a URL is a phishing URL or not
     * First we check if the url exist in table that contains a list of blacklisted sites or
     * in a table that contains a list of phishing sites.
     *
     * If the url does not exist in our database, we use google safe browsing API to check
     * if the URL is not listed as an "unsafe" url on google
     *
     * Finally, we use an algorithm to analyse the url and
     * decide if the url might be a phishing url or not
     *
     */
    @PostMapping("/validate-url")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> validateUrl(@Valid @RequestBody ValidateUrlRequest request) {
        LOGGER.info("Validate Url Request: {}", request);
        try {
            Optional<User> optionalUser = userRepository.findById(request.getUserId());
            com.phishing.app.model.entities.ValidateUrlRequest validateUrlRequest = validateUrlRequestRepository.save(
                buildRequest(optionalUser, request));
            if (optionalUser.isPresent()) {

                if (isUrlValid(request.getUrl())) {

                    if (checkIfUrlExistInOurDB(request) || isSuspicious(request.getUrl())
                        || !isSiteLegit(request.getUrl())) {
                        savePhishingSite(request, optionalUser.get());
                        validateUrlRequest.setSafeSite(false);
                        return ResponseEntity.ok().body(new MessageResponse(false, "Invalid URL"));
                    } else {
                        validateUrlRequest.setSafeSite(true);
                        return ResponseEntity.ok().body(new MessageResponse(true, "Valid URL"));
                    }

                } else {
                    LOGGER.error("Invalid URL: {}", request.getUrl());
                    return ResponseEntity.badRequest()
                        .body(new MessageResponse(false, "Invalid URL"));
                }
            } else {
                LOGGER.error("Invalid user ID: {}", request.getUserId());
                return ResponseEntity.badRequest()
                    .body(new MessageResponse(false, "Invalid user ID"));
            }
        } catch (Exception e) {
            LOGGER.error("Error: {}", e);
            return ResponseEntity.internalServerError()
                .body(new MessageResponse(false, "Service unavailable!"));
        }
    }


    /**
     * This method checks if the url exist in our database
     *
     * First we check if the url exist in black list site table
     * if it does not exist, we check if the url
     * exist in phishing site table
     * */
    private boolean checkIfUrlExistInOurDB(ValidateUrlRequest request) {
        boolean inValidUrl;
        List<BlackListSite> blackListSiteList = blackListSiteRepository.findAll();

        BlackListSite blackListSite = null;
        if (blackListSiteList != null && !blackListSiteList.isEmpty()) {
            blackListSite = blackListSiteList.stream().filter(bs -> bs.getUrl().trim()
                .equalsIgnoreCase(request.getUrl().trim())).findAny().orElse(null);
        }

        inValidUrl = blackListSite != null;

        LOGGER.info("Is in black listed site? {}, URL: {}", inValidUrl, request.getUrl());
        if (!inValidUrl) {

            List<PhishingSite> phishingSiteList = phishingSiteRepository.findAll();

            PhishingSite phishingSite = null;

            if (phishingSiteList != null && !phishingSiteList.isEmpty()) {
                phishingSite = phishingSiteList.stream()
                    .filter(ps -> ps.getUrl().trim().equalsIgnoreCase(request.getUrl())).findAny()
                    .orElse(null);
            }

            inValidUrl = phishingSite != null;

            LOGGER.info("Is in phishing site table? {}, URL: {}", inValidUrl, request.getUrl());
        }
        LOGGER.info("URL: {}, Is black listed? : {}", request.getUrl(), inValidUrl);

        return inValidUrl;
    }

    /**
     * This method save url that has been identified as phishing site
     * */
    private void savePhishingSite(ValidateUrlRequest request, User user) {
        try {
            LOGGER.info("Saving phishing site. URL: {}, User ID: {}", request.getUrl(),
                request.getUserId());
            PhishingSite site = new PhishingSite();
            site.setUrl(request.getUrl());
            site.setCreatedDate(new Date());
            site.setUpdatedDate(new Date());
            site.setCreateUser(user);
            site.setLastUpdatedUser(user);
            phishingSiteRepository.save(site);
        } catch (Exception e) {
            LOGGER.error("Error when saving site: ", e);
        }
    }

    /**
     * This method checks if a url is suspicious or not,
     * To do so we use google safe browsing API
     * */
    private static boolean isSuspicious(String theUrl) {
        LOGGER.info("Checking if url is suspicious. URL: {} ", theUrl);
        boolean isSuspicious = false;
        try {
            URL url = new URL(SAFE_BROWSING_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = objectMapper.writeValueAsString(buildSafeBrowsingRequest(theUrl));
            LOGGER.info("SafeBrowsingRequest Json Request: {}", jsonRequest);
            wr.writeBytes(jsonRequest);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

            SafeBrowsingResponse safeBrowsingResponse = (SafeBrowsingResponse) convertJsonToObject(
                response.toString(), new SafeBrowsingResponse());

            isSuspicious = safeBrowsingResponse != null && safeBrowsingResponse.getMatches() != null
                && !safeBrowsingResponse.getMatches().isEmpty();

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Failed to check the url using google browse safe: {}", e);
        }
        LOGGER.info("URL: {}, isSuspicious: {}", theUrl, isSuspicious);
        return isSuspicious;
    }

    /**
     * Building safe browsing request
     * */
    private static SafeBrowsingRequest buildSafeBrowsingRequest(String theUrl) {

        SafeBrowsingRequest request = new SafeBrowsingRequest();

        Client client = new Client();
        client.setClientId("Phishing Detector");
        client.setClientVersion("1.0");
        request.setClient(client);

        ThreatInfo threatInfo = new ThreatInfo();
        List<String> threatTypes = new ArrayList<>();
        threatTypes.add("MALWARE");
        threatTypes.add("SOCIAL_ENGINEERING");
        threatTypes.add("THREAT_TYPE_UNSPECIFIED");
        threatTypes.add("UNWANTED_SOFTWARE");
        threatTypes.add("POTENTIALLY_HARMFUL_APPLICATION");
        threatInfo.setThreatTypes(threatTypes);

        List<String> platformTypesList = new ArrayList<>();
        platformTypesList.add("PLATFORM_TYPE_UNSPECIFIED");
        platformTypesList.add("WINDOWS");
        platformTypesList.add("LINUX");
        platformTypesList.add("ANDROID");
        platformTypesList.add("OSX");
        platformTypesList.add("IOS");
        platformTypesList.add("ANY_PLATFORM");
        platformTypesList.add("ALL_PLATFORMS");
        platformTypesList.add("CHROME");
        threatInfo.setPlatformTypes(platformTypesList);

        List<String> threatEntryTypes = new ArrayList<>();
        threatEntryTypes.add("URL");
        threatInfo.setThreatEntryTypes(threatEntryTypes);

        List<ThreatEntry> threatEntries = new ArrayList<>();
        ThreatEntry threatEntry = new ThreatEntry();
        threatEntry.setUrl(theUrl);
        threatEntries.add(threatEntry);
        threatInfo.setThreatEntries(threatEntries);

        request.setThreatInfo(threatInfo);
        return request;
    }

    private com.phishing.app.model.entities.ValidateUrlRequest buildRequest(
        Optional<User> optionalUser, ValidateUrlRequest request) {
        User user = (optionalUser.isPresent() ? optionalUser.get() : null);
        com.phishing.app.model.entities.ValidateUrlRequest urlRequest = new com.phishing.app.model.entities.ValidateUrlRequest();
        urlRequest.setUrl(request.getUrl());
        urlRequest.setCreateUser(user);
        urlRequest.setLastUpdatedUser(user);
        return urlRequest;
    }

    private static Object convertJsonToObject(String json, Object objectClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, objectClass.getClass());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error when converting Json to object: ", e);
            return null;
        }
    }


    /**
     * This method determine if a url is a phishing url or not
     * by extracting the url elements and analyse them
     * */
    private boolean isSiteLegit(String url) {
        boolean isLegit = true;
        FeatureExtraction featureExtraction = new FeatureExtraction();
        /**Secured URL should contain HTTPS, if not, it might be a phishing URL*/
        featureExtraction.setUrlSecured(isSiteSecured(url));

        /**
         * Checking if the site contains words that are commonly used for phishing
         * */
        featureExtraction.setUrlContainPhishingWords(totalPhishingWords(url) > 0);

        /**
         * URLs may have IP address instead of domain name. If an IP
         * address is used as an alternative of the domain name in the URL,
         * we can be sure that someone is trying to steal personal information with this URL.
         * */
        featureExtraction.setUrlContainIP(containIP(url));

        /**Using “@” symbol in the URL leads the browser to ignore everything preceding
         the “@” symbol and the real address often follows the “@” symbol.*/
        featureExtraction.setUrlContainAtSymbol(url.contains("@"));

        /**Phishers can use long URL to hide the doubtful part in the address bar.
         In this project, if the length of the URL is greater than or equal 54 characters then
         the URL classified as phishing otherwise legitimate.*/
        featureExtraction.setInvalidUrlLength(url.length() > 55);

        /**
         * The existence of “//” within the URL path means that the user will be redirected to another
         * website. The location of the “//” in URL is computed. if the URL starts with “HTTP”, that
         * means the “//” should appear in the sixth position. However, if the URL employs “HTTPS” then
         * the “//” should appear in seventh position.
         */
        featureExtraction.setUrlContainForwardSlashInWrongPosition(
            UrlContainForwardSlashInWrongPosition(url));

        /**
         * The dash symbol is rarely used in legitimate URLs.
         * Phishers tend to add prefixes or suffixes separated by (-) to
         * the domain name so that users feel that they are dealing with a legitimate webpage.
         * */
        featureExtraction.setUrlContainDash(url.contains("-"));

        /***
         *If the total number of phishing characteristics is 3 or more
         * We return false, meaning the site/url is not safe
         */
        if (getPhishingChecksCount(featureExtraction) > 2) {
            isLegit = false;
        }

        LOGGER.info("URL: {}, Feature Extraction {}", url, featureExtraction);
        return isLegit;
    }


    /**
     * This method return the total number of url check that
     * are identified as characteristics of a phishing url
     * */
    private static int getPhishingChecksCount(FeatureExtraction featureExtraction) {
        int phishingBenchMark = 0;
        if (!featureExtraction.isUrlSecured()) {
            phishingBenchMark++;
        }

        if (featureExtraction.isUrlContainPhishingWords()) {
            phishingBenchMark++;
        }

        if (featureExtraction.isUrlContainIP()) {
            phishingBenchMark++;
        }

        if (featureExtraction.isUrlContainAtSymbol()) {
            phishingBenchMark++;
        }

        if (featureExtraction.isInvalidUrlLength()) {
            phishingBenchMark++;
        }

        if (featureExtraction.isUrlContainForwardSlashInWrongPosition()) {
            phishingBenchMark++;
        }

        if (featureExtraction.isUrlContainDash()) {
            phishingBenchMark++;
        }

        LOGGER.info("Phishing Bench Mark: {}", phishingBenchMark);

        return phishingBenchMark;
    }


    /**
     * The existence of “//” within the URL path means that the user will be redirected to another
     * website. The location of the “//” in URL is computed. if the URL starts with “HTTP”, that
     * means the “//” should appear in the sixth position. However, if the URL employs “HTTPS” then
     * the “//” should appear in seventh position.
     */
    private boolean UrlContainForwardSlashInWrongPosition(String url) {
        int index = url.indexOf("//");
        LOGGER.info("URL: {}, Index of //: {}", url, index);
        if (index >= 5) {
            if (index >= 6) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    /**
     * URLs may have IP address instead of domain name. If an IP address is used as an alternative
     * of the domain name in the URL, we can be sure that someone is trying to steal personal
     * information with this URL.
     */
    private boolean containIP(String url) {
        try {
            InetAddress ip = InetAddress.getByName(new URL(url)
                .getHost());
            LOGGER.info("URL: {}, HostAddress: {},HostName: {} ", url, ip.getHostAddress(),
                ip.getHostName());
            if (url.contains(ip.getHostAddress())) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Invalid URL: {}", url);
        }

        return false;
    }

    /**
     * Check if the url is secured
     * URL that are secured should contain https
     * */
    private boolean isSiteSecured(String url) {
        return url.contains("https");
    }


    /**
     * This method calculate the total number of common words used for phishing
     * on the site/url
     * */
    private int totalPhishingWords(String link) {
        int count = 0;

        try {
            URL url = new URL(link);
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                if (scanner.next() != null && !scanner.next().trim().isEmpty()) {
                    for (String word : Constant.PHISHING_WORDS) {
                        if (scanner.next().trim().toLowerCase().contains(word.toLowerCase())) {
                            count++;
                            LOGGER.info(
                                "Phishing word detected. [phishing word: {}, Scanned word: {}, Url: {}]",
                                word, scanner.next().trim(), link);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Unable to count phishing word: ", e);
        }

        return count;
    }


    /**
     * Check if the url is valid or not
     * */
    private boolean isUrlValid(String url) {
        try {
            new URL(url).toURI();

            HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
            int responseCode = huc.getResponseCode();
            LOGGER.error("URL Response code: {}", responseCode);
            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Invalid Url, [URL: ]", url, e);
            return false;
        }
    }


}
