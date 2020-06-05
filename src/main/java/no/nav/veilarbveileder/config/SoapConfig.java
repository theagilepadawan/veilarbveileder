package no.nav.veilarbveileder.config;

import no.nav.common.cxf.CXFClient;
import no.nav.veilarbveileder.client.VirksomhetEnhetSoapClient;
import no.nav.veilarbveileder.client.VirksomhetEnhetSoapClientImpl;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static no.nav.common.utils.EnvironmentUtils.getRequiredProperty;

@Configuration
public class SoapConfig {

    private static final String NORG_VIRKSOMHET_ENHET_URL = "VIRKSOMHET_ENHET_V1_ENDPOINTURL";

    @Bean
    public VirksomhetEnhetSoapClient virksomhetEnhetSoapClient(Enhet virksomhetEnhet) {
        return new VirksomhetEnhetSoapClientImpl(virksomhetEnhet);
    }

    @Bean
    public Enhet virksomhetEnhet() {
        return new CXFClient<>(Enhet.class)
                .address(getRequiredProperty(NORG_VIRKSOMHET_ENHET_URL))
                .configureStsForSubject()
                .build();
    }

}
