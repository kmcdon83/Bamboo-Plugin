package com.cx.plugin.utils;

import com.cx.restclient.exception.CxClientException;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class HttpHelper {

    public static final String HTTP_HOST = System.getProperty("http.proxyHost");
    public static final String HTTP_PORT = System.getProperty("http.proxyPort");
    public static final String HTTP_USERNAME = System.getProperty("http.proxyUser");
    public static final String HTTP_PASSWORD = System.getProperty("http.proxyPassword");

    public static final String HTTPS_HOST = System.getProperty("https.proxyHost");
    public static final String HTTPS_PORT = System.getProperty("https.proxyPort");
    public static final String HTTPS_USERNAME = System.getProperty("https.proxyUser");
    public static final String HTTPS_PASSWORD = System.getProperty("https.proxyPassword");

    public static Proxy getHttpProxy() {
        Proxy proxy = null;
        Authenticator authenticator;
        if (isNotEmpty(HTTPS_HOST) && isNotEmpty(HTTPS_PORT)) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(HTTPS_HOST, Integer.parseInt(HTTPS_PORT)));
            if (isNotEmpty(HTTPS_USERNAME) && isNotEmpty(HTTPS_PASSWORD)) {
                authenticator = new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return (new PasswordAuthentication(HTTPS_USERNAME, HTTPS_PASSWORD.toCharArray()));
                    }
                };
                Authenticator.setDefault(authenticator);
            }
        } else if (isNotEmpty(HTTP_HOST) && isNotEmpty(HTTP_PORT)) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(HTTP_HOST, Integer.parseInt(HTTP_PORT)));
            if (isNotEmpty(HTTP_USERNAME) && isNotEmpty(HTTP_PASSWORD)) {
                authenticator = new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return (new PasswordAuthentication(HTTP_USERNAME, HTTP_PASSWORD.toCharArray()));
                    }
                };
                Authenticator.setDefault(authenticator);
            }
        }

        return proxy;
    }

    public static SSLSocketFactory getSSLSocketFactory() throws CxClientException {
        TrustStrategy acceptingTrustStrategy = new TrustAllStrategy();
        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new CxClientException("Fail to set trust all certificate, 'SSLConnectionSocketFactory'", e);
        }
        return sslContext.getSocketFactory();
    }

    public static HostnameVerifier getHostnameVerifier() throws CxClientException {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

}
