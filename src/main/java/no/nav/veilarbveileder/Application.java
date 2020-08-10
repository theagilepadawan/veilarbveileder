package no.nav.veilarbveileder;

import lombok.SneakyThrows;
import no.nav.common.utils.SslUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.SSLContext;
import java.security.SecureRandom;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
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
