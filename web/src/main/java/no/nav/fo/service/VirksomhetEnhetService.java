package no.nav.fo.service;

import no.nav.fo.domene.Saksbehandler;

import no.nav.fo.domene.Veileder;
import no.nav.fo.domene.VeiledereResponse;

import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import no.nav.virksomhet.tjenester.enhet.v1.HentEnhetListeRessursIkkeFunnet;
import no.nav.virksomhet.tjenester.enhet.v1.HentEnhetListeUgyldigInput;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeEnhetikkefunnet;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeUgyldigInput;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;

import java.lang.Exception;
import java.util.stream.Collectors;

import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

public class VirksomhetEnhetService {

    private static final Logger logger = getLogger(VirksomhetEnhetService.class);

    @Inject
    private Enhet virksomhetEnhet;

    public WSHentEnhetListeResponse hentEnhetListe(String ident) throws HentEnhetListeUgyldigInput, HentEnhetListeRessursIkkeFunnet {

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
            String feil = String.format("Kunne ikke hente ansattopplysninger for %s", ident);
            logger.error(feil, e);
            throw e;
        } catch (java.lang.Exception e) {
            String feil = String.format("Kunne ikke hente ansattopplysninger for %s: Ukjent Feil", ident);
            logger.error(feil, e);
            throw e;
        }
    }

    public Saksbehandler hentSaksbehandlerInfo(String ident) throws HentEnhetListeUgyldigInput, HentEnhetListeRessursIkkeFunnet {

        Saksbehandler saksbehandler = new Saksbehandler().withIdent(ident);
        saksbehandler.setNavn(hentEnhetListe(ident).getRessurs().getNavn());
        return saksbehandler;
    }

    @Cacheable("ressursEnhetCache")
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
}
