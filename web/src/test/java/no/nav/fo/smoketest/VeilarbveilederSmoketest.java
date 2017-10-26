package no.nav.fo.smoketest;

import lombok.extern.slf4j.Slf4j;
import no.nav.dialogarena.smoketest.SmoketestException;
import no.nav.dialogarena.smoketest.SmoketestFSS;
import no.nav.dialogarena.smoketest.SmoketestFSS.SmoketestFSSConfig;
import no.nav.fo.domene.EnheterResponse;
import no.nav.fo.domene.PortefoljeEnhet;
import no.nav.fo.domene.Veileder;
import no.nav.fo.domene.VeiledereResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.System.getProperty;
import static no.nav.dialogarena.smoketest.SmoketestUtils.appOrLocalhost;
import static no.nav.dialogarena.smoketest.Tag.SMOKETEST;
import static no.nav.sbl.rest.RestUtils.withClient;

@Slf4j
@Tag(SMOKETEST)
public class VeilarbveilederSmoketest {

    private static SmoketestFSS smoketestFSS;
    private static String VEILARBVEILEDER = "veilarbveileder";
    private static String VEILARBVEILEDER_API = "veilarbveileder/api/";
    private static String ENHETER_URL;
    private static String VEILEDERINFO_FOR_IDENT_URL;
    private static String ME_URL;
    private static String VEILEDERE_URL;
    private static String hostname;

    @BeforeAll
    public static void setup() {
        System.setProperty("miljo", "t4");
        hostname = appOrLocalhost(getProperty("miljo"));
        SmoketestFSSConfig config = new SmoketestFSSConfig(VEILARBVEILEDER);
        smoketestFSS = new SmoketestFSS(config);
        configureUrls();
    }

    @Test
    public void skalHenteEnheter() {
        hentEnheter();
    }

    @Test
    public void skalHenteInformasjonOmInnloggetVeileder() {
        hentMe();
    }

    @Test
    public void skalHenteVeiledereForAlleTilgjengeligeEnheter() {
        List<PortefoljeEnhet> enheter = hentEnheter();
        enheter.stream().map(PortefoljeEnhet::getEnhetId).forEach(VeilarbveilederSmoketest::hentVeiledereForEnhet);
    }

    @Test
    public void skalHenteVeilederinfoForIdenter() {
        String enhet = hentEnheter().get(0).getEnhetId();
        log.info("Henter informasjon om alle veildere på enhet {}" , enhet);
        List<Veileder> veilederListe = hentVeiledereForEnhet(enhet).getVeilederListe();
        veilederListe.stream().map(Veileder::getIdent).forEach(VeilarbveilederSmoketest::hentVeilederinfoForIdent);

    }


    private static List<PortefoljeEnhet> hentEnheter() {
        log.info("Henter enheter for veileder {} - url: {}", smoketestFSS.getInnloggetVeielder(), ENHETER_URL);

        List<PortefoljeEnhet> enheter = withClient(client -> client.target(ENHETER_URL)
                .request()
                .cookie(smoketestFSS.getTokenCookie())
                .get(EnheterResponse.class)).getEnhetliste();
        if(enheter.isEmpty()) {
            throw new SmoketestException("Veileder " + smoketestFSS.getInnloggetVeielder() + " har ingen enheter i norg");
        }
        return enheter;
    }

    private static Veileder hentMe() {
        log.info("Henter info {} - url: {}", smoketestFSS.getInnloggetVeielder(), VEILEDERE_URL);
        return withClient(client -> client.target(ME_URL)
                .request()
                .cookie(smoketestFSS.getTokenCookie())
                .get(Veileder.class));
    }

    private static VeiledereResponse hentVeiledereForEnhet(String enhet) {
        String url = String.format(VEILEDERE_URL, enhet);
        log.info("Henter veiledere for enhet {} - url: {}", enhet, url);

        return withClient(client -> client.target(url)
                .request()
                .cookie(smoketestFSS.getTokenCookie())
                .get(VeiledereResponse.class));
    }

    private static Veileder hentVeilederinfoForIdent(String ident) {
        String url = String.format(VEILEDERINFO_FOR_IDENT_URL, ident);
        log.info("Henter info {} - url: {}", ident, url);
        return withClient(client -> client.target(url)
                .request()
                .cookie(smoketestFSS.getTokenCookie())
                .get(Veileder.class));
    }

    private static void configureUrls() {
        ENHETER_URL = hostname + VEILARBVEILEDER_API + "veileder/enheter";
        ME_URL = hostname + VEILARBVEILEDER_API + "veileder/me";
        VEILEDERINFO_FOR_IDENT_URL = hostname + VEILARBVEILEDER_API + "veileder/%s";
        VEILEDERE_URL = hostname + VEILARBVEILEDER_API + "enhet/%s/veiledere";
    }

}
