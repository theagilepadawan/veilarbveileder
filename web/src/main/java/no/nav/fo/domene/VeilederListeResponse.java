package no.nav.fo.domene;

import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet;

import java.util.List;

public class VeilederListeResponse {
    private List<Veileder> veilederListe;
    private Enhet enhet;

    public List<Veileder> getVeilederListe() {
        return veilederListe;
    }

    public VeilederListeResponse withVeilederListe(List<Veileder> veilederListe) {
        this.veilederListe = veilederListe;
        return this;
    }

    public Enhet getEnhet() {
        return enhet;
    }

    public VeilederListeResponse withEnhet(Enhet enhet) {
        this.enhet = enhet;
        return this;
    }
}
