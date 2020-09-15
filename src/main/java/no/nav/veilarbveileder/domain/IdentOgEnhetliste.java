package no.nav.veilarbveileder.domain;

import lombok.Value;
import no.nav.common.types.identer.NavIdent;

import java.util.List;

@Value
public class IdentOgEnhetliste {
    NavIdent ident;
    List<PortefoljeEnhet> enhetliste;
}
