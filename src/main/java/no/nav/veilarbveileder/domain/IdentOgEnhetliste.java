package no.nav.veilarbveileder.domain;

import lombok.Value;

import java.util.List;

@Value
public class IdentOgEnhetliste {
    String ident;
    List<PortefoljeEnhet> enhetliste;
}
