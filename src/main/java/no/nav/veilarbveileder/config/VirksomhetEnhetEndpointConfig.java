package no.nav.veilarbveileder.config;

import no.nav.sbl.dialogarena.common.cxf.CXFClient;
import no.nav.sbl.dialogarena.types.Pingable;
import no.nav.sbl.dialogarena.types.Pingable.Ping.PingMetadata;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.metrics.MetricsFactory.createTimerProxyForWebService;
import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Configuration
public class VirksomhetEnhetEndpointConfig {

    public static final String NORG_VIRKSOMHET_ENHET_URL = "VIRKSOMHET_ENHET_V1_ENDPOINTURL";

    @Bean
    public Enhet virksomhetEnhet() {
        return createTimerProxyForWebService("enhet_v1", new CXFClient<>(Enhet.class)
                .address(resolveVirksohetEnhetUrl())
                .configureStsForOnBehalfOfWithJWT()
                .build(), Enhet.class);
    }

    @Bean
    public Pingable virksomhetEnhetPing() {
        Enhet virksomhetEnhet = new CXFClient<>(Enhet.class)
                .address(resolveVirksohetEnhetUrl())
                .configureStsForSystemUserInFSS()
                .build();

        PingMetadata metadata = new PingMetadata(
                "VirksomhetEnhet_v1 via " + resolveVirksohetEnhetUrl(),
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

    private String resolveVirksohetEnhetUrl() {
        return getRequiredProperty(NORG_VIRKSOMHET_ENHET_URL);
    }

}
