package no.nav.fo.config;

import no.nav.fo.service.PepClient;
import no.nav.fo.service.ServiceConfig;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        Pingables.class,
        VirksomhetEnhetEndpointConfig.class,
        ServiceConfig.class,
        CacheConfig.class,
        AbacContext.class
})
public class LocalApplicationConfig {

    @Bean
    public PepClient pepClient() { return new PepClient(); }

}
