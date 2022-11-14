package com.phishing.app.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Client {

    public String callPostMethod(String strUrl) {

        String json = null;
        try {
            log.info("Calling Post Request [URL: {}]", strUrl);
            URL url = new URL(strUrl);
            String readLine;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();
            log.info("GET Response [URL: {}, Code: {}, Message: {}]", strUrl, responseCode,
                connection.getResponseMessage());

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    stringBuffer.append(readLine);
                }

                in.close();

                json = stringBuffer.toString();
                log.info("URL: {}, JSON Response: {}", strUrl, json);
            }
        } catch (Exception e) {
            log.error("ERROR: ", e);
        }

        return json;

    }


}