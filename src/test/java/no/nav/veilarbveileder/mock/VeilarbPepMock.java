package no.nav.veilarbveileder.mock;

import no.nav.common.abac.AbacClient;
import no.nav.common.abac.VeilarbPep;
import no.nav.common.abac.domain.request.ActionId;
import no.nav.common.types.identer.EksternBrukerId;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;

public class VeilarbPepMock extends VeilarbPep {

    private final AbacClient abacClient;

    public VeilarbPepMock(AbacClient abacClient) {
        super(null, null, null, null);
        this.abacClient = abacClient;
    }

    @Override
    public boolean harVeilederTilgangTilEnhet(NavIdent navIdent, EnhetId enhetId) {
        return false;
    }

    @Override
    public boolean harTilgangTilEnhet(String s, EnhetId enhetId) {
        return false;
    }

    @Override
    public boolean harTilgangTilEnhetMedSperre(String s, EnhetId enhetId) {
        return false;
    }

    @Override
    public boolean harVeilederTilgangTilPerson(NavIdent navIdent, ActionId actionId, EksternBrukerId eksternBrukerId) {
        return false;
    }

    @Override
    public boolean harTilgangTilPerson(String s, ActionId actionId, EksternBrukerId eksternBrukerId) {
        return false;
    }

    @Override
    public boolean harTilgangTilOppfolging(String s) {
        return false;
    }

    @Override
    public boolean harVeilederTilgangTilModia(String s) {
        return false;
    }

    @Override
    public boolean harVeilederTilgangTilKode6(NavIdent navIdent) {
        return false;
    }

    @Override
    public boolean harVeilederTilgangTilKode7(NavIdent navIdent) {
        return false;
    }

    @Override
    public boolean harVeilederTilgangTilEgenAnsatt(NavIdent navIdent) {
        return false;
    }
    
    @Override
    public AbacClient getAbacClient() {
        return abacClient;
    }
}
