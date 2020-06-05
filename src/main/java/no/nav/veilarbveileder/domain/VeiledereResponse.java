package no.nav.veilarbveileder.domain;

import lombok.Value;

import java.util.List;

@Value
public class VeiledereResponse {
    PortefoljeEnhet enhet;
    List<Veileder> veilederListe;
}
