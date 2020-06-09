package no.nav.veilarbveileder.mock;

import no.nav.common.abac.AbacClient;
import no.nav.common.abac.Pep;
import no.nav.common.abac.domain.AbacPersonId;
import no.nav.common.abac.domain.request.ActionId;

public class PepMock implements Pep {

    private final AbacClient abacClient;

    public PepMock(AbacClient abacClient) {
        this.abacClient = abacClient;
    }

    @Override
    public void sjekkTilgangTilEnhet(String veilederIdent, String enhetId) {

    }

    @Override
    public void sjekkVeilederTilgangTilPerson(String veilederIdent, ActionId actionId, AbacPersonId personId) {

    }

    @Override
    public void sjekkTilgangTilPerson(String innloggetBrukerIdToken, ActionId actionId, AbacPersonId personId) {

    }

    @Override
    public void sjekkTilgangTilOppfolging(String innloggetVeilederIdToken) {

    }

    @Override
    public void sjekkTilgangTilModia(String innloggetVeilederIdToken) {

    }

    @Override
    public void sjekkTilgangTilKode6(String veilederIdent) {

    }

    @Override
    public void sjekkTilgangTilKode7(String veilederIdent) {

    }

    @Override
    public void sjekkTilgangTilEgenAnsatt(String veilederIdent) {

    }

    @Override
    public AbacClient getAbacClient() {
        return abacClient;
    }
}
