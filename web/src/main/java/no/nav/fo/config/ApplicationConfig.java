package no.nav.fo.config;

import no.nav.apiapp.ApiApplication;
import no.nav.fo.internal.PingConfig;
import no.nav.fo.service.PepClient;
import no.nav.fo.service.PepClientInterface;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static no.nav.apiapp.ApiApplication.Sone.FSS;

@Configuration
@Import({
        VirksomhetEnhetEndpointConfig.class,
        ServiceConfig.class,
        CacheConfig.class,
        AbacContext.class,
        PingConfig.class
})
public class ApplicationConfig implements ApiApplication {

    @Bean
    public PepClientInterface pepClient() { return new PepClient(); }

    @Override
    public Sone getSone() {
        return FSS;
    }
}
