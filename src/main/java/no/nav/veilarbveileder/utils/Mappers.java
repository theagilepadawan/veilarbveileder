package no.nav.veilarbveileder.utils;

import no.nav.common.client.norg2.Enhet;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.Veileder;
import no.nav.veilarbveileder.domain.VeilederInfo;
import no.nav.veilarbveileder.domain.VeiledereResponse;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Ressurs;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;

import java.util.List;
import java.util.stream.Collectors;

public class Mappers {

    public static PortefoljeEnhet tilPortefoljeEnhet(Enhet enhet) {
        return new PortefoljeEnhet(enhet.getEnhetNr(), enhet.getNavn());
    }

    public static PortefoljeEnhet tilPortefoljeEnhet(no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet enhet) {
        return new PortefoljeEnhet(enhet.getEnhetId(), enhet.getNavn());
    }

    public static List<String> ressursResponseTilIdentListe(WSHentRessursListeResponse response) {
        return response.getRessursListe()
                .stream()
                .map(Ressurs::getRessursId)
                .collect(Collectors.toList());
    }

    public static VeilederInfo enhetResponseTilVeilederInfo(WSHentEnhetListeResponse response) {
        Ressurs ressurs = response.getRessurs();
        List<PortefoljeEnhet> enheter = response.getEnhetListe()
                .stream()
                .map(Mappers::tilPortefoljeEnhet)
                .collect(Collectors.toList());

        return new VeilederInfo()
                .setIdent(ressurs.getRessursId())
                .setNavn(ressurs.getNavn())
                .setFornavn(ressurs.getFornavn())
                .setEtternavn(ressurs.getEtternavn())
                .setEnheter(enheter);
    }

    public static VeiledereResponse ressursResponseTilVeilederResponse(WSHentRessursListeResponse response) {
        PortefoljeEnhet portefoljeEnhet = new PortefoljeEnhet()
                .setEnhetId(response.getEnhet().getEnhetId())
                .setNavn(response.getEnhet().getNavn());

        List<Veileder> veiledere = response.getRessursListe()
                .stream()
                .map(Mappers::ressursTilVeileder)
                .collect(Collectors.toList());

        return new VeiledereResponse(portefoljeEnhet, veiledere);
    }

    public static Veileder ressursTilVeileder(Ressurs ressurs) {
        return new Veileder()
                .setIdent(ressurs.getRessursId())
                .setNavn(ressurs.getNavn())
                .setFornavn(ressurs.getFornavn())
                .setEtternavn(ressurs.getEtternavn());
    }

}
