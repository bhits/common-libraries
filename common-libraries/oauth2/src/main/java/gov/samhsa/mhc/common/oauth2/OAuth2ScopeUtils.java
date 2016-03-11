package gov.samhsa.mhc.common.oauth2;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.joining;

public class OAuth2ScopeUtils {
    private OAuth2ScopeUtils() {
    }

    public static final String OR = " or ";
    public static final String AND = " and ";

    public static final String hasScope(String scope) {
        Assert.hasText(scope, "scope must have text");
        return "#oauth2.hasScope('" + scope + "')";
    }

    public static final String hasScopes(String... scopes){
        Assert.notEmpty(scopes, "scopes cannot be empty");
        Assert.noNullElements(scopes, "scopes cannot contain null elements");
        AtomicInteger counter = new AtomicInteger(0);
        final String expr =
                Arrays.stream(scopes)
                .filter(Objects::nonNull)
                .filter(StringUtils::hasText)
                .peek(scope -> counter.incrementAndGet())
                .map(OAuth2ScopeUtils::hasScope)
                .collect(joining(AND));
        Assert.isTrue(scopes.length == counter.get(), "all elements in the scope must have text");
        return expr;
    }
}
