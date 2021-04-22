package no.nav.veilarbveileder.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.client.axsys.AxsysClient;
import no.nav.common.client.norg2.Norg2Client;
import no.nav.common.types.identer.EnhetId;
import no.nav.common.types.identer.NavIdent;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.utils.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnhetService {

    private final Norg2Client norg2Client;

    private final AxsysClient axsysClient;

    public Optional<PortefoljeEnhet> hentEnhet(EnhetId enhetId) {
        return norg2Client
                .alleAktiveEnheter()
                .stream()
                .filter(enhet -> enhet.getEnhetNr().equals(enhetId.get()))
                .findFirst()
                .map(Mappers::tilPortefoljeEnhet);
    }

    public List<PortefoljeEnhet> alleEnheter() {
        return norg2Client.alleAktiveEnheter()
                .stream()
                .map(Mappers::tilPortefoljeEnhet)
                .collect(Collectors.toList());
    }

    public List<NavIdent> veilederePaEnhet(EnhetId enhetId) {
        return axsysClient.hentAnsatte(enhetId);
    }

    public List<PortefoljeEnhet> hentTilganger(NavIdent navIdent) {
        return axsysClient.hentTilganger(navIdent)
                .stream()
                .map(Mappers::tilPortefoljeEnhet)
                .collect(Collectors.toList());
    }

}

