package no.nav.veilarbveileder.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.client.nom.NomClient;
import no.nav.common.client.nom.VeilederNavn;
import no.nav.common.types.identer.NavIdent;
import no.nav.veilarbveileder.domain.Veileder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static no.nav.veilarbveileder.utils.NavnUtils.lagNavn;
import static no.nav.veilarbveileder.utils.NavnUtils.storForbokstav;

@Service
@RequiredArgsConstructor
public class VeilederService {

    private final NomClient nomClient;

    public Veileder hentVeileder(NavIdent navIdent) {
        return tilVeileder(nomClient.finnNavn(navIdent));
    }

    public List<Veileder> hentVeiledere(List<NavIdent> navIdenter) {
        return nomClient.finnNavn(navIdenter).stream().map(VeilederService::tilVeileder).collect(Collectors.toList());
    }

    private static Veileder tilVeileder(VeilederNavn veilederNavn) {
        String fornavn = storForbokstav(veilederNavn.getFornavn());
        String mellomnavn = storForbokstav(veilederNavn.getMellomnavn());
        String etternavn = storForbokstav(veilederNavn.getEtternavn());

        return new Veileder()
                .setIdent(veilederNavn.getNavIdent().get())
                .setFornavn(fornavn)
                .setEtternavn(etternavn)
                .setNavn(lagNavn(fornavn, mellomnavn, etternavn));
    }

}
