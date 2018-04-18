package no.nav.fo.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.fo.consumer.VirksomhetEnhetConsumer;
import no.nav.fo.domene.PortefoljeEnhet;
import no.nav.fo.domene.Veileder;
import no.nav.fo.domene.VeiledereResponse;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Ressurs;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class VirksomhetEnhetService {

    private static final String rolleModiaAdmin = "0000-GA-Modia_Admin";

    @Inject
    private LdapService ldapService;

    @Inject
    private OrganisasjonEnhetV2Service organisasjonEnhetV2Service;

    @Inject
    private VirksomhetEnhetConsumer virksomhetEnhetConsumer;


    public List<PortefoljeEnhet> hentEnhetListe(String ident) throws Exception {
        final boolean harModiaAdminRolle = ldapService.veilederHarRolle(ident, rolleModiaAdmin);

        if (harModiaAdminRolle) {
            log.info("Rollen {} ble brukt for ident: {}", rolleModiaAdmin, ident);
            return hentAlleEnheter();
        }

        WSHentEnhetListeResponse response = virksomhetEnhetConsumer.hentVeilederInfo(ident);
        return mapWSEnhetResponseTilEnheterResponse(response);
    }

    public Veileder hentVeilederData(String ident) throws Exception {
        WSHentEnhetListeResponse response = virksomhetEnhetConsumer.hentVeilederInfo(ident);
        return mapWSEnhetResponseTilVeilederResponse(response);
    }

    public VeiledereResponse hentRessursListe(String enhetId) throws Exception {
        WSHentRessursListeResponse response = virksomhetEnhetConsumer.hentEnhetInfo(enhetId);
        return mapRessursResponseTilVeilederResponse(response);
    }

    public List<String> hentIdentListe(String enhetId) throws Exception {
        WSHentRessursListeResponse response = virksomhetEnhetConsumer.hentEnhetInfo(enhetId);
        return mapRessursResponsTilIdentListe(response);
    }

    private Veileder mapWSEnhetResponseTilVeilederResponse(WSHentEnhetListeResponse response) {
        return new Veileder().setIdent(response.getRessurs().getRessursId())
                .setEtternavn(response.getRessurs().getEtternavn())
                .setFornavn(response.getRessurs().getFornavn())
                .setNavn(response.getRessurs().getNavn());
    }

    private List<String> mapRessursResponsTilIdentListe(WSHentRessursListeResponse originalResponse) {
        return originalResponse.getRessursListe()
                .stream()
                .map(Ressurs::getRessursId)
                .collect(Collectors.toList());
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
        return organisasjonEnhetV2Service.hentAlleEnheter();
    }
}
