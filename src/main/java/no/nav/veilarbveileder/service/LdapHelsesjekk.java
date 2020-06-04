package no.nav.veilarbveileder.service;

import no.nav.apiapp.selftest.Helsesjekk;
import no.nav.apiapp.selftest.HelsesjekkMetadata;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static no.nav.veilarbveileder.config.LdapContextProvider.LDAP_URL;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Component
public class LdapHelsesjekk implements Helsesjekk {

    public static final String LDAP_BASEDN_PROPERTY_NAME = "LDAP_BASEDN";
    private static final String LDAP_BASE_DN = getRequiredProperty(LDAP_BASEDN_PROPERTY_NAME);

    private final HelsesjekkMetadata helsesjekkMetadata = new HelsesjekkMetadata("ldap", getRequiredProperty(LDAP_URL), "Gjør et oppslag mot ldap med dummy-bruker", true);

    private final LdapService ldapService;

    @Inject
    public LdapHelsesjekk(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @Override
    public void helsesjekk() throws Throwable {
        if(ldapService.veilederHarRolle("dummy", "dummy")){
            throw new IllegalStateException();
        }
    }

    @Override
    public HelsesjekkMetadata getMetadata() {
        return helsesjekkMetadata;
    }

}
