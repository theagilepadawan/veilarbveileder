package no.nav.veilarbveileder.client;

import no.nav.common.health.HealthCheck;
import no.nav.common.types.identer.NavIdent;

public interface LdapClient extends HealthCheck {

    boolean veilederHarRolle(NavIdent navIdent, String rolle);

}
