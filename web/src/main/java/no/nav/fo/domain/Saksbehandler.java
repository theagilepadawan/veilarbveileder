package no.nav.fo.domain;

import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet;

import java.util.List;

public class Saksbehandler {

    private String ident;
    private String navn;
    private List<Enhet> enheter;

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setEnheter(List<Enhet> enheter) { this.enheter = enheter; }

    public Saksbehandler withIdent(String ident) {
        this.ident = ident;
        return this;
    }

    public String getIdent() {
        return ident;
    }

    public String getNavn() {
        return navn;
    }

    public List<Enhet> getEnheter() { return enheter; }
}
