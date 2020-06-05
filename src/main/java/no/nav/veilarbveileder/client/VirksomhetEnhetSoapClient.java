package no.nav.veilarbveileder.client;

import no.nav.common.health.HealthCheck;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;

public interface VirksomhetEnhetSoapClient extends HealthCheck {

    WSHentRessursListeResponse hentEnhetInfo(String enhetId);

    WSHentEnhetListeResponse hentVeilederInfo(String ident);

}
