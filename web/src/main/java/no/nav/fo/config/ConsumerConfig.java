package no.nav.fo.config;

import no.nav.fo.consumer.VirksomhetEnhetConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfig {

    @Bean
    public VirksomhetEnhetConsumer virksomhetEnhetConsumer() {
        return new VirksomhetEnhetConsumer();
    }
}
