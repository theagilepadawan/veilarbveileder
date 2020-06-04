package no.nav.veilarbveileder.config;

import no.nav.veilarbveileder.service.LdapService;
import no.nav.veilarbveileder.service.OrganisasjonEnhetV2Service;
import no.nav.veilarbveileder.service.VirksomhetEnhetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public VirksomhetEnhetService virksomhetEnhetServiceImpl() {
        return new VirksomhetEnhetService();
    }

    @Bean
    public LdapService ldapService() {
        return new LdapService();
    }

    @Bean
    public OrganisasjonEnhetV2Service organisasjonEnhetV2Service() {
        return new OrganisasjonEnhetV2Service();
    }
}
