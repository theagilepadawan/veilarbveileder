package no.nav.veilarbveileder.client;

import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;

public interface VirksomhetEnhetSoapClient {

    WSHentRessursListeResponse hentEnhetInfo(EnhetId enhetId);

    WSHentEnhetListeResponse hentVeilederInfo(NavIdent navIdent);

}
