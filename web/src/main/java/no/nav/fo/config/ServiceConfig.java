package no.nav.fo.config;

import no.nav.fo.service.VirksomhetEnhetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public VirksomhetEnhetService virksomhetEnhetServiceImpl() {
        return new VirksomhetEnhetService();
    }
}
