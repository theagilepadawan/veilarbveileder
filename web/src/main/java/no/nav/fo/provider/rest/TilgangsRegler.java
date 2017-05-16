package no.nav.fo.provider.rest;

import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.fo.service.PepClientInterface;
import no.nav.fo.util.TokenUtils;

import javax.ws.rs.NotAuthorizedException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class TilgangsRegler {
    final static Pattern pattern = Pattern.compile("\\d{4}");

    static void tilgangTilOppfolging(PepClientInterface pep) {
        SubjectHandler subjectHandler = SubjectHandler.getSubjectHandler();
        String ident = subjectHandler.getUid();
        String token = TokenUtils.getTokenBody(subjectHandler.getSubject());

        test("oppfølgingsbruker", ident, pep.isSubjectMemberOfModiaOppfolging(ident, token));
    }

    public static boolean enhetErIPilot(String enhet) {
        String enhetsliste = System.getProperty("portefolje.pilot.enhetliste", "");
        enhetsliste = pattern.matcher(enhetsliste).find() ? enhetsliste : "";

        if (isBlank(enhetsliste)) {
            return true;
        }

        List<String> pilotenheter = Arrays.asList(enhetsliste.split(","));

        return pilotenheter.isEmpty() || pilotenheter.contains(enhet);
    }

    public static void tilgangTilPilot(String enhet) {
        test("pilotenhet", enhet, enhetErIPilot(enhet));
    }

    private static void test(String navn, Object data, boolean matches) {
        if (!matches) {
            throw new NotAuthorizedException(format("sjekk av %s feilet, %s", navn, data));
        }
    }
}
