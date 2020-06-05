package no.nav.veilarbveileder.service;

import no.nav.common.abac.Pep;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing ident from subject"));
    }

    public void tilgangTilOppfolging() {
//        private static final String MODIA = "modia";
//        private static final String SYSTEMBRUKER = "srvveilarbveileder";
//        private static final String DOMAIN = "veilarb";

//        try {
//            RequestData requestData = pep.nyRequest()
//                    .withDomain(MODIA)
//                    .withResourceType(ResourceType.Modia);
//
//            BiasedDecisionResponse abacResponse = pep.harTilgang(requestData);
//            sjekkTilgang(ResourceType.Modia.name(), abacResponse);
//
//        } catch (PepException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void tilgangTilEnhet(String enhetId) {
        veilarbPep.sjekkVeilederTilgangTilEnhet(getInnloggetVeilederIdent(), enhetId);
    }

}
