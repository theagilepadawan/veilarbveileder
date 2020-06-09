package no.nav.veilarbveileder.config;

import no.nav.common.abac.AbacClient;
import no.nav.common.abac.Pep;
import no.nav.common.health.HealthCheck;
import no.nav.common.health.HealthCheckResult;
import no.nav.veilarbveileder.mock.AbacClientMock;
import no.nav.veilarbveileder.mock.PepMock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static no.nav.veilarbveileder.config.SoapConfig.VIRKSOMHET_ENHET_HEALTH_CHECK;

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
    public Pep veilarbPep(AbacClient abacClient) {
        return new PepMock(abacClient);
    }

    @Bean(VIRKSOMHET_ENHET_HEALTH_CHECK)
    public HealthCheck virksomhetEnhetHealthCheck() {
        return HealthCheckResult::healthy;
    }

}
