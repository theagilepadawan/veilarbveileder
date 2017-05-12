package no.nav.fo.provider.rest;


import io.swagger.annotations.Api;
import no.nav.fo.domene.VeiledereResponse;
import no.nav.fo.service.PepClientInterface;
import no.nav.fo.service.VirksomhetEnhetService;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeEnhetikkefunnet;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeUgyldigInput;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static java.util.Collections.emptyList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

@Api(value = "Enhet")
@Path("/enhet")
@Produces(APPLICATION_JSON)
public class EnhetController {

    @Inject
    VirksomhetEnhetService virksomhetEnhetService;

    @Inject
    PepClientInterface pepClient;

    @GET
    @Path("/{enhetId}/veiledere")
    public Response hentRessurser(@PathParam("enhetId") String enhetId) {
        TilgangsRegler.tilgangTilOppfolging(pepClient);

        try {
            if (!TilgangsRegler.enhetErIPilot(enhetId)) {
                VeiledereResponse respons = new VeiledereResponse()
                        .setVeilederListe(emptyList())
                        .setEnhet(new Enhet().withEnhetId(enhetId));

                return Response
                        .ok()
                        .entity(respons)
                        .build();
            }
            return Response.ok().entity(virksomhetEnhetService.hentRessursListe(enhetId)).build();
        } catch (HentRessursListeUgyldigInput e) {
            return Response.status(BAD_REQUEST).build();
        } catch (HentRessursListeEnhetikkefunnet e) {
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}