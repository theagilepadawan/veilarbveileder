package no.nav.veilarbveileder.service;

import no.nav.veilarbveileder.config.LdapContextProvider;
import org.springframework.cache.annotation.Cacheable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static no.nav.veilarbveileder.service.ADRolleParserKt.parseADRolle;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

public class LdapService {

    public static final String LDAP_BASEDN_PROPERTY_NAME = "LDAP_BASEDN";

    private String LDAP_BASE_DN = getRequiredProperty(LDAP_BASEDN_PROPERTY_NAME);

    @Cacheable("veilarbveilederCache")
    public boolean veilederHarRolle(String ident, String rolle) {
        NamingEnumeration<SearchResult> result = sokLDAP(ident);
        return getRoller(result).contains(rolle);
    }

    private static LdapContext ldapContext() {
        return LdapContextProvider.getInitialLdapContext();
    }

    private NamingEnumeration<SearchResult> sokLDAP(String ident) {
        String searchbase = "OU=Users,OU=NAV,OU=BusinessUnits," + LDAP_BASE_DN;
        SearchControls searchCtrl = new SearchControls();
        searchCtrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

        try {
            return ldapContext().search(searchbase, String.format("(&(objectClass=user)(CN=%s))", ident), searchCtrl);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getRoller(NamingEnumeration<SearchResult> result) {
        try {
            if (result.hasMore()) {
                SearchResult searchResult = result.next();
                NamingEnumeration<?> attributes = searchResult.getAttributes().get("memberof").getAll();
                return parseRollerFraAD(attributes);
            } else {
                return emptyList();
            }
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> parseRollerFraAD(NamingEnumeration<?> attributes) throws NamingException {
        List<String> rawRolleStrenger = new ArrayList<>();
        while (attributes.hasMore()) {
            rawRolleStrenger.add((String) attributes.next());
        }
        return parseADRolle(rawRolleStrenger);
    }
}
