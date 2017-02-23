package no.nav.fo.domain;

public class Saksbehandler {

    private String ident;

    public void setNavn(String navn) {
        this.navn = navn;
    }

    private String navn;

    public Saksbehandler withIdent(String ident) {
        this.ident = ident;
        return this;
    }
}
