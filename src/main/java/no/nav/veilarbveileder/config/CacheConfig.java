package no.nav.veilarbveileder.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String VEILEDER_ROLLE_CACHE_NAME = "veilederRolleCache";

    @Bean
    public Cache veilederRolleCache() {
        return new CaffeineCache(VEILEDER_ROLLE_CACHE_NAME, Caffeine.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build());
    }

}
