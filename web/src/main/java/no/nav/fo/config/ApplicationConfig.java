package no.nav.fo.config;

import no.nav.apiapp.ApiApplication;
import no.nav.fo.provider.rest.RestConfig;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        VirksomhetEnhetEndpointConfig.class,
        ServiceConfig.class,
        CacheConfig.class,
        AbacContext.class,
        RestConfig.class,
        LdapContextProvider.class,
        OrganisasjonEnhetV2Config.class,
        ConsumerConfig.class
})
public class ApplicationConfig implements ApiApplication {

    @Override
    public String getApplicationName() {
        return "veilarbveileder";
    }
}
