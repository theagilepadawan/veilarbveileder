package no.nav.veilarbveileder.config;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.client.axsys.AxsysClient;
import no.nav.common.client.nom.NomClient;
import no.nav.common.client.norg2.Norg2Client;
import no.nav.common.health.selftest.SelfTestCheck;
import no.nav.common.health.selftest.SelfTestChecks;
import no.nav.common.health.selftest.SelfTestMeterBinder;
import no.nav.veilarbveileder.client.LdapClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class HealthCheckConfig {

    @Bean
    public SelfTestChecks selfTestChecks(
            LdapClient ldapClient,
            Norg2Client norg2Client,
            NomClient nomClient,
            AxsysClient axsysClient
    ) {
        List<SelfTestCheck> selfTestChecks = Arrays.asList(
                new SelfTestCheck("Ldap sjekk", true, ldapClient),
                new SelfTestCheck("Ping mot norg2 REST API", true, norg2Client),
                new SelfTestCheck("Ping mot NOM", true, nomClient),
                new SelfTestCheck("Ping mot Axsys", true, axsysClient)
        );

        return new SelfTestChecks(selfTestChecks);
    }

    @Bean
    public SelfTestMeterBinder selfTestMeterBinder(SelfTestChecks selfTestChecks) {
        return new SelfTestMeterBinder(selfTestChecks);
    }

}
