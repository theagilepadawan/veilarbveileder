package no.nav.veilarbveileder.service;

import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.Veileder;
import no.nav.veilarbveileder.domain.VeilederInfo;
import no.nav.veilarbveileder.domain.VeiledereResponse;

import java.util.List;

/**
 * Midlertidig interface for å toggle bruk av tjenestebuss
 */
public interface VeilederOgEnhetService {

    List<PortefoljeEnhet> hentEnhetListe(NavIdent navIdent);

    Veileder hentVeilederData(NavIdent navIdent);

    VeilederInfo hentVeilederInfo(NavIdent navIdent);

    VeiledereResponse hentRessursListe(EnhetId enhetId);

    List<String> hentIdentListe(EnhetId enhetId);

}
