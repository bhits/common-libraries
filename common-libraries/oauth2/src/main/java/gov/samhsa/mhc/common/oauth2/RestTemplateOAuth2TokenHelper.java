package gov.samhsa.mhc.common.oauth2;

import org.springframework.http.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

public final class RestTemplateOAuth2TokenHelper {
    private RestTemplateOAuth2TokenHelper() {
    }

    public static final void deleteWithToken(RestTemplate restTemplate, OAuth2Authentication oAuth2Authentication, String url) {
        assertArguments(restTemplate, oAuth2Authentication, url);
        final HttpEntity<?> requestEntity = createHttpRequestEntityWithToken(oAuth2Authentication);
        restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, (Class) null);
    }

    public static final <T> T getWithToken(RestTemplate restTemplate, OAuth2Authentication oAuth2Authentication, String url, Class<T> responseType) {
        assertArguments(restTemplate, oAuth2Authentication, url, responseType);
        HttpEntity<?> requestEntity = createHttpRequestEntityWithToken(oAuth2Authentication);
        final ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
        return responseEntity.getBody();
    }

    public static final <T> T postWithToken(RestTemplate restTemplate, OAuth2Authentication oAuth2Authentication, String url, Object request, Class<T> responseType) {
        assertArguments(restTemplate, oAuth2Authentication, url, request, responseType);
        HttpEntity<?> requestEntity = createHttpRequestEntityWithToken(oAuth2Authentication, request);
        final ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
        return responseEntity.getBody();
    }

    public static final <T> T putWithToken(RestTemplate restTemplate, OAuth2Authentication oAuth2Authentication, String url, Object request, Class<T> responseType) {
        assertArguments(restTemplate, oAuth2Authentication, url, request, responseType);
        HttpEntity<?> requestEntity = createHttpRequestEntityWithToken(oAuth2Authentication, request);
        final ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType);
        return responseEntity.getBody();
    }

    private static final HttpEntity<?> createHttpRequestEntityWithToken(OAuth2Authentication oAuth2Authentication) {
        HttpHeaders headers = createHttpHeadersWithToken(oAuth2Authentication);
        return new HttpEntity<>(headers);
    }

    private static final HttpEntity<?> createHttpRequestEntityWithToken(OAuth2Authentication oAuth2Authentication, Object request) {
        HttpHeaders headers = createHttpHeadersWithToken(oAuth2Authentication);
        return new HttpEntity<>(request, headers);
    }

    private static final HttpHeaders createHttpHeadersWithToken(OAuth2Authentication oAuth2Authentication) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final Optional<OAuth2AuthenticationDetails> oAuth2AuthenticationDetails = Optional.of(oAuth2Authentication)
                .map(OAuth2Authentication::getDetails)
                .filter(RestTemplateOAuth2TokenHelper::isNotNull)
                .filter(OAuth2AuthenticationDetails.class::isInstance)
                .map(OAuth2AuthenticationDetails.class::cast);
        final String tokenType = oAuth2AuthenticationDetails.map(OAuth2AuthenticationDetails::getTokenType).orElseThrow(() -> new OAuth2AuthenticationException("OAuth2 token type cannot be found"));
        final String tokenValue = oAuth2AuthenticationDetails.map(OAuth2AuthenticationDetails::getTokenValue).orElseThrow(() -> new OAuth2AuthenticationException("OAuth2 token value cannot be found"));
        headers.add("Authorization", tokenType + " " + tokenValue);
        return headers;
    }

    private static final boolean isNotNull(Object o) {
        return o != null;
    }

    private static final void assertArguments(RestTemplate restTemplate, OAuth2Authentication oAuth2Authentication, String url) {
        Assert.notNull(restTemplate, "restTemplate cannot be null");
        Assert.notNull(oAuth2Authentication, "oAuth2Authentication cannot be null");
        Assert.hasText(url, "url must have text");
    }

    private static final <T> void assertArguments(RestTemplate restTemplate, OAuth2Authentication oAuth2Authentication, String url, Class<T> responseType) {
        assertArguments(restTemplate, oAuth2Authentication, url);
        Assert.notNull(responseType, "responseType cannot be null");
    }

    private static final <T> void assertArguments(RestTemplate restTemplate, OAuth2Authentication oAuth2Authentication, String url, Object request, Class<T> responseType) {
        assertArguments(restTemplate, oAuth2Authentication, url, responseType);
        Assert.notNull(request, "request cannot be null");
    }
}
