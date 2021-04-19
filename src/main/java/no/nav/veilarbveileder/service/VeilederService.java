package no.nav.veilarbveileder.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.client.nom.NomClient;
import no.nav.common.client.nom.VeilederNavn;
import no.nav.common.types.identer.NavIdent;
import no.nav.common.utils.StringUtils;
import no.nav.veilarbveileder.domain.Veileder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        // TODO: Converter fra Uppercase til stor forbokstav
        return new Veileder()
                .setIdent(veilederNavn.getNavIdent().get())
                .setFornavn(veilederNavn.getFornavn())
                .setEtternavn(veilederNavn.getEtternavn())
                .setNavn(lagNavn(veilederNavn.getFornavn(), veilederNavn.getMellomnavn(), veilederNavn.getEtternavn()));
    }

    private static String lagNavn(String fornavn, String mellomnavn, String etternavn) {
        StringBuilder builder = new StringBuilder();
        builder.append(etternavn);
        builder.append(", ");
        builder.append(fornavn);

        if (StringUtils.notNullOrEmpty(mellomnavn)) {
            builder.append(" ");
            builder.append(mellomnavn);
        }

        return builder.toString();
    }

}
