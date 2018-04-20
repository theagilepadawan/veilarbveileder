package no.nav.fo.service

import no.nav.fo.PortefoljeEnhet
import no.nav.fo.Veileder
import no.nav.fo.VeiledereResponse
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.informasjon.Organisasjonsenhet
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse

fun orgEnhetTilPortefoljeEnhet(orgEnhet: Organisasjonsenhet): PortefoljeEnhet =
    PortefoljeEnhet(orgEnhet.enhetId, orgEnhet.enhetNavn)

fun wsEnhetResponseTilEnheterResponse(response: WSHentEnhetListeResponse): List<PortefoljeEnhet> =
    response.enhetListe.map { enhet ->
        PortefoljeEnhet(enhet.enhetId, enhet.navn)
    }

fun wsEnhetResponseTilVeileder(response: WSHentEnhetListeResponse): Veileder {
    val ressurs = response.ressurs
    return Veileder(ressurs.ressursId, ressurs.navn, ressurs.fornavn, ressurs.etternavn)
}

fun ressursResponseTilVeilederResponse(response: WSHentRessursListeResponse): VeiledereResponse =
        VeiledereResponse(response.enhet, response.ressursListe.map {
            Veileder(it.ressursId, it.navn, it.fornavn, it.etternavn)
        })

fun ressursResponseTilIdentListe(response: WSHentRessursListeResponse): List<String> =
        response.ressursListe.map { it.ressursId }