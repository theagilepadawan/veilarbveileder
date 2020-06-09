package no.nav.veilarbveileder.utils;

import no.nav.common.utils.Credentials;

import static no.nav.common.utils.EnvironmentUtils.getRequiredProperty;

public class ServiceUserUtils {

    public static Credentials getServiceUserCredentials() {
        // TODO: Den riktig måte å gjøre dette på er å mounte credentials fra Vault
        String username = getRequiredProperty("SRVVEILARBVEILEDER_USERNAME");
        String password = getRequiredProperty("SRVVEILARBVEILEDER_PASSWORD");
        return new Credentials(username, password);
    }

}
