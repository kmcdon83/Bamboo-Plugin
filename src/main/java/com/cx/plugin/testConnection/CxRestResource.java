package com.cx.plugin.testConnection;


import com.cx.plugin.testConnection.dto.TestConnectionResponse;
import com.cx.restclient.CxShragaClient;
import com.cx.restclient.dto.Team;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.sast.dto.Preset;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cx.plugin.utils.CxParam.*;
import static com.cx.plugin.utils.CxPluginUtils.decrypt;

/**
 * A resource of message.
 */
@Path("/")
public class CxRestResource {

    private List<Preset> presets;
    private List<Team> teams;
    private CxShragaClient shraga;
    private String result = "";
    private Logger logger = LoggerFactory.getLogger(CxRestResource.class);


    @POST
    @Path("test/connection")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response testConnection(Map<Object, Object> data) {

        TestConnectionResponse tcResponse;
        result = "";
        URL url;
        String urlToCheck;
        int statusCode = 400;
        URLConnection urlConn;
        urlToCheck = StringUtils.defaultString(data.get("url"));

        try {
            UrlValidator urlValidator = new UrlValidator();
            if(!urlValidator.isValid(urlToCheck)){
                return getInvalidUrlResponse(statusCode);
            }

            url = new URL(urlToCheck);
            urlConn=url.openConnection();
            if (url.getProtocol().equalsIgnoreCase("https")) {
                ((HttpsURLConnection) urlConn).setSSLSocketFactory(getSSLSocketFactory());
                ((HttpsURLConnection) urlConn).setHostnameVerifier(getHostnameVerifier());
            }
            //HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

            urlConn.connect();
        } catch (Exception e) {
            return getInvalidUrlResponse(statusCode);
        }

        String username = StringUtils.defaultString(data.get("username"));
        String pas = StringUtils.defaultString(data.get("pas"));

        try {
            if (loginToServer(url, username, decrypt(pas))) {
                try {
                    teams = shraga.getTeamList();
                } catch (Exception e) {
                    throw new Exception(CONNECTION_FAILED_COMPATIBILITY + "\nError: " + e.getMessage());
                }
                presets = shraga.getPresetList();
                if (presets == null || teams == null) {
                    throw new Exception("invalid preset teamPath");
                }
                result = "Connection successful";
                tcResponse = new TestConnectionResponse(result, presets, teams);
                statusCode = 200;

            } else {
                result = result.contains("Failed to authenticate") ? "Failed to authenticate" : result;
                result = result.startsWith("Login failed.") ? result : "Login failed. " + result;
                tcResponse = getTCFailedResponse();
            }
        } catch (Exception e) {
            result = "Fail to login: " + e.getMessage();
            tcResponse = getTCFailedResponse();
        }
        return Response.status(statusCode).entity(tcResponse).build();
    }

    private Response getInvalidUrlResponse(int statusCode) {
        TestConnectionResponse tcResponse;
        result = "Invalid URL";
        tcResponse = new TestConnectionResponse(result, null, null);
        return Response.status(statusCode).entity(tcResponse).build();
    }
    private static SSLSocketFactory getSSLSocketFactory() throws CxClientException {
        TrustStrategy acceptingTrustStrategy = new TrustAllStrategy();
        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new CxClientException("Fail to set trust all certificate, 'SSLConnectionSocketFactory'", e);
        }
        return sslContext.getSocketFactory();
    }

    private static HostnameVerifier getHostnameVerifier() throws CxClientException {
        return (hostname, session) -> true;
    }
    @NotNull
    private TestConnectionResponse getTCFailedResponse() {
        presets = new ArrayList<Preset>() {{
            new Preset(NO_PRESET_ID, NO_PRESET_MESSAGE);
        }};
        teams = new ArrayList<Team>() {{
            new Team(NO_TEAM_PATH, NO_TEAM_MESSAGE);
        }};

        return new TestConnectionResponse(result, presets, teams);
    }

    private boolean loginToServer(URL url, String username, String pd) {
        try {
            shraga = new CxShragaClient(url.toString().trim(), username, pd, CX_ORIGIN, true, logger);
            shraga.login();

            return true;
        } catch (Exception CxClientException) {
            result = CxClientException.getMessage();
            return false;
        }
    }
}