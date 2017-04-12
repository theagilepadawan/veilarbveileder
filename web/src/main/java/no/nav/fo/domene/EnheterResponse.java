package no.nav.fo.domene;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class EnheterResponse {
    List<PortefoljeEnhet> enhetliste;
    Veileder veileder;
}
