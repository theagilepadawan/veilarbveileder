package no.nav.veilarbveileder.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.client.norg2.Norg2Client;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.veilarbveileder.client.VirksomhetEnhetSoapClient;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.Veileder;
import no.nav.veilarbveileder.domain.VeilederInfo;
import no.nav.veilarbveileder.domain.VeiledereResponse;
import no.nav.veilarbveileder.utils.Mappers;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static no.nav.veilarbveileder.service.AuthService.ROLLE_MODIA_ADMIN;

@Slf4j
public class VeilederOgEnhetServiceV1 implements VeilederOgEnhetService {

    private final AuthService authService;

    private final Norg2Client norg2Client;

    private final VirksomhetEnhetSoapClient virksomhetEnhetSoapClient;

    @Autowired
    public VeilederOgEnhetServiceV1(AuthService authService, Norg2Client norg2Client, VirksomhetEnhetSoapClient virksomhetEnhetSoapClient) {
        this.authService = authService;
        this.norg2Client = norg2Client;
        this.virksomhetEnhetSoapClient = virksomhetEnhetSoapClient;
    }

    @Override
    public List<PortefoljeEnhet> hentEnhetListe(NavIdent navIdent) {
        final boolean harModiaAdminRolle = authService.harModiaAdminRolle(navIdent);

        if (harModiaAdminRolle) {
            log.info("Rollen {} ble brukt for ident: {}", ROLLE_MODIA_ADMIN, navIdent);
            return alleEnheter();
        }

        WSHentEnhetListeResponse response = virksomhetEnhetSoapClient.hentVeilederInfo(navIdent);

        return response.getEnhetListe().stream().map(Mappers::tilPortefoljeEnhet).collect(Collectors.toList());
    }

    @Override
    public Veileder hentVeilederData(NavIdent navIdent) {
        WSHentEnhetListeResponse response = virksomhetEnhetSoapClient.hentVeilederInfo(navIdent);
        return Mappers.ressursTilVeileder(response.getRessurs());
    }

    @Override
    public VeilederInfo hentVeilederInfo(NavIdent navIdent) {
        final boolean harModiaAdminRolle = authService.harModiaAdminRolle(navIdent);
        WSHentEnhetListeResponse response = virksomhetEnhetSoapClient.hentVeilederInfo(navIdent);
        VeilederInfo veilederInfo = Mappers.enhetResponseTilVeilederInfo(response);

        if (harModiaAdminRolle) {
            log.info("Rollen {} ble brukt for ident: {}", ROLLE_MODIA_ADMIN, navIdent);
            veilederInfo.setEnheter(alleEnheter());
        }

        return veilederInfo;
    }

    @Override
    public VeiledereResponse hentRessursListe(EnhetId enhetId) {
        WSHentRessursListeResponse response = virksomhetEnhetSoapClient.hentEnhetInfo(enhetId);
        return Mappers.ressursResponseTilVeilederResponse(response);
    }

    @Override
    public List<String> hentIdentListe(EnhetId enhetId) {
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
