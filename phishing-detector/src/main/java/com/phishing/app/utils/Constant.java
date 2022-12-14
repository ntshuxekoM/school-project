package com.phishing.app.utils;

import org.apache.commons.logging.Log;

public class Constant {
    public static final String APP_EMAIL="info@phishing.Detector.co.za";
    public static final String SAFE_BROWSING_URL="https://safebrowsing.googleapis.com/v4/threatMatches:find?key=AIzaSyDM8WLlpy_387_mQORKE5zS5xNc9AoN534";

    //https://blog.hubspot.com/blog/tabid/6307/bid/30684/the-ultimate-list-of-email-spam-trigger-words.aspx
    public static final String[] PHISHING_WORDS = {"DHL", "official",
        "bank", "security", "urgent", "alert", "important", "delivery",
        "ebay", "taxes", "credit", "verify", "confirm", "account",
        "bill", "immediately", "address", "telephone", "ssn", "charity",
        "check", "personal", "confidential", "atm", "warning",
        "fraud", "won", "irs", "owe", "pay"};
}
