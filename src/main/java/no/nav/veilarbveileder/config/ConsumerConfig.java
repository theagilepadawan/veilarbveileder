package no.nav.veilarbveileder.config;

import no.nav.veilarbveileder.consumer.VirksomhetEnhetConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfig {

    @Bean
    public VirksomhetEnhetConsumer virksomhetEnhetConsumer() {
        return new VirksomhetEnhetConsumer();
    }
}
