package no.nav.fo.provider.rest;

import no.nav.brukerdialog.security.context.InternbrukerSubjectHandler;
import no.nav.fo.service.BrukertilgangService;
import no.nav.fo.service.VirksomhetEnhetService;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.RequestData;
import no.nav.sbl.dialogarena.common.abac.pep.domain.response.BiasedDecisionResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.lang.System.setProperty;
import static no.nav.brukerdialog.security.context.SubjectHandler.SUBJECTHANDLER_KEY;
import static no.nav.sbl.dialogarena.common.abac.pep.domain.response.Decision.Permit;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetControllerTest {

    @Mock
    private VirksomhetEnhetService virksomhetEnhetService;

    @Mock
    private BrukertilgangService brukertilgangService;

    @Mock
    private Pep pepClientInterface;

    @InjectMocks
    private EnhetController enhetController;

    @Before
    public void setup() {
        setProperty(SUBJECTHANDLER_KEY, InternbrukerSubjectHandler.class.getName());
        InternbrukerSubjectHandler.setVeilederIdent("testident");
    }

    @Test
    public void skalReturnereResponsNaarBrukerHarTilgang() throws Exception {
        when(pepClientInterface.harTilgang(any(RequestData.class))).thenReturn(new BiasedDecisionResponse(Permit, null));
        when(brukertilgangService.harBrukerTilgang(any(), any())).thenReturn(true);

        enhetController.hentRessurser("0002");

        verify(pepClientInterface, times(1)).isSubjectMemberOfModiaOppfolging(any(), any());
        verify(virksomhetEnhetService, times(1)).hentRessursListe(anyString());
    }
}
