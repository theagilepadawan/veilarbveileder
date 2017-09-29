package no.nav.fo.service;


import no.nav.fo.domene.EnheterResponse;
import no.nav.fo.domene.PortefoljeEnhet;
import no.nav.fo.domene.Veileder;
import no.nav.fo.domene.VeiledereResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import no.nav.virksomhet.tjenester.enhet.v1.*;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Inject;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;


public class VirksomhetEnhetService {

    private static final Logger LOGGER = getLogger(VirksomhetEnhetService.class);

    @Inject
    private Enhet virksomhetEnhet;

    @Cacheable("veilarbveilederCache")
    public EnheterResponse hentEnhetListe(String ident) throws Exception {
        try {
            WSHentEnhetListeRequest request = new WSHentEnhetListeRequest();
            request.setRessursId(ident);
            WSHentEnhetListeResponse response = virksomhetEnhet.hentEnhetListe(request);
            return mapWSEnhetResponseTilEnheterResponse(response);
        } catch (Exception e) {
            LOGGER.error("Kunne ikke hente enhetene til veileder {} fra VirksomhetEnhet/NORG2", ident, e);
            throw e;
        }
    }

    @Cacheable("veilarbveilederCache")
    public VeiledereResponse hentRessursListe(String enhetId) throws Exception {
        try {
            WSHentRessursListeRequest request = new WSHentRessursListeRequest();
            request.setEnhetId(enhetId);
            WSHentRessursListeResponse originalResponse = virksomhetEnhet.hentRessursListe(request);
            return mapRessursResponseTilVeilederResponse(originalResponse);
        } catch (Exception e) {
            LOGGER.error("Kunne ikke hente ressursene til enhet {} fra VirksomhetEnhet/NORG2", enhetId, e);
            throw e;
        }
    }

    VeiledereResponse mapRessursResponseTilVeilederResponse(WSHentRessursListeResponse originalResponse) {
        return new VeiledereResponse()
                .setEnhet(originalResponse.getEnhet())
                .setVeilederListe(originalResponse.getRessursListe().stream().map(ressurs ->
                        new Veileder()
                                .setIdent(ressurs.getRessursId())
                                .setNavn(ressurs.getNavn())
                                .setFornavn(ressurs.getFornavn())
                                .setEtternavn(ressurs.getEtternavn()))
                        .collect(Collectors.toList())
                );
    }

    private EnheterResponse mapWSEnhetResponseTilEnheterResponse(WSHentEnhetListeResponse response) {
        return new EnheterResponse()
                .setVeileder(new Veileder().setIdent(response.getRessurs().getRessursId())
                        .setEtternavn(response.getRessurs().getEtternavn())
                        .setFornavn(response.getRessurs().getFornavn())
                        .setNavn(response.getRessurs().getNavn()))
                .setEnhetliste(response.getEnhetListe().stream().map(enhet ->
                        new PortefoljeEnhet()
                                .setEnhetId(enhet.getEnhetId())
                                .setNavn(enhet.getNavn()))
                        .collect(Collectors.toList()));

    }
}
