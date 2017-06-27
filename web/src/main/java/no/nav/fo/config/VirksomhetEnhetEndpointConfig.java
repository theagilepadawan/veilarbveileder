package no.nav.fo.config;

import no.nav.sbl.dialogarena.common.cxf.CXFClient;
import no.nav.sbl.dialogarena.types.Pingable;
import no.nav.sbl.dialogarena.types.Pingable.Ping.PingMetadata;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.metrics.MetricsFactory.createTimerProxyForWebService;

@Configuration
public class VirksomhetEnhetEndpointConfig {


    @Bean
    public Enhet virksomhetEnhet() {
        return createTimerProxyForWebService("enhet_v1", new CXFClient<>(Enhet.class)
                .address(System.getProperty("norg.virksomhet_enhet.url"))
                .configureStsForOnBehalfOfWithJWT()
                .build(), Enhet.class);
    }

    @Bean
    public Pingable virksomhetEnhetPing() {
        Enhet virksomhetEnhet = new CXFClient<>(Enhet.class)
                .address(System.getProperty("norg.virksomhet_enhet.url"))
                .configureStsForSystemUserInFSS()
                .build();

        PingMetadata metadata = new PingMetadata(
                "VirksomhetEnhet_v1 via " + System.getProperty("norg.virksomhet_enhet.url"),
                "Ping mot VirksomhetEnhet (NORG).",
                true
        );

        return () -> {
            try {
                virksomhetEnhet.ping();
                return Pingable.Ping.lyktes(metadata);
            } catch (Exception e) {
                return Pingable.Ping.feilet(metadata, e);
            }
        };
    }
}
