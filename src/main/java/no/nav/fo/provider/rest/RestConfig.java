package no.nav.fo.provider.rest;

import no.nav.fo.provider.rest.logger.JSLoggerController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        JSLoggerController.class,
        EnhetController.class,
        VeilederController.class
})
public class RestConfig {}
