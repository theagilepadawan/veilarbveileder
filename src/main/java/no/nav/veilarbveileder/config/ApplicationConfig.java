package no.nav.veilarbveileder.config;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.abac.*;
import no.nav.common.client.norg2.CachedNorg2Client;
import no.nav.common.client.norg2.Norg2Client;
import no.nav.common.client.norg2.NorgHttp2Client;
import no.nav.common.utils.Credentials;
import no.nav.veilarbveileder.client.LdapClient;
import no.nav.veilarbveileder.client.LdapClientImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import static no.nav.common.utils.NaisUtils.getCredentials;

@Slf4j
@Configuration
@EnableScheduling
@EnableConfigurationProperties({EnvironmentProperties.class})
public class ApplicationConfig {

    @Bean
    public Credentials serviceUserCredentials() {
        return getCredentials("service_user");
    }

    @Bean
    public Norg2Client norg2Client(EnvironmentProperties properties) {
        return new CachedNorg2Client(new NorgHttp2Client(properties.getNorg2Url()));
    }

    @Bean
    public Pep veilarbPep(EnvironmentProperties properties, Credentials serviceUserCredentials) {
        return new VeilarbPep(properties.getAbacUrl(), serviceUserCredentials.username, serviceUserCredentials.password);
    }

    @Bean
    public LdapClient ldapClient() {
        return new LdapClientImpl();
    }

}
