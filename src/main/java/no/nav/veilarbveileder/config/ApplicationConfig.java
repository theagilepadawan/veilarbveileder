package no.nav.veilarbveileder.config;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.brukerdialog.security.domain.IdentType;
import no.nav.common.oidc.auth.OidcAuthenticatorConfig;
import no.nav.veilarbveileder.provider.rest.RestConfig;
import no.nav.veilarbveileder.service.LdapHelsesjekk;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static no.nav.common.oidc.Constants.OPEN_AM_ID_TOKEN_COOKIE_NAME;
import static no.nav.common.oidc.Constants.REFRESH_TOKEN_COOKIE_NAME;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Configuration
@Import({
        VirksomhetEnhetEndpointConfig.class,
        ServiceConfig.class,
        CacheConfig.class,
        AbacContext.class,
        RestConfig.class,
        LdapHelsesjekk.class,
        LdapContextProvider.class,
        OrganisasjonEnhetV2Config.class,
        ConsumerConfig.class
})
public class ApplicationConfig implements ApiApplication {

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator
                .sts()
                .enableCXFSecureLogs()
                .addOidcAuthenticator(createOpenAmAuthenticatorConfig())
                .addOidcAuthenticator(createSystemUserAuthenticatorConfig());
    }

    private OidcAuthenticatorConfig createSystemUserAuthenticatorConfig() {
        String discoveryUrl = getRequiredProperty("SECURITY_TOKEN_SERVICE_DISCOVERY_URL");
        String clientId = getRequiredProperty("SECURITY_TOKEN_SERVICE_CLIENT_ID");

        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(discoveryUrl)
                .withClientId(clientId)
                .withIdentType(IdentType.Systemressurs);
    }

    private OidcAuthenticatorConfig createOpenAmAuthenticatorConfig() {
        String discoveryUrl = getRequiredProperty("OPENAM_DISCOVERY_URL");
        String clientId = getRequiredProperty("VEILARBLOGIN_OPENAM_CLIENT_ID");
        String refreshUrl = getRequiredProperty("VEILARBLOGIN_OPENAM_REFRESH_URL");

        return new OidcAuthenticatorConfig()
                .withDiscoveryUrl(discoveryUrl)
                .withClientId(clientId)
                .withRefreshUrl(refreshUrl)
                .withRefreshTokenCookieName(REFRESH_TOKEN_COOKIE_NAME)
                .withIdTokenCookieName(OPEN_AM_ID_TOKEN_COOKIE_NAME)
                .withIdentType(IdentType.InternBruker);
    }
}
