package no.nav.veilarbveileder.config;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.abac.Pep;
import no.nav.common.abac.VeilarbPepFactory;
import no.nav.common.abac.audit.SpringAuditRequestInfoSupplier;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.auth.context.AuthContextHolderThreadLocal;
import no.nav.common.client.axsys.AxsysClient;
import no.nav.common.client.axsys.AxsysClientImpl;
import no.nav.common.client.axsys.CachedAxsysClient;
import no.nav.common.client.norg2.CachedNorg2Client;
import no.nav.common.client.norg2.Norg2Client;
import no.nav.common.client.norg2.NorgHttp2Client;
import no.nav.common.featuretoggle.UnleashClient;
import no.nav.common.featuretoggle.UnleashClientImpl;
import no.nav.common.utils.Credentials;
import no.nav.veilarbveileder.client.LdapClient;
import no.nav.veilarbveileder.client.LdapClientImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import static no.nav.common.utils.EnvironmentUtils.isProduction;
import static no.nav.veilarbveileder.utils.ServiceUserUtils.getServiceUserCredentials;

@Slf4j
@Configuration
@EnableScheduling
@EnableConfigurationProperties({EnvironmentProperties.class})
public class ApplicationConfig {

    public final static String APPLICATION_NAME = "veilarbveileder";

    @Bean
    public Credentials credentials() {
        return getServiceUserCredentials();
    }

    @Bean
    public Norg2Client norg2Client(EnvironmentProperties properties) {
        return new CachedNorg2Client(new NorgHttp2Client(properties.getNorg2Url()));
    }

    @Bean
    public AxsysClient axsysClient(){
        String url = (isProduction().orElse(false)) ? "https://axsys.intern.nav.no" : "https://axsys.dev.intern.nav.no";
        AxsysClientImpl axsysClient = new AxsysClientImpl(url);
        return new CachedAxsysClient(axsysClient);
    }

    @Bean
    public Pep pep(EnvironmentProperties properties, Credentials serviceUserCredentials) {
        return VeilarbPepFactory.get(
                properties.getAbacUrl(), serviceUserCredentials.username,
                serviceUserCredentials.password, new SpringAuditRequestInfoSupplier()
        );
    }

    @Bean
    public LdapClient ldapClient() {
        return new LdapClientImpl();
    }

    @Bean
    public AuthContextHolder authContextHolder() {
        return AuthContextHolderThreadLocal.instance();
    }

    @Bean
    public UnleashClient unleashClient(EnvironmentProperties properties) {
        return new UnleashClientImpl(properties.getUnleashUrl(), APPLICATION_NAME);
    }

}
