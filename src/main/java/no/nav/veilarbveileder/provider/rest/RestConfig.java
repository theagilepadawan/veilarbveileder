package no.nav.veilarbveileder.provider.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        EnhetController.class,
        VeilederController.class
})
public class RestConfig {}
