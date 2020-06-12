package no.nav.veilarbveileder.service;

import no.nav.common.abac.Pep;
import no.nav.common.auth.subject.SsoToken;
import no.nav.common.auth.subject.SubjectHandler;
import no.nav.veilarbveileder.client.LdapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final Pep veilarbPep;
    private final LdapClient ldapClient;

    private static final String ROLLE_MODIA_ADMIN = "0000-GA-Modia_Admin";

    @Autowired
    public AuthService(Pep veilarbPep, LdapClient ldapClient) {
        this.veilarbPep = veilarbPep;
        this.ldapClient = ldapClient;
    }

    public String getInnloggetVeilederIdent() {
        return SubjectHandler
                .getIdent()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is missing from subject"));
    }

    public String getInnloggetBrukerToken() {
        return SubjectHandler
                .getSsoToken()
                .map(SsoToken::getToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is missing"));
    }

    public void tilgangTilModia() {
        if (!veilarbPep.harVeilederTilgangTilModia(getInnloggetBrukerToken())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public void tilgangTilEnhet(String enhetId) {
        String ident = getInnloggetVeilederIdent();
        if (!ldapClient.veilederHarRolle(ident, ROLLE_MODIA_ADMIN) && !veilarbPep.harVeilederTilgangTilEnhet(ident, enhetId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

}
