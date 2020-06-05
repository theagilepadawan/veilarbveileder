package no.nav.veilarbveileder.config;

import no.nav.veilarbveileder.controller.EnhetController;
import no.nav.veilarbveileder.controller.InternalController;
import no.nav.veilarbveileder.controller.VeilederController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        VeilederController.class,
        EnhetController.class,
        InternalController.class
})
public class ControllerTestConfig {}
