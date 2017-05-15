package no.nav.fo.provider.rest;

import no.nav.brukerdialog.security.context.InternbrukerSubjectHandler;
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
        setProperty("no.nav.modig.core.context.subjectHandlerImplementationClass", InternbrukerSubjectHandler.class.getName());
        InternbrukerSubjectHandler.setVeilederIdent("testident");
        System.clearProperty("portefolje.pilot.enhetliste");
    }

    @Test
    public void skalReturnereTomResponsNaarEnhetIkkeErIPilot() throws Exception {
        setProperty("portefolje.pilot.enhetliste", "0000,0001");

        Response response = enhetController.hentRessurser("0002");
        verify(pepClientInterface, never()).isSubjectMemberOfModiaOppfolging(any(), any());
    }

    @Test
    public void skalIkkeReturnereTomResponsNaarPilotlisteIkkeInneholderEnhet() {
        setProperty("portefolje.pilot.enhetliste", "[]");
        Response response = enhetController.hentRessurser("0002");

        verify(pepClientInterface, times(1)).isSubjectMemberOfModiaOppfolging(any(), any());

    }



}
