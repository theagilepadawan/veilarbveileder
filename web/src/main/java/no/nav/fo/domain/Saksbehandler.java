package no.nav.fo.domain;

public class Saksbehandler {

    private String ident;
    private String navn;

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Saksbehandler withIdent(String ident) {
        this.ident = ident;
        return this;
    }
}
