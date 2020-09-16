package no.nav.veilarbveileder.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.abac.Pep;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.auth.context.UserRole;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.veilarbveileder.client.LdapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class AuthService {

    private final Pep veilarbPep;
    private final LdapClient ldapClient;

    public static final String ROLLE_MODIA_ADMIN = "0000-GA-Modia_Admin";

    @Autowired
    public AuthService(Pep veilarbPep, LdapClient ldapClient) {
        this.veilarbPep = veilarbPep;
        this.ldapClient = ldapClient;
    }

    public NavIdent getInnloggetVeilederIdent() {
        return AuthContextHolder.getNavIdent().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NAV ident is missing"));
    }

    public String getInnloggetBrukerToken() {
        return AuthContextHolder.getIdTokenString().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is missing"));
    }

    public void sjekkErSystemBruker() {
        if (AuthContextHolder.requireRole() != UserRole.SYSTEM) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Kun systembruker har tilgang");
        }
    }
    public void sjekkTilgangTilModia() {
        if (!veilarbPep.harVeilederTilgangTilModia(getInnloggetBrukerToken())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ikke tilgang til modia");
        }
    }

    public void sjekkVeilederTilgangTilEnhet(EnhetId enhetId) {
        NavIdent ident = getInnloggetVeilederIdent();
        if (!harModiaAdminRolle(ident) && !veilarbPep.harVeilederTilgangTilEnhet(ident, enhetId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ikke tilgang til enhet");
        }
    }

    public boolean harModiaAdminRolle(NavIdent ident) {
        return ldapClient.veilederHarRolle(ident, ROLLE_MODIA_ADMIN);
    }

}
