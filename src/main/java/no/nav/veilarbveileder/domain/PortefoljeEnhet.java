package no.nav.veilarbveileder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import no.nav.common.types.identer.EnhetId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PortefoljeEnhet {
    EnhetId enhetId;
    String navn;
}
