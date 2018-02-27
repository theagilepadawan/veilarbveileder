package no.nav.fo.provider.rest;

import io.vavr.Tuple;
import lombok.SneakyThrows;
import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.RequestData;
import no.nav.sbl.dialogarena.common.abac.pep.domain.ResourceType;
import no.nav.sbl.dialogarena.common.abac.pep.domain.response.BiasedDecisionResponse;
import no.nav.sbl.dialogarena.common.abac.pep.exception.PepException;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;

import static java.lang.String.format;
import static no.nav.sbl.dialogarena.common.abac.pep.domain.ResourceType.Enhet;
import static no.nav.sbl.dialogarena.common.abac.pep.domain.request.Action.ActionId.READ;
import static no.nav.sbl.dialogarena.common.abac.pep.domain.response.Decision.Permit;
import static no.nav.sbl.dialogarena.common.abac.pep.utils.SecurityUtils.getSamlToken;

public class TilgangsRegler {

    @SneakyThrows
    static void tilgangTilOppfolging(Pep pep) {
        SubjectHandler subjectHandler = SubjectHandler.getSubjectHandler();
        String ident = subjectHandler.getUid();

        RequestData requestData = pep.nyRequest()
                .withDomain("modia")
                .withResourceType(ResourceType.Modia);

        test("oppfølgingsbruker", ident, pep.harTilgang(requestData).getBiasedDecision() == Permit);
    }

    static void tilgangTilEnhet(Pep pep, String enhet) {
        String veilederId = SubjectHandler.getSubjectHandler().getUid();
        tilgangTilEnhet(pep, enhet, veilederId);
    }

    private static void tilgangTilEnhet(Pep pep, String enhet, String ident) {
        BiasedDecisionResponse callAllowed;
        try {
            callAllowed = pep.harTilgang(lagRequest(pep, enhet, ident));
        } catch (PepException e) {
            throw new InternalServerErrorException("Something went wrong in PEP", e);
        }
        test("tilgang til enhet", Tuple.of(enhet, ident), Permit.equals(callAllowed.getBiasedDecision()));
    }

    private static RequestData lagRequest(Pep pep, String enhet, String ident) throws PepException {
        return pep.nyRequest()
                .withFnr(ident)
                .withEnhet(enhet)
                .withAction(READ)
                .withResourceType(Enhet)
                .withSamlToken(getSamlToken().orElse(null));
    }

    private static void test(String navn, Object data, boolean matches) {
        if (!matches) {
            throw new NotAuthorizedException(format("sjekk av %s feilet, %s", navn, data));
        }
    }
}
