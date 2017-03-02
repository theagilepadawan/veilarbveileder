package no.nav.fo.domene;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Veileder {
    String ident;
    String navn;
    String fornavn;
    String etternavn;

    public Veileder withIdent(String ident) {
        this.ident = ident;
        return this;
    }


}
