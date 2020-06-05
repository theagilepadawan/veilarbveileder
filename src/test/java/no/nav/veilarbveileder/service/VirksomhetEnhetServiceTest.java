package no.nav.veilarbveileder.service;

import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.Veileder;
import no.nav.veilarbveileder.domain.VeiledereResponse;
import no.nav.veilarbveileder.utils.Mappers;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Ressurs;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import no.nav.virksomhet.tjenester.enhet.v1.HentEnhetListeRessursIkkeFunnet;
import no.nav.virksomhet.tjenester.enhet.v1.HentEnhetListeUgyldigInput;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MockApplicationConfig.class})
public class VirksomhetEnhetServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private WSHentEnhetListeResponse response;

//    @Inject
    private Enhet virksomhetEnhet;

//    @Inject
    private VirksomhetEnhetService virksomhetEnhetService;

    @Before
    public void before() throws Exception {
        reset(virksomhetEnhet);
        response = createWSHentEnhetListeResponse();
        when(virksomhetEnhet.hentEnhetListe(any(WSHentEnhetListeRequest.class))).thenReturn(response);
    }

    @Test
    public void hentEnhetListeOk() throws Exception {
        List<PortefoljeEnhet> enhetListe = virksomhetEnhetService.hentEnhetListe("XX23456");
        verify(virksomhetEnhet).hentEnhetListe(any(WSHentEnhetListeRequest.class));
        assertThat(enhetListe.get(0).getEnhetId()).isEqualTo("1");
    }

    @Test
    public void hentVeilederOk() throws Exception {
        Veileder veileder = virksomhetEnhetService.hentVeilederData("XX23456");
        assertThat(veileder.getNavn()).isEqualTo("fornavn etternavn");
    }

    @Test(expected = HentEnhetListeRessursIkkeFunnet.class)
    public void shouldThrowExceptionWhenNotFound() throws Exception {
        HentEnhetListeRessursIkkeFunnet exception = new HentEnhetListeRessursIkkeFunnet("msg");
        when(virksomhetEnhet.hentEnhetListe(any(WSHentEnhetListeRequest.class))).thenThrow(exception);

        expectedException.expectMessage(contains("msg"));

        virksomhetEnhetService.hentEnhetListe("ident");
    }

    @Test(expected = HentEnhetListeUgyldigInput.class)
    public void shouldThrowExceptionWhenInvalidInputException() throws Exception {
        HentEnhetListeUgyldigInput exception = new HentEnhetListeUgyldigInput("msg");
        when(virksomhetEnhet.hentEnhetListe(any(WSHentEnhetListeRequest.class))).thenThrow(exception);

        expectedException.expectMessage(contains("msg"));

        virksomhetEnhetService.hentEnhetListe("ident");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenRuntimeExceptionIsThrown() throws Exception {
        IllegalArgumentException exception = new IllegalArgumentException("feil");
        when(virksomhetEnhet.hentEnhetListe(any(WSHentEnhetListeRequest.class))).thenThrow(exception);

        expectedException.expectMessage(contains("feil"));

        virksomhetEnhetService.hentEnhetListe("ident");
    }

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

    private WSHentEnhetListeResponse createWSHentEnhetListeResponse() {
        WSHentEnhetListeResponse response = new WSHentEnhetListeResponse();
        response.getEnhetListe().addAll(singletonList(createEnhet("1", "navn1")));
        response.setRessurs(createRessurs());
        return response;
    }

    private no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet createEnhet(String enhetId, String navn) {
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
