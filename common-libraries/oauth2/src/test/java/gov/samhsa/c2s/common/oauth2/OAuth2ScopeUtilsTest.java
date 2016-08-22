package gov.samhsa.c2s.common.oauth2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class OAuth2ScopeUtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testHasScope() throws Exception {
        // Arrange
        final String MOCK_SCOPE = "MOCK_SCOPE";
        final String expectedExpr = "#oauth2.hasScope('MOCK_SCOPE')";

        // Act
        final String expr = OAuth2ScopeUtils.hasScope(MOCK_SCOPE);

        // Assert
        assertEquals(expectedExpr, expr);
    }

    @Test
    public void testHasScope_Throws_IllegalArgumentException_When_Scope_Not_Have_Text() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        final String expr = OAuth2ScopeUtils.hasScope(" ");
    }

    @Test
    public void testHasScopes() throws Exception {
        // Arrange
        final String MOCK_SCOPE1 = "MOCK_SCOPE1";
        final String MOCK_SCOPE2 = "MOCK_SCOPE2";
        final String expectedExpr = "#oauth2.hasScope('MOCK_SCOPE1') and #oauth2.hasScope('MOCK_SCOPE2')";

        // Act
        final String expr = OAuth2ScopeUtils.hasScopes(MOCK_SCOPE1, MOCK_SCOPE2);

        // Assert
        assertEquals(expectedExpr, expr);
    }

    @Test
    public void testHasScopes_Throws_IllegalArgumentException_When_Scopes_Null() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        final String expr = OAuth2ScopeUtils.hasScopes(null);
    }

    @Test
    public void testHasScopes_Throws_IllegalArgumentException_When_Multiple_Scopes_Null() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        final String expr = OAuth2ScopeUtils.hasScopes(null, null);
    }

    @Test
    public void testHasScopes_Throws_IllegalArgumentException_When_Scopes_Not_Have_Text() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        final String expr = OAuth2ScopeUtils.hasScopes("", " ");
    }

    @Test
    public void testHasScopes_Throws_IllegalArgumentException_When_One_Scope_Not_Have_Text() throws Exception {
        // Arrange
        thrown.expect(IllegalArgumentException.class);

        // Act
        final String expr = OAuth2ScopeUtils.hasScopes("", "someScope");
    }
}