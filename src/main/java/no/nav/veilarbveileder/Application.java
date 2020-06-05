package no.nav.veilarbveileder;

import no.nav.common.cxf.StsSecurityConstants;
import no.nav.common.utils.Credentials;
import no.nav.common.utils.SslUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static no.nav.common.utils.EnvironmentUtils.getRequiredProperty;
import static no.nav.common.utils.NaisUtils.getCredentials;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
        Credentials serviceUser = getCredentials("service_user");

        //CXF
        System.setProperty(StsSecurityConstants.STS_URL_KEY, getRequiredProperty("SECURITYTOKENSERVICE_URL"));
        System.setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, serviceUser.username);
        System.setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, serviceUser.password);

        SslUtils.setupTruststore();
        SpringApplication.run(Application.class, args);
    }

}
