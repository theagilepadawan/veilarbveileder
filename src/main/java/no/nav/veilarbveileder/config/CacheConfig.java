package no.nav.veilarbveileder.config;


import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

import static no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext.ABAC_CACHE;


@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    String cacheSecondsString = System.getProperty("cache.config.seconds", "3600");
    int cacheSecondsInt = Integer.parseInt(cacheSecondsString);

    @Bean
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("veilarbveilederCache");
        cacheConfiguration.setMaxEntriesLocalHeap(10000);
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration.setTimeToIdleSeconds(6000);
        cacheConfiguration.setTimeToLiveSeconds(cacheSecondsInt);
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfiguration);
        config.addCache(ABAC_CACHE);

        return net.sf.ehcache.CacheManager.newInstance(config);
    }


    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }

    static class CustomKeyGenerator extends SimpleKeyGenerator {
        @Override
        public Object generate(Object target, Method method, Object... params) {
            return String.format("%s.%s(%s)", target.getClass().getName(), method.getName(), super.generate(target, method, params));
        }
    }
}
