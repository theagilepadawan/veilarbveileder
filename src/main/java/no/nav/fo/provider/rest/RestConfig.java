package no.nav.fo.provider.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        EnhetController.class,
        VeilederController.class
})
public class RestConfig {}
