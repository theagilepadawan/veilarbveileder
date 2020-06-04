package no.nav.veilarbveileder.provider.rest;

import io.swagger.annotations.Api;
import no.nav.veilarbveileder.PortefoljeEnhet;
import no.nav.veilarbveileder.VeiledereResponse;
import no.nav.veilarbveileder.service.OrganisasjonEnhetV2Service;
import no.nav.veilarbveileder.service.VirksomhetEnhetService;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Api(value = "Enhet")
@Path("/enhet")
@Produces(APPLICATION_JSON)
public class EnhetController {

    @Inject
    private VirksomhetEnhetService virksomhetEnhetService;

    @Inject
    private OrganisasjonEnhetV2Service organisasjonEnhetV2Service;

    @Inject
    private Pep pepClient;

    @GET
    @Path("/{enhetId}/navn")
    public PortefoljeEnhet hentNavn(@PathParam("enhetId") String enhetId) {
        return organisasjonEnhetV2Service.hentEnhet(enhetId).orElse(null);
    }

    @GET
    @Path("/{enhetId}/veiledere")
    public VeiledereResponse hentRessurser(@PathParam("enhetId") String enhetId) throws Exception {
        TilgangsRegler.tilgangTilOppfolging(pepClient);
        TilgangsRegler.tilgangTilEnhet(pepClient, enhetId);
        return virksomhetEnhetService.hentRessursListe(enhetId);
    }

    @GET
    @Path("/{enhetId}/identer")
    public List<String> hentIdenter(@PathParam("enhetId") String enhetId) throws Exception {
        return virksomhetEnhetService.hentIdentListe(enhetId);
    }
}
