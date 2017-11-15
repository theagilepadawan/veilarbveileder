package no.nav.fo.provider.rest;

import io.vavr.Tuple;
import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.fo.service.BrukertilgangService;
import no.nav.fo.service.PepClientInterface;
import no.nav.fo.util.TokenUtils;

import javax.ws.rs.NotAuthorizedException;


import static java.lang.String.format;

public class TilgangsRegler {

    static void tilgangTilOppfolging(PepClientInterface pep) {
        SubjectHandler subjectHandler = SubjectHandler.getSubjectHandler();
        String ident = subjectHandler.getUid();
        String token = TokenUtils.getTokenBody(subjectHandler.getSubject());

        test("oppfølgingsbruker", ident, pep.isSubjectMemberOfModiaOppfolging(ident, token));
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
