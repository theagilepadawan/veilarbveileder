package no.nav.fo.domene;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PortefoljeEnhet {
    String enhetId;
    String navn;
}
