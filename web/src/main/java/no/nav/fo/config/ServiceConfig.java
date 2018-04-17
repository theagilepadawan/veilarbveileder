package no.nav.fo.config;

import no.nav.fo.service.LdapService;
import no.nav.fo.service.OrganisasjonEnhetV2Service;
import no.nav.fo.service.VirksomhetEnhetService;
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
