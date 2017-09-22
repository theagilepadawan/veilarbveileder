package no.nav.fo.config;

import no.nav.apiapp.ApiApplication;
import no.nav.fo.internal.PingConfig;
import no.nav.fo.service.PepClientInterface;
import no.nav.fo.service.PepClientMock;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        VirksomhetEnhetEndpointConfigMock.class,
        ServiceConfig.class,
        CacheConfig.class,
        AbacContext.class,
        PingConfig.class
})
public class LocalApplicationConfig implements ApiApplication {

    @Bean
    public PepClientInterface pepClient() { return new PepClientMock(); }

    @Override
    public Sone getSone() {
        return Sone.FSS;
    }
}
