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
    public void sjekkVeilederTilgangTilEnhet(String s, String s1) {

    }

    @Override
    public void sjekkVeilederTilgangTilBruker(String s, ActionId actionId, AbacPersonId abacPersonId) {

    }

    @Override
    public void sjekkTilgangTilPerson(String s, ActionId actionId, AbacPersonId abacPersonId) {

    }

    @Override
    public void sjekkVeilederTilgangTilKode6(String s) {

    }

    @Override
    public void sjekkVeilederTilgangTilKode7(String s) {

    }

    @Override
    public void sjekkVeilederTilgangTilEgenAnsatt(String s) {

    }

    @Override
    public AbacClient getAbacClient() {
        return abacClient;
    }
}
