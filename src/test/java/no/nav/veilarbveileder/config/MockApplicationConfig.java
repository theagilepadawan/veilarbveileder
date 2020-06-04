package no.nav.veilarbveileder.config;

import no.nav.veilarbveileder.consumer.VirksomhetEnhetConsumer;
import no.nav.veilarbveileder.service.LdapService;
import no.nav.veilarbveileder.service.OrganisasjonEnhetV2Service;
import no.nav.veilarbveileder.service.VirksomhetEnhetService;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.OrganisasjonEnhetV2;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;


@Configuration
public class MockApplicationConfig {

    @Bean
    public VirksomhetEnhetService virksomhetEnhetService() { return new VirksomhetEnhetService(); }

    @Bean
    public Enhet virksomhetEnhet() { return mock(Enhet.class);}

    @Bean
    public LdapService ldapService() { return mock(LdapService.class); }

    @Bean
    public OrganisasjonEnhetV2Service organisasjonEnhetV2Service() {
        return mock(OrganisasjonEnhetV2Service.class);
    }

    @Bean
    public OrganisasjonEnhetV2 organisasjonEnhetV2() {
        return mock(OrganisasjonEnhetV2.class);
    }

    @Bean
    public VirksomhetEnhetConsumer virksomhetEnhetConsumer() {
        return new VirksomhetEnhetConsumer();
    }
}
