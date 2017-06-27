package no.nav.fo.internal;


import no.nav.sbl.dialogarena.types.Pingable;
import no.nav.sbl.dialogarena.types.Pingable.Ping.PingMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Configuration
public class PingConfig {

    @Bean
    public Pingable issoPing() throws IOException {
        PingMetadata metadata = new PingMetadata(
                "ISSO via " + System.getProperty("isso.isalive.url"),
                "Sjekker isAlive til ISSO, single signon løsning.",
                true
        );

        return () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(System.getProperty("isso.isalive.url")).openConnection();
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    return Pingable.Ping.lyktes(metadata);
                }
                return Pingable.Ping.feilet(metadata, "IsAlive returnerte statuskode: " + connection.getResponseCode());
            } catch (Exception e) {
                return Pingable.Ping.feilet(metadata, e);
            }
        };
    }

}
