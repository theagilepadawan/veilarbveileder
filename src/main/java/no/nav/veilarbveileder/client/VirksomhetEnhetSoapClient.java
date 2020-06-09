package no.nav.veilarbveileder.client;

import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;

public interface VirksomhetEnhetSoapClient {

    WSHentRessursListeResponse hentEnhetInfo(String enhetId);

    WSHentEnhetListeResponse hentVeilederInfo(String ident);

}
