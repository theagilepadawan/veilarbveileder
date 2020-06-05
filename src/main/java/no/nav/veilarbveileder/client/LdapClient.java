package no.nav.veilarbveileder.client;

import no.nav.common.health.HealthCheck;

public interface LdapClient extends HealthCheck {

    boolean veilederHarRolle(String ident, String rolle);

}
