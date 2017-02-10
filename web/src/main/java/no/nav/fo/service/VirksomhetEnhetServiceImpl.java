package no.nav.fo.service;


import no.nav.fo.domene.Veileder;
import no.nav.fo.domene.VeiledereResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import no.nav.virksomhet.tjenester.enhet.v1.*;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;

import java.lang.Exception;
import java.util.stream.Collectors;

import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

public class VirksomhetEnhetServiceImpl {

    private static final Logger logger = getLogger(VirksomhetEnhetServiceImpl.class);

    @Inject
    private Enhet virksomhetEnhet;

    public WSHentEnhetListeResponse hentEnhetListe(String ident) throws Exception{

        try {
            WSHentEnhetListeRequest request = new WSHentEnhetListeRequest();
            request.setRessursId(ident);
            WSHentEnhetListeResponse response = virksomhetEnhet.hentEnhetListe(request);
            return response;
        } catch (HentEnhetListeUgyldigInput e) {
            String feil = String.format("Kunne ikke hente ansattopplysnigner for %s", ident);
            logger.error(feil, e);
            throw e;
        } catch (HentEnhetListeRessursIkkeFunnet e) {
            String feil = String.format("Kunne ikke hente ansattopplysnigner for %s", ident);
            logger.error(feil,e);
            throw e;
        } catch (java.lang.Exception e) {
            String feil = String.format("Kunne ikke hente ansattopplysnigner for %s: Ukjent Feil", ident);
            logger.error(feil, e);
            throw e;
        }
    }

    @Cacheable("ressursEnhetCache")
    public VeiledereResponse hentRessursListe(String enhetId, int fra, int antall) throws Exception {

        try {
            WSHentRessursListeRequest request = new WSHentRessursListeRequest();
            request.setEnhetId(enhetId);
            WSHentRessursListeResponse originalResponse = virksomhetEnhet.hentRessursListe(request);

            VeiledereResponse veiledereResponse = mapRessursResponseTilVeilederResponse(originalResponse);

            return hentSubsetTilSide(veiledereResponse, fra, antall);
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

    private VeiledereResponse mapRessursResponseTilVeilederResponse(WSHentRessursListeResponse originalResponse) {
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

    private VeiledereResponse hentSubsetTilSide(VeiledereResponse veiledereResponse, int fra, int antall) {
        return new VeiledereResponse()
                .setEnhet(veiledereResponse.getEnhet())
                .setVeilederListe(veiledereResponse.getVeilederListe().stream().skip(fra).limit(antall).collect(Collectors.toList()))
                .setSublistFraIndex(fra)
                .setTotaltAntallVeiledere(veiledereResponse.getVeilederListe().size());
    }
}
