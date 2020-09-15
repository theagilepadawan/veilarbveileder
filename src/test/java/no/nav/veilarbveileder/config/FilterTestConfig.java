package no.nav.veilarbveileder.config;

import no.nav.common.auth.context.UserRole;
import no.nav.common.test.auth.TestAuthContextFilter;
import no.nav.veilarbveileder.utils.TestData;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterTestConfig {

    @Bean
    public FilterRegistrationBean testSubjectFilterRegistrationBean() {
        FilterRegistrationBean<TestAuthContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TestAuthContextFilter(UserRole.INTERN, TestData.TEST_VEILEDER_IDENT));
        registration.setOrder(1);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

}
