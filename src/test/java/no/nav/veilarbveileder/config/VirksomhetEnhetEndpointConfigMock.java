package no.nav.veilarbveileder.config;

import no.nav.veilarbveileder.mock.EnhetMock;
import no.nav.sbl.dialogarena.types.Pingable;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.sbl.dialogarena.types.Pingable.Ping;

@Configuration
public class VirksomhetEnhetEndpointConfigMock {

    @Bean
    public Enhet virksomhetEnhet() {
        return new EnhetMock();
    }

    @Bean
    public Pingable virksomhetEnhetPing() {
        Enhet virksomhetEnhet = new EnhetMock();

        Ping.PingMetadata metadata = new Ping.PingMetadata(
                "Mock av VirksomhetEnhet",
                "Ping mot VirksomhetEnhet (NORG).",
                true
        );

        return () -> {
            try {
                virksomhetEnhet.ping();
                return Ping.lyktes(metadata);
            } catch (Exception e) {
                return Ping.feilet(metadata, e);
            }
        };
    }
}
