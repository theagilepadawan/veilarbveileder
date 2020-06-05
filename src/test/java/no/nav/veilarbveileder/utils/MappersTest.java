package no.nav.veilarbveileder.utils;

import no.nav.veilarbveileder.domain.VeiledereResponse;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Ressurs;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MappersTest {

    @Test
    public void skalMappeRessursListeResponsTilVeiledereResponsKorrekt() {
        no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet enhet = createEnhet("9999", "NAV Aremark");
        Ressurs ressurs = createRessurs();
        Ressurs anotherRessurs = createAnotherRessurs();
        List<Ressurs> ressursListe = new ArrayList<>();
        ressursListe.add(ressurs);
        ressursListe.add(anotherRessurs);
        WSHentRessursListeResponse wsHentRessursListeResponse = mock(WSHentRessursListeResponse.class);
        when(wsHentRessursListeResponse.getEnhet()).thenReturn(enhet);
        when(wsHentRessursListeResponse.getRessursListe()).thenReturn(ressursListe);

        VeiledereResponse veiledereResponse = Mappers.ressursResponseTilVeilederResponse(wsHentRessursListeResponse);

        assertThat(veiledereResponse.getEnhet().getEnhetId()).isEqualTo(enhet.getEnhetId());
        assertThat(veiledereResponse.getEnhet().getNavn()).isEqualTo(enhet.getNavn());
        assertThat(veiledereResponse.getVeilederListe().get(0).getNavn()).isEqualTo(ressurs.getNavn());
        assertThat(veiledereResponse.getVeilederListe().get(0).getEtternavn()).isEqualTo(ressurs.getEtternavn());
        assertThat(veiledereResponse.getVeilederListe().get(1).getFornavn()).isEqualTo(anotherRessurs.getFornavn());
        assertThat(veiledereResponse.getVeilederListe().get(1).getIdent()).isEqualTo(anotherRessurs.getRessursId());
    }

    private Enhet createEnhet(String enhetId, String navn) {
        no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet enhet = mock(no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet.class);
        when(enhet.getEnhetId()).thenReturn(enhetId);
        when(enhet.getNavn()).thenReturn(navn);
        return enhet;
    }

    private Ressurs createRessurs() {
        Ressurs ressurs = new Ressurs();
        ressurs.setRessursId("ressurs id");
        ressurs.setNavn("fornavn etternavn");
        ressurs.setEtternavn("etternavn");
        ressurs.setFornavn("fornavn");
        return ressurs;
    }

    private Ressurs createAnotherRessurs() {
        Ressurs ressurs = new Ressurs();
        ressurs.setRessursId("another ressurs id");
        ressurs.setNavn("another fornavn etternavn");
        ressurs.setEtternavn("another etternavn");
        ressurs.setFornavn("another fornavn");
        return ressurs;
    }
}
