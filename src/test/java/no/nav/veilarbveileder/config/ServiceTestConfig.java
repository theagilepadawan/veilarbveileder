package no.nav.veilarbveileder.config;

import no.nav.veilarbveileder.service.AuthService;
import no.nav.veilarbveileder.service.VirksomhetEnhetService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AuthService.class,
        VirksomhetEnhetService.class
})
public class ServiceTestConfig {}
