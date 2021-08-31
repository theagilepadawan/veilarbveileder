package no.nav.veilarbveileder.config;

import no.nav.common.abac.AbacClient;
import no.nav.common.abac.VeilarbPep;
import no.nav.veilarbveileder.mock.AbacClientMock;
import no.nav.veilarbveileder.mock.VeilarbPepMock;
import no.nav.veilarbveileder.utils.ModiaPep;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@EnableConfigurationProperties({EnvironmentProperties.class})
@Import({
        SwaggerConfig.class,
        ClientTestConfig.class,
        ControllerTestConfig.class,
        ServiceTestConfig.class,
        FilterTestConfig.class
})
public class ApplicationTestConfig {

    @Bean
    public AbacClient abacClient() {
        return new AbacClientMock();
    }

    @Bean
    public VeilarbPep veilarbPep(AbacClient abacClient) {
        return new VeilarbPepMock(abacClient);
    }

    @Bean
    public ModiaPep modiapep(AbacClient abacClient) {
        return new ModiaPep(new VeilarbPepMock(abacClient));
    }

}
