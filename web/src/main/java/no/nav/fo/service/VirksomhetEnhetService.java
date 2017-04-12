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

    private static final Logger logger = getLogger(VirksomhetEnhetService.class);

    @Inject
    private Enhet virksomhetEnhet;

    @Cacheable("veilarbveilederCache")
    public EnheterResponse hentEnhetListe(String ident) throws Exception {
        try {
            WSHentEnhetListeRequest request = new WSHentEnhetListeRequest();
            request.setRessursId(ident);
            WSHentEnhetListeResponse response = virksomhetEnhet.hentEnhetListe(request);
            return mapWSEnhetResponseTilEnheterResponse(response);
        } catch (HentEnhetListeUgyldigInput e) {
            String feil = String.format("Kunne ikke hente ansattopplysnigner for %s", ident);
            logger.error(feil, e);
            throw e;
        } catch (HentEnhetListeRessursIkkeFunnet e) {
            String feil = String.format("Kunne ikke hente ansattopplysninger for %s", ident);
            logger.error(feil, e);
            throw e;
        } catch (Exception e) {
            String feil = String.format("Kunne ikke hente ansattopplysninger for %s: Ukjent Feil", ident);
            logger.error(feil, e);
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

        } catch (HentRessursListeUgyldigInput e) {
            String feil = String.format("Kunne ikke hente ressursliste for %s", enhetId);
            logger.error(feil, e);
            throw e;
        } catch (HentRessursListeEnhetikkefunnet e) {
            String feil = String.format("Kunne ikke hente ressursliste for %S", enhetId);
            logger.error(feil, e);
            throw e;
        } catch (Exception e) {
            String feil = String.format("Kunne ikke hente ressursliste for %s, ukjent feil", enhetId);
            logger.error(feil, e);
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

    EnheterResponse mapWSEnhetResponseTilEnheterResponse(WSHentEnhetListeResponse response) {
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
