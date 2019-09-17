package no.nav.fo.provider.rest;

import no.nav.brukerdialog.security.context.SubjectHandler;
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

    static void tilgangTilOppfolging(Pep pep) {
        SubjectHandler subjectHandler = SubjectHandler.getSubjectHandler();
        String ident = subjectHandler.getUid();

        try {
            RequestData requestData = pep.nyRequest()
                    .withDomain("modia")
                    .withResourceType(ResourceType.Modia);

            BiasedDecisionResponse abacResponse = pep.harTilgang(requestData);
            sjekkTilgang(ResourceType.Modia.name(), ident, abacResponse);

        } catch (PepException e) {
            throw new RuntimeException(e);
        }
    }

    static void tilgangTilEnhet(Pep pep, String enhet) {
        String veilederId = SubjectHandler.getSubjectHandler().getUid();
        tilgangTilEnhet(pep, enhet, veilederId);
    }

    private static void tilgangTilEnhet(Pep pep, String enhet, String ident) {
        BiasedDecisionResponse abacResponse;
        try {
            abacResponse = pep.harTilgangTilEnhet(enhet, "srvveilarbveileder", "veilarb");
        } catch (PepException e) {
            throw new InternalServerErrorException("Something went wrong in PEP", e);
        }
        sjekkTilgang(enhet, ident, abacResponse);
    }

    private static void sjekkTilgang(String ressurs, String ident, BiasedDecisionResponse abacResponse) {
        Decision decision = abacResponse.getBiasedDecision();
        if (!Permit.equals(decision)) {
            String message = format("Veileder %s har ikke tilgang til %s: %s", ident, ressurs, decision);
            throw new NotAuthorizedException(message, ressurs);
        }
    }
}
