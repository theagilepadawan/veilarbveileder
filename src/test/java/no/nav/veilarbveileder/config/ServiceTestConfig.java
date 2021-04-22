package no.nav.veilarbveileder.config;

import no.nav.veilarbveileder.service.AuthService;
import no.nav.veilarbveileder.service.VeilederOgEnhetServiceV1;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AuthService.class,
        VeilederOgEnhetServiceV1.class
})
public class ServiceTestConfig {}
