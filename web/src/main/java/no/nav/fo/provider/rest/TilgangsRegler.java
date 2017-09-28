package no.nav.fo.provider.rest;

import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.fo.service.PepClientInterface;
import no.nav.fo.util.TokenUtils;

import javax.ws.rs.NotAuthorizedException;

import java.util.regex.Pattern;

import static java.lang.String.format;

public class TilgangsRegler {
    final static Pattern pattern = Pattern.compile("\\d{4}");

    static void tilgangTilOppfolging(PepClientInterface pep) {
        SubjectHandler subjectHandler = SubjectHandler.getSubjectHandler();
        String ident = subjectHandler.getUid();
        String token = TokenUtils.getTokenBody(subjectHandler.getSubject());

        test("oppfølgingsbruker", ident, pep.isSubjectMemberOfModiaOppfolging(ident, token));
    }

    private static void test(String navn, Object data, boolean matches) {
        if (!matches) {
            throw new NotAuthorizedException(format("sjekk av %s feilet, %s", navn, data));
        }
    }
}
