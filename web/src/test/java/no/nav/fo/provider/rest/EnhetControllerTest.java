package no.nav.fo.provider.rest;

import no.nav.brukerdialog.security.context.InternbrukerSubjectHandler;
import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.fo.service.PepClientInterface;
import no.nav.fo.service.VirksomhetEnhetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static java.lang.System.setProperty;
import static no.nav.brukerdialog.security.context.SubjectHandler.SUBJECTHANDLER_KEY;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetControllerTest {

    @Mock
    VirksomhetEnhetService virksomhetEnhetService;

    @Mock
    PepClientInterface pepClientInterface;

    @InjectMocks
    EnhetController enhetController;

    @Before
    public void setup() {
        setProperty(SUBJECTHANDLER_KEY, InternbrukerSubjectHandler.class.getName());
        InternbrukerSubjectHandler.setVeilederIdent("testident");
        System.clearProperty("portefolje.pilot.enhetliste");
    }

    @Test
    public void skalReturnereTomResponsNaarEnhetIkkeErIPilot() throws Exception {
        when(pepClientInterface.isSubjectMemberOfModiaOppfolging(anyString(), any())).thenReturn(true);
        setProperty("portefolje.pilot.enhetliste", "0000,0001");

        enhetController.hentRessurser("0002");

        verify(pepClientInterface, times(1)).isSubjectMemberOfModiaOppfolging(any(), any());
        verify(virksomhetEnhetService, never()).hentRessursListe(anyString());

    }

    @Test
    public void skalIkkeReturnereTomResponsNaarPilotlisteIkkeInneholderEnhet() throws Exception {
        when(pepClientInterface.isSubjectMemberOfModiaOppfolging(anyString(), any())).thenReturn(true);
        setProperty("portefolje.pilot.enhetliste", "[]");

        enhetController.hentRessurser("0002");

        verify(pepClientInterface, times(1)).isSubjectMemberOfModiaOppfolging(any(), any());
        verify(virksomhetEnhetService, times(1)).hentRessursListe(anyString());

    }



}
