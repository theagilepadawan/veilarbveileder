package no.nav.fo.provider.rest;

import no.nav.fo.service.VirksomhetEnhetService;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.RequestData;
import no.nav.sbl.dialogarena.common.abac.pep.domain.response.BiasedDecisionResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static no.nav.sbl.dialogarena.common.abac.pep.domain.response.Decision.Permit;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetControllerTest {

    @Mock
    private VirksomhetEnhetService virksomhetEnhetService;

    @Mock
    private Pep pep;

    @InjectMocks
    private EnhetController enhetController;

    @Test
    public void skalReturnereResponsNaarBrukerHarTilgang() throws Exception {
        when(pep.nyRequest()).thenReturn(new RequestData());
        when(pep.harTilgang(any(RequestData.class))).thenReturn(new BiasedDecisionResponse(Permit, null));
        when(pep.harTilgangTilEnhet(any(), any(), any())).thenReturn(new BiasedDecisionResponse(Permit, null));

        enhetController.hentRessurser("0002");

        verify(pep, times(1)).harTilgang(any(RequestData.class));
        verify(virksomhetEnhetService, times(1)).hentRessursListe(anyString());
    }
}
