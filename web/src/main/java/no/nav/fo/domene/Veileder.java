package no.nav.fo.domene;

public class Veileder {
    private String ident;
    private String navn;
    private String fornavn;
    private String etternavn;

    public String getIdent() {
        return ident;
    }

    public Veileder withIdent(String ident) {
        this.ident = ident;
        return this;
    }

    public String getNavn() {
        return navn;
    }

    public Veileder withNavn(String navn) {
        this.navn = navn;
        return this;
    }

    public String getFornavn() {
        return fornavn;
    }

    public Veileder withFornavn(String fornavn) {
        this.fornavn = fornavn;
        return this;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public Veileder withEtternavn(String etternavn) {
        this.etternavn = etternavn;
        return this;
    }
}
