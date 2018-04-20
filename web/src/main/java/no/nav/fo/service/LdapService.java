package no.nav.fo.service;

import no.nav.fo.config.LdapContextProvider;
import org.springframework.cache.annotation.Cacheable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getProperty;
import static java.util.stream.Collectors.toList;
import static no.nav.fo.service.ADRolleParserKt.parseADRolle;

public class LdapService {

    @Cacheable("veilarbveilederCache")
    public boolean veilederHarRolle(String ident, String rolle) {
        NamingEnumeration<SearchResult> result = sokLDAP(ident);
        return getRoller(result).contains(rolle);
    }

    private static LdapContext ldapContext() {
        return LdapContextProvider.getInitialLdapContext();
    }

    private NamingEnumeration<SearchResult> sokLDAP(String ident) {
        String searchbase = "OU=Users,OU=NAV,OU=BusinessUnits," + getProperty("ldap.basedn");
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
            NamingEnumeration<?> attributes = result.next().getAttributes().get("memberof").getAll();
            return parseRollerFraAD(attributes);
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
