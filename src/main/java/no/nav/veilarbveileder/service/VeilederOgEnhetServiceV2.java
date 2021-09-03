package no.nav.veilarbveileder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.Veileder;
import no.nav.veilarbveileder.domain.VeilederInfo;
import no.nav.veilarbveileder.domain.VeiledereResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static no.nav.veilarbveileder.service.AuthService.ROLLE_MODIA_ADMIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class VeilederOgEnhetServiceV2 implements VeilederOgEnhetService {

    private final AuthService authService;

    private final EnhetService enhetService;

    private final VeilederService veilederService;

    @Override
    public List<PortefoljeEnhet> hentEnhetListe(NavIdent navIdent) {
        if (authService.harModiaAdminRolle(navIdent)) {
            log.info("Rollen {} ble brukt for ident: {}", ROLLE_MODIA_ADMIN, navIdent);
            return enhetService.alleEnheter();
        }

        return enhetService.hentTilganger(navIdent);
    }

    @Override
    public Veileder hentVeilederData(NavIdent navIdent) {
        return veilederService.hentVeileder(navIdent);
    }

    @Override
    public VeilederInfo hentVeilederInfo(NavIdent navIdent) {
        Veileder veileder = hentVeilederData(navIdent);
        List<PortefoljeEnhet> veilederEnheter = hentEnhetListe(navIdent);

        return new VeilederInfo()
                .setIdent(veileder.getIdent())
                .setFornavn(veileder.getFornavn())
                .setEtternavn(veileder.getEtternavn())
                .setNavn(veileder.getNavn())
                .setEnheter(veilederEnheter);
    }

    @Override
    public VeiledereResponse hentRessursListe(EnhetId enhetId) {
        PortefoljeEnhet enhet = enhetService.hentEnhet(enhetId).orElseThrow();
        List<NavIdent> veilederIdenterPaEnhet = enhetService.veilederePaEnhet(enhetId);
        List<Veileder> veilederePaEnhet = veilederService.hentVeiledere(veilederIdenterPaEnhet);

        return new VeiledereResponse(enhet, veilederePaEnhet);
    }

    @Override
    public List<String> hentIdentListe(EnhetId enhetId) {
        return enhetService.veilederePaEnhet(enhetId).stream().map(NavIdent::get).collect(Collectors.toList());
    }

}
