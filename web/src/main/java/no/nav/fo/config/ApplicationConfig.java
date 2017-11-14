package no.nav.fo.config;

import no.nav.apiapp.ApiApplication;
import no.nav.fo.provider.rest.RestConfig;
import no.nav.fo.service.PepClient;
import no.nav.fo.service.PepClientInterface;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        VirksomhetEnhetEndpointConfig.class,
        ServiceConfig.class,
        CacheConfig.class,
        AbacContext.class,
        RestConfig.class
})
public class ApplicationConfig implements ApiApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    @Bean
    public PepClientInterface pepClient() { return new PepClient(); }

    @Override
    public Sone getSone() {
        return Sone.FSS;
    }
}
