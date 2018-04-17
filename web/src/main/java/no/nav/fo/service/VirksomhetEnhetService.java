package no.nav.fo.service;

import no.nav.fo.domene.PortefoljeEnhet;
import no.nav.fo.domene.Veileder;
import no.nav.fo.domene.VeiledereResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;


public class VirksomhetEnhetService {

    private static final Logger LOGGER = getLogger(VirksomhetEnhetService.class);
    private final String rolleModiaAdmin = "0000-GA-Modia_Admin";

    @Inject
    private Enhet virksomhetEnhet;

    @Inject
    private LdapService ldapService;

    @Inject
    private OrganisasjonEnhetV2Service organisasjonEnhetV2Service;


    @Cacheable("veilarbveilederCache")
    public List<PortefoljeEnhet> hentEnhetListe(String ident) throws Exception {
        try {
            final boolean harModiaAdminRolle = ldapService.veilederHarRolle(ident, rolleModiaAdmin);

            if (harModiaAdminRolle) {
                return hentAlleEnheter();
            }

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
    public Veileder hentVeilederData(String ident) throws Exception {
        WSHentEnhetListeRequest request = new WSHentEnhetListeRequest();
        request.setRessursId(ident);
        try {
            WSHentEnhetListeResponse response = virksomhetEnhet.hentEnhetListe(request);
            return new Veileder().setIdent(response.getRessurs().getRessursId())
                        .setEtternavn(response.getRessurs().getEtternavn())
                        .setFornavn(response.getRessurs().getFornavn())
                        .setNavn(response.getRessurs().getNavn());
        } catch (Exception e) {
            LOGGER.error("Kunne ikke hente veilederdata til veileder {} fra VirksomhetEnhet/NORG2", ident, e);
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

    private List<PortefoljeEnhet> mapWSEnhetResponseTilEnheterResponse(WSHentEnhetListeResponse response) {
        return response.getEnhetListe().stream().map(enhet ->
                new PortefoljeEnhet()
                        .setEnhetId(enhet.getEnhetId())
                        .setNavn(enhet.getNavn()))
                .collect(Collectors.toList());

    }

    private List<PortefoljeEnhet> hentAlleEnheter() {
        return  organisasjonEnhetV2Service.hentAlleEnheter();
    }
}
