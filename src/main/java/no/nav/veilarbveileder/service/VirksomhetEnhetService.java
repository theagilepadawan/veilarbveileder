package no.nav.veilarbveileder.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.client.norg2.Norg2Client;
import no.nav.veilarbveileder.client.LdapClient;
import no.nav.veilarbveileder.client.VirksomhetEnhetSoapClient;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.Veileder;
import no.nav.veilarbveileder.domain.VeilederInfo;
import no.nav.veilarbveileder.domain.VeiledereResponse;
import no.nav.veilarbveileder.utils.Mappers;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VirksomhetEnhetService {

    private static final String rolleModiaAdmin = "0000-GA-Modia_Admin";

    private final LdapClient ldapClient;

    private final Norg2Client norg2Client;

    private final VirksomhetEnhetSoapClient virksomhetEnhetSoapClient;

    @Autowired
    public VirksomhetEnhetService(LdapClient ldapClient, Norg2Client norg2Client, VirksomhetEnhetSoapClient virksomhetEnhetSoapClient) {
        this.ldapClient = ldapClient;
        this.norg2Client = norg2Client;
        this.virksomhetEnhetSoapClient = virksomhetEnhetSoapClient;
    }

    public List<PortefoljeEnhet> hentEnhetListe(String ident) {
        final boolean harModiaAdminRolle = ldapClient.veilederHarRolle(ident, rolleModiaAdmin);

        if (harModiaAdminRolle) {
            log.info("Rollen {} ble brukt for ident: {}", rolleModiaAdmin, ident);
            return alleEnheter();
        }

        WSHentEnhetListeResponse response = virksomhetEnhetSoapClient.hentVeilederInfo(ident);

        return response.getEnhetListe().stream().map(Mappers::tilPortefoljeEnhet).collect(Collectors.toList());
    }

    public Veileder hentVeilederData(String ident) {
        WSHentEnhetListeResponse response = virksomhetEnhetSoapClient.hentVeilederInfo(ident);
        return Mappers.ressursTilVeileder(response.getRessurs());
    }

    public VeilederInfo hentVeilederInfo(String ident) {
        final boolean harModiaAdminRolle = ldapClient.veilederHarRolle(ident, rolleModiaAdmin);
        WSHentEnhetListeResponse response = virksomhetEnhetSoapClient.hentVeilederInfo(ident);
        VeilederInfo veilederInfo = Mappers.enhetResponseTilVeilederInfo(response);

        if (harModiaAdminRolle) {
            log.info("Rollen {} ble brukt for ident: {}", rolleModiaAdmin, ident);
            veilederInfo.setEnheter(alleEnheter());
        }

        return veilederInfo;
    }

    public VeiledereResponse hentRessursListe(String enhetId) {
        WSHentRessursListeResponse response = virksomhetEnhetSoapClient.hentEnhetInfo(enhetId);
        return Mappers.ressursResponseTilVeilederResponse(response);
    }

    public List<String> hentIdentListe(String enhetId) {
        WSHentRessursListeResponse response = virksomhetEnhetSoapClient.hentEnhetInfo(enhetId);
        return Mappers.ressursResponseTilIdentListe(response);
    }

    private List<PortefoljeEnhet> alleEnheter() {
        return norg2Client.alleAktiveEnheter()
                .stream()
                .map(Mappers::tilPortefoljeEnhet)
                .collect(Collectors.toList());
    }

}
