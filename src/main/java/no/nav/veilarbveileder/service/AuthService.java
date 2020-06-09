package no.nav.veilarbveileder.service;

import no.nav.common.abac.Pep;
import no.nav.common.auth.subject.SsoToken;
import no.nav.common.auth.subject.SubjectHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final Pep veilarbPep;

    @Autowired
    public AuthService(Pep veilarbPep) {
        this.veilarbPep = veilarbPep;
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
        veilarbPep.sjekkTilgangTilModia(getInnloggetBrukerToken());
    }

    public void tilgangTilEnhet(String enhetId) {
        veilarbPep.sjekkTilgangTilEnhet(getInnloggetVeilederIdent(), enhetId);
    }

}
