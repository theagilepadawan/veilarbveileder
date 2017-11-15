package no.nav.fo.service;

import io.vavr.control.Try;
import no.nav.fo.domene.PortefoljeEnhet;

import javax.inject.Inject;
import java.util.List;


public class BrukertilgangService {

    @Inject
    VirksomhetEnhetService virksomhetEnhetService;

    public boolean harBrukerTilgang(String ident, String enhet) {
        return Try.of(() -> virksomhetEnhetService
                .hentEnhetListe(ident))
                .map(response -> finnesEnhetIListe(response.getEnhetliste(), enhet))
                .getOrElse(false);
    }

    private boolean finnesEnhetIListe(List<PortefoljeEnhet> enhetListe, String enhet) {
        return enhetListe.stream()
                .filter(item -> item.getEnhetId().equals(enhet))
                .toArray()
                .length > 0;
    }
}
