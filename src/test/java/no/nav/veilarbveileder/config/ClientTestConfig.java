package no.nav.veilarbveileder.config;

import no.nav.common.client.norg2.CachedNorg2Client;
import no.nav.common.client.norg2.Norg2Client;
import no.nav.common.client.norg2.NorgHttp2Client;
import no.nav.common.health.HealthCheckResult;
import no.nav.veilarbveileder.client.LdapClient;
import no.nav.veilarbveileder.client.VirksomhetEnhetSoapClient;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Ressurs;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static no.nav.veilarbveileder.utils.TestData.*;

@Configuration
public class ClientTestConfig {

    @Bean
    public Norg2Client norg2Client() {
        return new Norg2Client() {
            @Override
            public List<no.nav.common.client.norg2.Enhet> alleAktiveEnheter() {
                return Collections.emptyList();
            }

            @Override
            public no.nav.common.client.norg2.Enhet hentEnhet(String s) {
                return null;
            }

            @Override
            public no.nav.common.client.norg2.Enhet hentTilhorendeEnhet(String s) {
                return null;
            }

            @Override
            public HealthCheckResult checkHealth() {
                return HealthCheckResult.healthy();
            }
        };
    }

    @Bean
    public LdapClient ldapClient() {
        return new LdapClient() {
            @Override
            public boolean veilederHarRolle(String ident, String rolle) {
                return false;
            }

            @Override
            public HealthCheckResult checkHealth() {
                return HealthCheckResult.healthy();
            }
        };
    }

    @Bean
    public VirksomhetEnhetSoapClient virksomhetEnhetSoapClient() {
        return new VirksomhetEnhetSoapClient() {
            @Override
            public WSHentRessursListeResponse hentEnhetInfo(String enhetId) {
                Enhet enhet = new Enhet().withEnhetId(enhetId);
                List<Ressurs> ressursliste = new ArrayList<>();
                ressursliste.add(createRessurs("Arne","And","XX11111"));
                ressursliste.add(createRessurs("Jens Bjarne","Olsen","XX22222"));
                ressursliste.add(createRessurs("Donald","Duck","XX33333"));
                return new WSHentRessursListeResponse().withEnhet(enhet).withRessursListe(ressursliste);
            }

            @Override
            public WSHentEnhetListeResponse hentVeilederInfo(String ident) {
                Ressurs ressurs = createRessurs("Arne","And", ident);
                List<no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet> enhetliste = new ArrayList<>();
                enhetliste.add(createEnhet("0713","NAV SANDE"));
                enhetliste.add(createEnhet("0104","NAV MOSS"));
                return new WSHentEnhetListeResponse().withEnhetListe(enhetliste).withRessurs(ressurs);
            }

            @Override
            public HealthCheckResult checkHealth() {
                return HealthCheckResult.healthy();
            }
        };
    }

    private Ressurs createRessurs(String fornavn, String etternavn, String ressursId) {
        return new Ressurs()
                .withFornavn(fornavn)
                .withEtternavn(etternavn)
                .withNavn(fornavn + " " + etternavn)
                .withRessursId(ressursId);
    }

    private no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet createEnhet(String enhetId, String enhetNavn) {
        return new no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet().withNavn(enhetNavn).withEnhetId(enhetId);
    }

}
