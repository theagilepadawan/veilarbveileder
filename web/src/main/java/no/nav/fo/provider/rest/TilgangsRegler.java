package no.nav.fo.provider.rest;

import io.vavr.Tuple;
import lombok.SneakyThrows;
import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.fo.service.BrukertilgangService;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.RequestData;
import no.nav.sbl.dialogarena.common.abac.pep.domain.ResourceType;

import javax.ws.rs.NotAuthorizedException;

import static java.lang.String.format;
import static no.nav.sbl.dialogarena.common.abac.pep.domain.response.Decision.Permit;

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

    static void tilgangTilEnhet(BrukertilgangService brukertilgangService, String enhet) {
        String veilederId = SubjectHandler.getSubjectHandler().getUid();
        tilgangTilEnhet(brukertilgangService, enhet, veilederId);
    }

    private static void tilgangTilEnhet(BrukertilgangService brukertilgangService, String enhet, String ident) {
        test("tilgang til enhet", Tuple.of(enhet, ident), brukertilgangService.harBrukerTilgang(ident, enhet));
    }

    private static void test(String navn, Object data, boolean matches) {
        if (!matches) {
            throw new NotAuthorizedException(format("sjekk av %s feilet, %s", navn, data));
        }
    }
}
