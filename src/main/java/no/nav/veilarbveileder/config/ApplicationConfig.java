package no.nav.veilarbveileder.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import no.nav.common.abac.VeilarbPep;
import no.nav.common.abac.VeilarbPepFactory;
import no.nav.common.abac.audit.SpringAuditRequestInfoSupplier;
import no.nav.common.auth.context.AuthContextHolder;
import no.nav.common.auth.context.AuthContextHolderThreadLocal;
import no.nav.common.client.axsys.AxsysClient;
import no.nav.common.client.axsys.AxsysClientImpl;
import no.nav.common.client.axsys.AxsysEnhet;
import no.nav.common.client.axsys.CachedAxsysClient;
import no.nav.common.client.nom.CachedNomClient;
import no.nav.common.client.nom.NomClient;
import no.nav.common.client.nom.NomClientImpl;
import no.nav.common.client.norg2.CachedNorg2Client;
import no.nav.common.client.norg2.Norg2Client;
import no.nav.common.client.norg2.NorgHttp2Client;
import no.nav.common.featuretoggle.UnleashClient;
import no.nav.common.featuretoggle.UnleashClientImpl;
import no.nav.common.sts.ServiceToServiceTokenProvider;
import no.nav.common.sts.utils.AzureAdServiceTokenProviderBuilder;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.common.utils.Credentials;
import no.nav.common.utils.EnvironmentUtils;
import no.nav.common.utils.UrlUtils;
import no.nav.veilarbveileder.client.LdapClient;
import no.nav.veilarbveileder.client.LdapClientImpl;
import no.nav.veilarbveileder.utils.DevNomClient;
import no.nav.veilarbveileder.utils.ModiaPep;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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
        String url = UrlUtils.createServiceUrl("axsys", "org", false);
        Cache<EnhetId, List<NavIdent>> hentAnsatteCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500)
                .build();
        Cache<NavIdent, List<AxsysEnhet>> hentTilgangerCache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();

        return new CachedAxsysClient(new AxsysClientImpl(url), hentTilgangerCache, hentAnsatteCache);
    }

    @Bean
    public VeilarbPep veilarbPep(EnvironmentProperties properties, Credentials serviceUserCredentials) {
        return VeilarbPepFactory.get(
                properties.getAbacVeilarbUrl(), serviceUserCredentials.username,
                serviceUserCredentials.password, new SpringAuditRequestInfoSupplier()
        );
    }

    @Bean
    public ModiaPep modiaPep(EnvironmentProperties properties, Credentials serviceUserCredentials) {
        var pep = VeilarbPepFactory.get(
                properties.getAbacModiaUrl(), serviceUserCredentials.username,
                serviceUserCredentials.password, new SpringAuditRequestInfoSupplier()
        );

        return new ModiaPep(pep);
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

    @Bean
    public ServiceToServiceTokenProvider serviceToServiceTokenProvider() {
        return AzureAdServiceTokenProviderBuilder.builder()
                .withEnvironmentDefaults()
                .build();
    }

    @Bean
    public NomClient nomClient() {
        if (EnvironmentUtils.isDevelopment().orElse(false)) {
            return new DevNomClient();
        }

        Supplier<String> serviceTokenSupplier = () -> serviceToServiceTokenProvider()
                .getServiceToken("nom-api", "nom", "prod-gcp");

        return new CachedNomClient(new NomClientImpl("https://nom-api.intern.nav.no", serviceTokenSupplier));
    }

}
