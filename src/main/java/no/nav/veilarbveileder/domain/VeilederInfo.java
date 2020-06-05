package no.nav.veilarbveileder.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class VeilederInfo {
    String ident;
    String navn;
    String fornavn;
    String etternavn;
    List<PortefoljeEnhet> enheter;
}
