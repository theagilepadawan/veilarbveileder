package no.nav.fo.service;

import no.nav.fo.PortefoljeEnhet;
import no.nav.fo.Veileder;
import no.nav.fo.VeilederInfo;
import no.nav.fo.VeiledereResponse;
import no.nav.fo.consumer.VirksomhetEnhetConsumer;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Ressurs;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class VirksomhetEnhetService {

    private static final String rolleModiaAdmin = "0000-GA-Modia_Admin";
    private Logger log = LoggerFactory.getLogger(VirksomhetEnhetService.class.getName());

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
        return MappersKt.wsEnhetResponseTilEnheterResponse(response);
    }

    public Veileder hentVeilederData(String ident) throws Exception {
        WSHentEnhetListeResponse response = virksomhetEnhetConsumer.hentVeilederInfo(ident);
        return MappersKt.wsEnhetResponseTilVeileder(response);
    }

    public VeilederInfo hentVeilederInfo(String ident) throws Exception {
        final boolean harModiaAdminRolle = ldapService.veilederHarRolle(ident, rolleModiaAdmin);
        WSHentEnhetListeResponse response = virksomhetEnhetConsumer.hentVeilederInfo(ident);
        VeilederInfo veilederInfo = MappersKt.wsEnhetResponseTilVeilederInfo(response);
        if (!harModiaAdminRolle) {
            return veilederInfo;
        }

        log.info("Rollen {} ble brukt for ident: {}", rolleModiaAdmin, ident);

        List<PortefoljeEnhet> enheter = hentEnhetListe(ident);
        return new VeilederInfo(veilederInfo.getIdent(), veilederInfo.getNavn(), veilederInfo.getFornavn(), veilederInfo.getEtternavn(), enheter);
    }

    public VeiledereResponse hentRessursListe(String enhetId) throws Exception {
        WSHentRessursListeResponse response = virksomhetEnhetConsumer.hentEnhetInfo(enhetId);
        return MappersKt.ressursResponseTilVeilederResponse(response);
    }

    public List<String> hentIdentListe(String enhetId) throws Exception {
        WSHentRessursListeResponse response = virksomhetEnhetConsumer.hentEnhetInfo(enhetId);
        return MappersKt.ressursResponseTilIdentListe(response);
    }

    private List<PortefoljeEnhet> hentAlleEnheter() {
        return organisasjonEnhetV2Service.hentAlleEnheter();
    }
}
