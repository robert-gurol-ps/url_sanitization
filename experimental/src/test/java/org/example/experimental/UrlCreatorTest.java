package org.example.experimental;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class UrlCreatorTest {

    private static final UrlCreator sut = new UrlCreator();
    private static final String SQL_INJECTION_PAYLOAD = "Robert'); DROP TABLE Students;--";

    @ParameterizedTest
    @MethodSource("niceInputs")
    public void createUrlForDomainOrIp_onNiceInputs_worksAsExpected(String domainOrIp, String expected) {
        String actual = sut.createUrlForDomainOrIp(domainOrIp);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("injectionInputs")
    public void createUrlForDomainOrIp_onInjectionInputs_sanitizes(String domainOrIp, String expected) {
        String actual = sut.createUrlForDomainOrIp(domainOrIp);

        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> injectionInputs() {
        return Stream.of(
                arguments("example.org?orderBy=" + SQL_INJECTION_PAYLOAD + "&someParamToMakeItAValidUrl=",
                        // looks similar, but the URL has been "defused" by escaping things...?
                        "https://example.org%3ForderBy=Robert');%20DROP%20TABLE%20Students;--&someParamToMakeItAValidUrl=/some/url/path")
        );
    }

    @ParameterizedTest
    @MethodSource("niceInputs")
    public void createUrlForDomainOrIpNaive_onNiceInputs_worksAsExpected(String domainOrIp, String expected) {
        String actual = sut.createUrlForDomainOrIpNaive(domainOrIp);

        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> niceInputs() {
        return Stream.of(
                arguments("example.org", "https://example.org/some/url/path"),
                arguments("192.168.0.1", "https://192.168.0.1/some/url/path"),
                arguments("::1", "https://[::1]/some/url/path")
        );
    }
}