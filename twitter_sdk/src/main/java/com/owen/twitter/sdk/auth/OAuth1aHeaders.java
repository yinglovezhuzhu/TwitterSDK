package com.owen.twitter.sdk.auth;

import java.util.HashMap;
import java.util.Map;

public class OAuth1aHeaders {
    public static final String HEADER_AUTH_SERVICE_PROVIDER = "X-Auth-Service-Provider";
    public static final String HEADER_AUTH_CREDENTIALS = "X-Verify-Credentials-Authorization";

    /**
     * Gets authorization header for inclusion in HTTP request headers.
     *
     * @param authConfig The auth config.
     * @param authToken The auth token to use to sign the request.
     * @param callback The callback url.
     * @param method The HTTP method.
     * @param url The url.
     * @param postParams The post parameters.
     */
    public String getAuthorizationHeader(TwitterAuthConfig authConfig,
            TwitterAccessToken authToken, String callback, String method, String url,
            Map<String, String> postParams) {
        final OAuth1aParameters oAuth1aParameters = getOAuth1aParameters(authConfig, authToken,
                callback, method, url, postParams);
        return oAuth1aParameters.getAuthorizationHeader();
    }


    /**
     * @param authConfig The auth config.
     * @param authToken  The auth token to use.
     * @param callback   The callback url.
     * @param method     The HTTP method (GET, POST, PUT, DELETE, etc).
     * @param url        The url delegation should be sent to (e.g. https://api.twitter.com/1.1/account/verify_credentials.json).
     * @param postParams The post parameters.
     * @return A map of OAuth Echo headers
     */
    public Map<String, String> getOAuthEchoHeaders(TwitterAuthConfig authConfig,
                                                   TwitterAccessToken authToken, String callback, String method, String url,
            Map<String, String> postParams) {
        final Map<String, String> headers = new HashMap<>(2);
        final String authorizationHeader = getAuthorizationHeader(authConfig, authToken,
                callback, method, url, postParams);
        headers.put(HEADER_AUTH_CREDENTIALS, authorizationHeader);
        headers.put(HEADER_AUTH_SERVICE_PROVIDER, url);
        return headers;
    }

    OAuth1aParameters getOAuth1aParameters(TwitterAuthConfig authConfig, TwitterAccessToken
            authToken, String callback, String method, String url, Map<String, String> postParams) {
        return new OAuth1aParameters(authConfig, authToken, callback, method, url, postParams);
    }
}
