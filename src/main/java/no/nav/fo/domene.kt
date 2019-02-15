package no.nav.fo

import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet

data class VeiledereResponse(val enhet: Enhet, val veilederListe: List<Veileder>)
data class Veileder(val ident: String, val navn: String, val fornavn: String?, val etternavn: String?)
data class EnheterResponse(val veileder: Veileder, val enhetliste: List<PortefoljeEnhet>)
data class PortefoljeEnhet(val enhetId: String, val navn: String)
data class IdentOgEnhetliste(val ident: String, val enhetliste: List<PortefoljeEnhet>)
