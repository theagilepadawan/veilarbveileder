package no.nav.veilarbveileder.config;

import no.nav.veilarbveileder.service.AuthService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AuthService.class,
})
public class ServiceTestConfig {}
