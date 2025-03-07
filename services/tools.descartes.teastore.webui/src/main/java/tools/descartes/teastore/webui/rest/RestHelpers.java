package tools.descartes.teastore.webui.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;
import tools.descartes.teastore.entities.message.SessionBlob;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RestHelpers {
    public static SessionBlob parseSessionCookie(Cookie cookie) {
        if(cookie != null) {
            ObjectMapper o = new ObjectMapper();
            try {
                SessionBlob blob = o.readValue(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8), SessionBlob.class);
                if (blob != null) {
                    return blob;
                }
            } catch (IOException e) {
                throw new IllegalStateException("Cookie corrupted!");
            }
        }
        return new SessionBlob();
    }

    public static NewCookie encodeSessionCookie(SessionBlob blob) {
        ObjectMapper o = new ObjectMapper();
        try {
            return new NewCookie("sessionBlob", URLEncoder.encode(o.writeValueAsString(blob), "UTF-8"),
                    "/", null, null, Integer.MAX_VALUE, false);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not save blob!");
        }
    }

    public static NewCookie deleteCookie(Cookie cookie) {
        if(cookie != null) {
            return new NewCookie(cookie, "", 0, false);
        }
        return null;
    }
}
