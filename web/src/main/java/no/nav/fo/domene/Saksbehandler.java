package no.nav.fo.domene;

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

    public String getIdent() {
        return ident;
    }

    public String getNavn() {
        return navn;
    }

}
