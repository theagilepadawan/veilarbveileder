package no.nav.fo.config;

import no.nav.fo.service.ServiceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        Pingables.class,
        VirksomhetEnhetEndpointConfigMock.class,
        ServiceConfig.class,
        CacheConfig.class
})
public class LocalApplicationConfig {

}
