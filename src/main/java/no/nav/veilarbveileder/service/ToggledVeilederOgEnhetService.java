package no.nav.veilarbveileder.service;

import no.nav.common.client.norg2.Norg2Client;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.veilarbveileder.client.VirksomhetEnhetSoapClient;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.Veileder;
import no.nav.veilarbveileder.domain.VeilederInfo;
import no.nav.veilarbveileder.domain.VeiledereResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToggledVeilederOgEnhetService implements VeilederOgEnhetService {

    private final UnleashService unleashService;

    private final VeilederOgEnhetServiceV1 virksomhetEnhetService;

    private final VeilederOgEnhetServiceV2 virksomhetEnhetServiceV2;

    @Autowired
    public ToggledVeilederOgEnhetService(
            UnleashService unleashService,
            AuthService authService,
            Norg2Client norg2Client,
            VirksomhetEnhetSoapClient virksomhetEnhetSoapClient,
            EnhetService enhetService,
            VeilederService veilederService
    ) {
        this.unleashService = unleashService;

        virksomhetEnhetService = new VeilederOgEnhetServiceV1(authService, norg2Client, virksomhetEnhetSoapClient);

        virksomhetEnhetServiceV2 = new VeilederOgEnhetServiceV2(authService, enhetService, veilederService);
    }

    @Override
    public List<PortefoljeEnhet> hentEnhetListe(NavIdent navIdent) {
        return hentIVirksomhetEnhet().hentEnhetListe(navIdent);
    }

    @Override
    public Veileder hentVeilederData(NavIdent navIdent) {
        return hentIVirksomhetEnhet().hentVeilederData(navIdent);
    }

    @Override
    public VeilederInfo hentVeilederInfo(NavIdent navIdent) {
        return hentIVirksomhetEnhet().hentVeilederInfo(navIdent);
    }

    @Override
    public VeiledereResponse hentRessursListe(EnhetId enhetId) {
        return hentIVirksomhetEnhet().hentRessursListe(enhetId);
    }

    @Override
    public List<String> hentIdentListe(EnhetId enhetId) {
        return hentIVirksomhetEnhet().hentIdentListe(enhetId);
    }

    private VeilederOgEnhetService hentIVirksomhetEnhet() {
        if (unleashService.skalIkkeBrukeTjenestebuss()) {
            return virksomhetEnhetServiceV2;
        } else {
            return virksomhetEnhetService;
        }
    }

}
