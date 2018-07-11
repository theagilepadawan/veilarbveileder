package no.nav.fo.provider.rest.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/logging")
public class JSLoggerController {
    Logger LOGGER = LoggerFactory.getLogger("frontendlog");

    @POST
    @Consumes(APPLICATION_JSON)
    public void log(LogLinje logLinje) {
        switch (logLinje.level) {
            case "INFO":
                LOGGER.info(logLinje.toString());
                break;
            case "WARN":
                LOGGER.warn(logLinje.toString());
                break;
            case "ERROR":
                LOGGER.error(logLinje.toString());
                break;
            default:
                LOGGER.error("Level-field for LogLinje ikke godkjent.", logLinje);
        }
    }
}
