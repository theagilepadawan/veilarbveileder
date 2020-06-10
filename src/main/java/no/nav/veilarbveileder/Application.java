package no.nav.veilarbveileder;

import lombok.SneakyThrows;
import no.nav.common.cxf.StsSecurityConstants;
import no.nav.common.utils.Credentials;
import no.nav.common.utils.SslUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.SSLContext;
import java.security.SecureRandom;

import static no.nav.common.utils.EnvironmentUtils.getRequiredProperty;
import static no.nav.veilarbveileder.utils.ServiceUserUtils.getServiceUserCredentials;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
        Credentials serviceUser = getServiceUserCredentials();

        //CXF
        System.setProperty(StsSecurityConstants.STS_URL_KEY, getRequiredProperty("SECURITYTOKENSERVICE_URL"));
        System.setProperty(StsSecurityConstants.SYSTEMUSER_USERNAME, serviceUser.username);
        System.setProperty(StsSecurityConstants.SYSTEMUSER_PASSWORD, serviceUser.password);

        downgradeTls();
        SslUtils.setupTruststore();
        SpringApplication.run(Application.class, args);
    }

    // LDAP fungerer ikke med TLS v1.3 som er default i Java 11
    @SneakyThrows
    private static void downgradeTls() {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, new SecureRandom());
        SSLContext.setDefault(sslContext);
    }

}
