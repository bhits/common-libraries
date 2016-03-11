package gov.samhsa.mhc.common.oauth2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

import static gov.samhsa.mhc.common.unit.matcher.ArgumentMatchers.matching;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RestTemplateOAuth2TokenHelperTest {


    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String MOCK_URL = "MOCK_URL";
    private static final String MOCK_TOKEN_TYPE = "MOCK_TOKEN_TYPE";
    private static final String MOCK_TOKEN_VALUE = "MOCK_TOKEN_VALUE";
    private static final ArgumentMatcher<HttpEntity> matchingToken =
            matching((HttpEntity httpEntity) ->
                    httpEntity.getHeaders().get(AUTHORIZATION_HEADER_KEY)
                            .get(0).equals(MOCK_TOKEN_TYPE + " " + MOCK_TOKEN_VALUE));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private RestTemplate restTemplateMock;
    @Mock
    private OAuth2Authentication oauth2AuthenticationMock;

    private Class responseTypeMock = this.getClass();
    @Mock
    private ResponseEntity responseMock;
    @Mock
    private Object responseObjectMock;


    @Before
    public void setup() {
        OAuth2AuthenticationDetails oauth2Details = mock(OAuth2AuthenticationDetails.class);
        when(oauth2AuthenticationMock.getDetails()).thenReturn(oauth2Details);
        when(oauth2Details.getTokenType()).thenReturn(MOCK_TOKEN_TYPE);
        when(oauth2Details.getTokenValue()).thenReturn(MOCK_TOKEN_VALUE);
        when(responseMock.getBody()).thenReturn(responseObjectMock);
    }

    @Test
    public void testDeleteWithToken() throws Exception {
        // Arrange

        // Act
        RestTemplateOAuth2TokenHelper
                .deleteWithToken(restTemplateMock, oauth2AuthenticationMock, MOCK_URL);
        // Assert
        verify(restTemplateMock, times(1))
                .exchange(eq(MOCK_URL), eq(HttpMethod.DELETE),
                        argThat(matchingToken), eq((Class) null));
    }

    @Test
    public void testDeleteWithToken_Throws_OAuth2AuthenticationException_OAuth2Authentication_Is_Null() throws Exception {
        // Arrange
        thrown.expect(OAuth2AuthenticationException.class);

        // Act
        RestTemplateOAuth2TokenHelper
                .deleteWithToken(restTemplateMock, null, MOCK_URL);
    }

    @Test
    public void testDeleteWithToken_Throws_IllegalArgumentException_When_RestTemplate_Is_Null() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        RestTemplateOAuth2TokenHelper
                .deleteWithToken(null, oauth2AuthenticationMock, MOCK_URL);
    }

    @Test
    public void testDeleteWithToken_Throws_IllegalArgumentException_When_Url_Not_Have_Text() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        RestTemplateOAuth2TokenHelper
                .deleteWithToken(restTemplateMock, oauth2AuthenticationMock, " ");
    }

    @Test
    public void testDeleteWithToken_Throws_IllegalArgumentException_When_Url_Is_Null() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        RestTemplateOAuth2TokenHelper
                .deleteWithToken(restTemplateMock, oauth2AuthenticationMock, null);
    }

    @Test
    public void testGetWithToken() throws Exception {
        // Arrange
        when(restTemplateMock.exchange(eq(MOCK_URL), eq(HttpMethod.GET),
                argThat(matchingToken), eq(responseTypeMock))).thenReturn(responseMock);
        // Act
        final Object responseObject = RestTemplateOAuth2TokenHelper
                .getWithToken(restTemplateMock, oauth2AuthenticationMock, MOCK_URL, responseTypeMock);

        // Assert
        assertEquals(responseObjectMock, responseObject);
        verify(restTemplateMock, times(1))
                .exchange(eq(MOCK_URL), eq(HttpMethod.GET),
                        argThat(matchingToken), eq(responseTypeMock));
    }

    @Test
    public void testGetWithToken_Throws_IllegalArgumentException_When_Response_Type_Is_Null() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        final Object responseObject = RestTemplateOAuth2TokenHelper
                .getWithToken(restTemplateMock, oauth2AuthenticationMock, MOCK_URL, null);
    }

    @Test
    public void testPostWithToken() throws Exception {
        // Arrange
        Object requestMock = mock(Object.class);
        when(restTemplateMock.exchange(eq(MOCK_URL), eq(HttpMethod.POST),
                argThat(matchingToken), eq(responseTypeMock))).thenReturn(responseMock);
        // Act
        final Object responseObject = RestTemplateOAuth2TokenHelper
                .postWithToken(restTemplateMock, oauth2AuthenticationMock, MOCK_URL, requestMock, responseTypeMock);

        // Assert
        assertEquals(responseObjectMock, responseObject);
        verify(restTemplateMock, times(1))
                .exchange(eq(MOCK_URL), eq(HttpMethod.POST),
                        argThat(matchingToken), eq(responseTypeMock));
    }

    @Test
    public void testPostWithToken_Throws_IllegalArgumentException_When_Request_Object_Is_Null() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        final Object responseObject = RestTemplateOAuth2TokenHelper
                .postWithToken(restTemplateMock, oauth2AuthenticationMock, MOCK_URL, null, responseTypeMock);
    }

    @Test
    public void testPutWithToken() throws Exception {
        // Arrange
        Object requestMock = mock(Object.class);
        when(restTemplateMock.exchange(eq(MOCK_URL), eq(HttpMethod.PUT),
                argThat(matchingToken), eq(responseTypeMock))).thenReturn(responseMock);
        // Act
        final Object responseObject = RestTemplateOAuth2TokenHelper
                .putWithToken(restTemplateMock, oauth2AuthenticationMock, MOCK_URL, requestMock, responseTypeMock);

        // Assert
        assertEquals(responseObjectMock, responseObject);
        verify(restTemplateMock, times(1))
                .exchange(eq(MOCK_URL), eq(HttpMethod.PUT),
                        argThat(matchingToken), eq(responseTypeMock));
    }
}