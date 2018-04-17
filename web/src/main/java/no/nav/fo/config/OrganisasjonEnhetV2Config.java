package no.nav.fo.config;

import no.nav.sbl.dialogarena.common.cxf.CXFClient;
import no.nav.sbl.dialogarena.types.Pingable;
import no.nav.sbl.dialogarena.types.Pingable.Ping.PingMetadata;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.binding.OrganisasjonEnhetV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.metrics.MetricsFactory.createTimerProxyForWebService;
import static no.nav.sbl.dialogarena.types.Pingable.Ping;

@Configuration
public class OrganisasjonEnhetV2Config {

    @Bean
    public OrganisasjonEnhetV2 organisasjonEnhetV2() {
        return createTimerProxyForWebService("OrganisasjonEnhetV2", new CXFClient<>(OrganisasjonEnhetV2.class)
                .address(System.getProperty("norg2.organisasjonenhet.v2.url"))
                .configureStsForSystemUserInFSS()
                .build(), OrganisasjonEnhetV2.class);
    }

    @Bean
    public Pingable organisasjonEnhetV2Ping() {
        OrganisasjonEnhetV2 organisasjonEnhetV2 = new CXFClient<>(OrganisasjonEnhetV2.class)
                .address(System.getProperty("norg2.organisasjonenhet.v2.url"))
                .configureStsForSystemUserInFSS()
                .build();

        PingMetadata metadata = new Ping.PingMetadata(
                "NORG2 - OrganisasjonEnhetV2 via " + System.getProperty("norg2.organisasjonenhet.v2.url"),
                "Ping mot OrganisasjonenhetV2 (Norg2).",
                true
        );

        return () -> {
            try {
                organisasjonEnhetV2.ping();
                return Pingable.Ping.lyktes(metadata);
            } catch (Exception e) {
                return Pingable.Ping.feilet(metadata, e);
            }
        };
    }
}
