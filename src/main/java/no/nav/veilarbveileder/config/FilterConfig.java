package no.nav.veilarbveileder.config;

import no.nav.common.auth.oidc.filter.OidcAuthenticationFilter;
import no.nav.common.auth.oidc.filter.OidcAuthenticatorConfig;
import no.nav.common.auth.subject.IdentType;
import no.nav.common.auth.utils.ServiceUserTokenFinder;
import no.nav.common.auth.utils.UserTokenFinder;
import no.nav.common.log.LogFilter;
import no.nav.common.rest.filter.SetStandardHttpHeadersFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static no.nav.common.auth.Constants.*;
import static no.nav.common.auth.oidc.filter.OidcAuthenticator.fromConfigs;
import static no.nav.common.utils.EnvironmentUtils.isDevelopment;
import static no.nav.common.utils.EnvironmentUtils.requireApplicationName;

@Configuration
public class FilterConfig {

    private final List<String> ALLOWED_SERVICE_USERS = List.of(
            "srvveilarbfilter"
    );

    private OidcAuthenticatorConfig openAmServiceUserAuthConfig(EnvironmentProperties properties) {
        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(properties.getOpenAmDiscoveryUrl())
                .withClientId(properties.getVeilarbloginOpenAmClientId())
                .withIdTokenFinder(new ServiceUserTokenFinder())
                .withIdentType(IdentType.Systemressurs);
    }

    private OidcAuthenticatorConfig naisServiceUserAuthConfig(EnvironmentProperties properties) {
        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(properties.getNaisStsDiscoveryUrl())
                .withClientIds(ALLOWED_SERVICE_USERS)
                .withIdentType(IdentType.Systemressurs);
    }

    private OidcAuthenticatorConfig openAmAuthConfig(EnvironmentProperties properties) {
        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(properties.getOpenAmDiscoveryUrl())
                .withClientId(properties.getVeilarbloginOpenAmClientId())
                .withIdTokenCookieName(OPEN_AM_ID_TOKEN_COOKIE_NAME)
                .withRefreshTokenCookieName(REFRESH_TOKEN_COOKIE_NAME)
                .withIdTokenFinder(new UserTokenFinder())
                .withRefreshUrl(properties.getOpenAmRefreshUrl())
                .withIdentType(IdentType.InternBruker);
    }

    private OidcAuthenticatorConfig azureAdAuthConfig(EnvironmentProperties environmentProperties) {
        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(environmentProperties.getAadDiscoveryUrl())
                .withClientId(environmentProperties.getVeilarbloginAadClientId())
                .withIdTokenCookieName(AZURE_AD_ID_TOKEN_COOKIE_NAME)
                .withIdentType(IdentType.InternBruker);
    }

    @Bean
    public FilterRegistrationBean authenticationFilterRegistrationBean(EnvironmentProperties properties) {
        FilterRegistrationBean<OidcAuthenticationFilter> registration = new FilterRegistrationBean<>();
        OidcAuthenticationFilter authenticationFilter = new OidcAuthenticationFilter(
                fromConfigs(
                        openAmAuthConfig(properties),
                        azureAdAuthConfig(properties),
                        openAmServiceUserAuthConfig(properties),
                        naisServiceUserAuthConfig(properties)
                )
        );

        registration.setFilter(authenticationFilter);
        registration.setOrder(1);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean logFilterRegistrationBean() {
        FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogFilter(requireApplicationName(), isDevelopment().orElse(false)));
        registration.setOrder(2);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean setStandardHeadersFilterRegistrationBean() {
        FilterRegistrationBean<SetStandardHttpHeadersFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SetStandardHttpHeadersFilter());
        registration.setOrder(3);
        registration.addUrlPatterns("/*");
        return registration;
    }

}
