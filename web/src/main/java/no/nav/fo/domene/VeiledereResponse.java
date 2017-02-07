package no.nav.fo.domene;

import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet;

import java.util.List;

public class VeiledereResponse {
    private List<Veileder> veilederListe;
    private Enhet enhet;
    private int totaltAntallVeiledere;
    private int sublistFraIndex;

    public List<Veileder> getVeilederListe() {
        return veilederListe;
    }

    public VeiledereResponse withVeilederListe(List<Veileder> veilederListe) {
        this.veilederListe = veilederListe;
        return this;
    }

    public Enhet getEnhet() {
        return enhet;
    }

    public VeiledereResponse withEnhet(Enhet enhet) {
        this.enhet = enhet;
        return this;
    }

    public VeiledereResponse withTotaltAntallVeiledere(int totaltAntallVeiledere) {
        this.totaltAntallVeiledere = totaltAntallVeiledere;
        return this;
    }

    public VeiledereResponse withSublistFraIndex(int sublistFraIndex) {
        this.sublistFraIndex = sublistFraIndex;
        return this;
    }
}
