package no.nav.veilarbveileder.utils;

import no.nav.common.client.nom.NomClient;
import no.nav.common.client.nom.VeilederNavn;
import no.nav.common.health.HealthCheckResult;
import no.nav.common.types.identer.NavIdent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * NOM har ikke støtte for å slå opp reelle veiledere eller Z-identer i test, så en alternativ klient må brukes
 */
public class DevNomClient implements NomClient {

    @Override
    public VeilederNavn finnNavn(NavIdent navIdent) {
        return lagVeilederNavn(navIdent);
    }

    @Override
    public List<VeilederNavn> finnNavn(List<NavIdent> navIdenter) {
        return navIdenter.stream().map(this::lagVeilederNavn).collect(Collectors.toList());
    }

    @Override
    public HealthCheckResult checkHealth() {
        return HealthCheckResult.healthy();
    }

    private VeilederNavn lagVeilederNavn(NavIdent navIdent) {
        String identNr = navIdent.get().substring(1); // Z12345 -> 12345

        return new VeilederNavn()
                .setNavIdent(navIdent)
                .setFornavn("F_" + identNr)
                .setEtternavn("E_" + identNr)
                .setVisningsNavn("E_" + identNr + ", F_" + identNr );
    }

}
