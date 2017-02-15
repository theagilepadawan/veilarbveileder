package no.nav.fo.domene;

import lombok.Data;
import lombok.experimental.Accessors;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet;

import java.util.List;

@Data
@Accessors(chain = true)
public class VeiledereResponse {
    List<Veileder> veilederListe;
    Enhet enhet;
}
