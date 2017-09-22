package no.nav.fo.config;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.ServletUtil;
import no.nav.fo.provider.rest.RestConfig;
import no.nav.fo.service.PepClient;
import no.nav.fo.service.PepClientInterface;
import no.nav.sbl.dialogarena.common.abac.pep.context.AbacContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@Import({
        VirksomhetEnhetEndpointConfig.class,
        ServiceConfig.class,
        CacheConfig.class,
        AbacContext.class,
        RestConfig.class
})
public class ApplicationConfig implements ApiApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    @Bean
    public PepClientInterface pepClient() { return new PepClient(); }

    @Override
    public Sone getSone() {
        return Sone.FSS;
    }

    @Override
    public void startup(ServletContext servletContext) {
        forwardTjenesterTilApi(servletContext);
    }

    // Bakoverkompatibilitet for klienter som går mot /tjenester
    public static void forwardTjenesterTilApi(ServletContext servletContext) {
        GenericServlet genericServlet = new GenericServlet() {
            @Override
            public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
                HttpServletRequest request = (HttpServletRequest) req;
                String requestURI = request.getRequestURI();
                String relativPath = requestURI.substring(request.getContextPath().length() + request.getServletPath().length() + 1);
                String apiPath = DEFAULT_API_PATH + relativPath;
                LOGGER.warn("bakoverkompatibilitet: {} -> {}", requestURI, apiPath);
                RequestDispatcher requestDispatcher = req.getRequestDispatcher(apiPath);
                requestDispatcher.forward(req, res);
            }
        };
        ServletUtil.leggTilServlet(servletContext, genericServlet, "/tjenester/*");
    }

}
