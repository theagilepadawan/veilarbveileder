package no.nav.veilarbveileder.service

import no.nav.veilarbveileder.PortefoljeEnhet
import no.nav.veilarbveileder.Veileder
import no.nav.veilarbveileder.VeilederInfo
import no.nav.veilarbveileder.VeiledereResponse
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.informasjon.WSOrganisasjonsenhet
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse

fun orgEnhetTilPortefoljeEnhet(orgEnhet: WSOrganisasjonsenhet): PortefoljeEnhet =
    PortefoljeEnhet(orgEnhet.enhetId, orgEnhet.enhetNavn)

fun wsEnhetResponseTilEnheterResponse(response: WSHentEnhetListeResponse): List<PortefoljeEnhet> =
    response.enhetListe.map { PortefoljeEnhet(it.enhetId, it.navn) }

fun wsEnhetResponseTilVeileder(response: WSHentEnhetListeResponse): Veileder {
    val ressurs = response.ressurs
    return Veileder(ressurs.ressursId, ressurs.navn, ressurs.fornavn, ressurs.etternavn)
}

fun wsEnhetResponseTilVeilederInfo(response: WSHentEnhetListeResponse): VeilederInfo {
    val ressurs = response.ressurs
    val enheter = wsEnhetResponseTilEnheterResponse(response)
    return VeilederInfo(ressurs.ressursId, ressurs.navn, ressurs.fornavn, ressurs.etternavn, enheter)
}

fun ressursResponseTilVeilederResponse(response: WSHentRessursListeResponse): VeiledereResponse =
        VeiledereResponse(response.enhet, response.ressursListe.map {
            Veileder(it.ressursId, it.navn, it.fornavn, it.etternavn)
        })

fun ressursResponseTilIdentListe(response: WSHentRessursListeResponse): List<String> =
        response.ressursListe.map { it.ressursId }