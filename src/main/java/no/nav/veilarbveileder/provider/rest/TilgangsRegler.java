package no.nav.veilarbveileder.provider.rest;

import no.nav.common.auth.SubjectHandler;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.RequestData;
import no.nav.sbl.dialogarena.common.abac.pep.domain.ResourceType;
import no.nav.sbl.dialogarena.common.abac.pep.domain.response.BiasedDecisionResponse;
import no.nav.sbl.dialogarena.common.abac.pep.domain.response.Decision;
import no.nav.sbl.dialogarena.common.abac.pep.exception.PepException;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;

import static java.lang.String.format;
import static no.nav.sbl.dialogarena.common.abac.pep.domain.response.Decision.Permit;

public class TilgangsRegler {

    private static final String MODIA = "modia";
    private static final String SYSTEMBRUKER = "srvveilarbveileder";
    private static final String DOMAIN = "veilarb";

    static void tilgangTilOppfolging(Pep pep) {

        try {
            RequestData requestData = pep.nyRequest()
                    .withDomain(MODIA)
                    .withResourceType(ResourceType.Modia);

            BiasedDecisionResponse abacResponse = pep.harTilgang(requestData);
            sjekkTilgang(ResourceType.Modia.name(), abacResponse);

        } catch (PepException e) {
            throw new RuntimeException(e);
        }
    }

    public static void tilgangTilEnhet(Pep pep, String enhet) {
        BiasedDecisionResponse abacResponse;
        try {
            abacResponse = pep.harTilgangTilEnhet(enhet, SYSTEMBRUKER, DOMAIN);
        } catch (PepException e) {
            throw new InternalServerErrorException("Something went wrong in PEP", e);
        }
        sjekkTilgang(enhet, abacResponse);
    }

    private static void sjekkTilgang(String ressurs, BiasedDecisionResponse abacResponse) {
        Decision decision = abacResponse.getBiasedDecision();
        if (!Permit.equals(decision)) {
            String ident = SubjectHandler.getIdent().orElseThrow(IllegalStateException::new);
            String message = format("Veileder %s har ikke tilgang til %s: %s", ident, ressurs, decision);
            throw new NotAuthorizedException(message, ressurs);
        }
    }
}
