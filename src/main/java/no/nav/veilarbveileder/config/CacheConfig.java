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

    public static final String HENT_ENHET_INFO_CACHE_NAME = "hentEnhetInfoCache";
    public static final String VEILEDER_INFO_CACHE_NAME = "hentVeilederInfoCache";
    public static final String VEILEDER_ROLLE_CACHE_NAME = "veilederRolleCache";

    @Bean
    public Cache enhetInfoCache() {
        return new CaffeineCache(HENT_ENHET_INFO_CACHE_NAME, Caffeine.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .maximumSize(1000)
                .build());
    }

    @Bean
    public Cache veilederInfoCache() {
        return new CaffeineCache(VEILEDER_INFO_CACHE_NAME, Caffeine.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build());
    }

    @Bean
    public Cache veilederRolleCache() {
        return new CaffeineCache(VEILEDER_ROLLE_CACHE_NAME, Caffeine.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build());
    }

}
