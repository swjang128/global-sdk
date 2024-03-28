package io.snplab.gsdk.common.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.OffsetDateTime;


public class MailUtil {

    final static String RESET_IMAGE = "https://cdn.v3.dev.myd.world/snplab/sdk/reset.png";
    final static String LOGO_IMAGE = "https://cdn.v3.dev.myd.world/snplab/sdk/logo-white.svg";

    public static JSONObject getData(String email) throws JSONException {
        return new JSONObject()
                .put("email", email)
                .put("expiration", OffsetDateTime.now().withNano(0).plusHours(6).toString());
    }

    public static String getPasswordChangeLink(HttpServletRequest request, String base64EncryptedData) {
        return MailUtil.getServerUrl(request) + "/password-reset?enc=" + base64EncryptedData;
    }

    public static String getServerUrl(HttpServletRequest request) {
        return request.getRequestURL().toString().split(request.getRequestURI())[0];
    }

    public static String generateHtmlContent(String email, String passwordChangeLink, String filename) throws IOException {
        String filePath = "static/email/" + filename + ".html";
        Resource resource = new ClassPathResource(filePath);
        String htmlFile = htmlFile = new String(resource.getInputStream().readAllBytes());

        return htmlFile
                .replace("{logoImg}", LOGO_IMAGE)
                .replace("{resetImg}", RESET_IMAGE)
                .replace("{email}", email)
                .replace("{url}", passwordChangeLink);
    }
}
