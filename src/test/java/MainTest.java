import no.nav.brukerdialog.security.Constants;
import no.nav.brukerdialog.tools.SecurityConstants;
import no.nav.dialogarena.config.fasit.FasitUtils;
import no.nav.dialogarena.config.fasit.LdapConfig;
import no.nav.dialogarena.config.fasit.ServiceUser;
import no.nav.dialogarena.config.fasit.dto.RestService;
import no.nav.fo.config.LdapContextProvider;
import no.nav.fo.config.OrganisasjonEnhetV2Config;
import no.nav.fo.config.VirksomhetEnhetEndpointConfig;
import no.nav.fo.service.LdapService;
import no.nav.sbl.dialogarena.common.abac.pep.service.AbacServiceConfig;
import no.nav.sbl.dialogarena.common.cxf.StsSecurityConstants;
import no.nav.testconfig.ApiAppTest;

import static no.nav.dialogarena.config.fasit.FasitUtils.Zone.FSS;
import static no.nav.dialogarena.config.fasit.FasitUtils.getDefaultEnvironment;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;
import static no.nav.sbl.util.EnvironmentUtils.Type.SECRET;
import static no.nav.sbl.util.EnvironmentUtils.*;

public class MainTest {

    private static final String APPLICATION_NAME = "veilarbveileder";

    public static void main(String[] args) {
        ApiAppTest.setupTestContext(ApiAppTest.Config.builder()
                .applicationName(APPLICATION_NAME)
                .build()
        );

        String securityTokenService = FasitUtils.getBaseUrl("securityTokenService", FSS);
        ServiceUser srvveilarbveileder = FasitUtils.getServiceUser("srvveilarbveileder", APPLICATION_NAME);

        setProperty(resolveSrvUserPropertyName(), srvveilarbveileder.username, PUBLIC);
        setProperty(resolverSrvPasswordPropertyName(), srvveilarbveileder.password, PUBLIC);

        setProperty(StsSecurityConstants.STS_URL_KEY, securityTokenService, PUBLIC);
        setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, srvveilarbveileder.getUsername(), PUBLIC);
        setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, srvveilarbveileder.getPassword(), SECRET);

        LdapConfig ldapConfig = FasitUtils.getLdapConfig();
        setProperty(LdapService.LDAP_BASEDN_PROPERTY_NAME, ldapConfig.baseDN, PUBLIC);
        setProperty(LdapContextProvider.LDAP_URL, ldapConfig.url, PUBLIC);
        setProperty(LdapContextProvider.LDAP_USERNAME, ldapConfig.username, PUBLIC);
        setProperty(LdapContextProvider.LDAP_PASSWORD, ldapConfig.password, SECRET);

        setProperty(OrganisasjonEnhetV2Config.NORG2_ORGANISASJONENHET_V2_URL, FasitUtils.getWebServiceEndpoint("virksomhet:OrganisasjonEnhet_v2").url, PUBLIC);
        setProperty(VirksomhetEnhetEndpointConfig.NORG_VIRKSOMHET_ENHET_URL, FasitUtils.getWebServiceEndpoint("virksomhet:Enhet_v1").url, PUBLIC);

        String issoHost = FasitUtils.getBaseUrl("isso-host");
        String issoJWS = FasitUtils.getBaseUrl("isso-jwks");
        String issoISSUER = FasitUtils.getBaseUrl("isso-issuer");
        String issoIsAlive = FasitUtils.getBaseUrl("isso.isalive", FSS);
        ServiceUser isso_rp_user = FasitUtils.getServiceUser("isso-rp-user", APPLICATION_NAME);
        String loginUrl = FasitUtils.getBaseUrl("veilarblogin.redirect-url", FSS);
        setProperty(Constants.ISSO_HOST_URL_PROPERTY_NAME, issoHost, PUBLIC);
        setProperty(Constants.ISSO_RP_USER_USERNAME_PROPERTY_NAME, isso_rp_user.getUsername(), PUBLIC);
        setProperty(Constants.ISSO_RP_USER_PASSWORD_PROPERTY_NAME, isso_rp_user.getPassword(), SECRET);
        setProperty(Constants.ISSO_JWKS_URL_PROPERTY_NAME, issoJWS, PUBLIC);
        setProperty(Constants.ISSO_ISSUER_URL_PROPERTY_NAME, issoISSUER, PUBLIC);
        setProperty(Constants.ISSO_ISALIVE_URL_PROPERTY_NAME, issoIsAlive, PUBLIC);
        setProperty(SecurityConstants.SYSTEMUSER_USERNAME, srvveilarbveileder.getUsername(), PUBLIC);
        setProperty(SecurityConstants.SYSTEMUSER_PASSWORD, srvveilarbveileder.getPassword(), SECRET);
        setProperty(Constants.OIDC_REDIRECT_URL_PROPERTY_NAME, loginUrl, PUBLIC);

        RestService abacEndpoint = FasitUtils.getRestService("abac.pdp.endpoint", getDefaultEnvironment());
        setProperty(AbacServiceConfig.ABAC_ENDPOINT_URL_PROPERTY_NAME, abacEndpoint.getUrl(), PUBLIC);

        Main.main(new String[]{"9591"});
    }

}