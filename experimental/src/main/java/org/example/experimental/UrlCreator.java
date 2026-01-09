package org.example.experimental;

import com.google.common.net.InetAddresses;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;

public class UrlCreator {

    private static final String URL_SCHEME_HTTPS = "https";
    private static final String URL_PROTOCOL_PREFIX = URL_SCHEME_HTTPS + "://";
    private static final String URL_PATH = "/some/url/path";

    public String createUrlForDomainOrIp(String domainOrIpRaw) {
        String domainOrIp = surroundWithBracketsIfIpv6(domainOrIpRaw);
        return UriComponentsBuilder.newInstance()
                .scheme(URL_SCHEME_HTTPS)
                .host(domainOrIp)
                .path(URL_PATH)
                .toUriString();
    }

    public String createUrlForDomainOrIpNaive(String domainOrIpRaw) {
        String domainOrIp = surroundWithBracketsIfIpv6(domainOrIpRaw);
        return URL_PROTOCOL_PREFIX + domainOrIp + URL_PATH;
    }

    private String surroundWithBracketsIfIpv6(String domainOrIp) {
        if (InetAddresses.isInetAddress(domainOrIp)) {
            // does something, but don't sanitize
            InetAddress address = InetAddresses.forString(domainOrIp);
            return InetAddresses.toUriString(address);
        }
        return domainOrIp;
    }
}
