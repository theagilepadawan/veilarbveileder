package no.nav.veilarbveileder.consumer;

import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Inject;

public class VirksomhetEnhetConsumer {
    private static Logger log = LoggerFactory.getLogger(VirksomhetEnhetConsumer.class.getName());

    @Inject
    private Enhet virksomhetEnhet;


    @Cacheable("veilarbveilederCache")
    public WSHentRessursListeResponse hentEnhetInfo(String enhetId) throws Exception{
        WSHentRessursListeRequest request = new WSHentRessursListeRequest();
        request.setEnhetId(enhetId);
        try {
            return virksomhetEnhet.hentRessursListe(request);
        } catch (Exception e) {
            log.error("Kunne ikke hente ressursene til enhet {} fra VirksomhetEnhet/NORG2", enhetId, e);
            throw e;
        }
    }

    @Cacheable("veilarbveilederCache")
    public WSHentEnhetListeResponse hentVeilederInfo(String ident) throws Exception{
        WSHentEnhetListeRequest request = new WSHentEnhetListeRequest();
        request.setRessursId(ident);
        try {
            return virksomhetEnhet.hentEnhetListe(request);
        } catch (Exception e){
            log.error("Kunne ikke hente enhetene til veileder {} fra VirksomhetEnhet/NORG2", ident, e);
            throw e;
        }
    }
}
