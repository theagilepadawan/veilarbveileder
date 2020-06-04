import no.nav.apiapp.ApiApp;
import no.nav.veilarbveileder.config.ApplicationConfig;

public class Main {

    public static void main(String... args) {
        ApiApp.runApp(ApplicationConfig.class, args);
    }

}
