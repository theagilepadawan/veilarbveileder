package no.nav.fo.provider.rest;


import no.nav.fo.domene.VeiledereResponse;
import no.nav.fo.service.VirksomhetEnhetServiceImpl;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeEnhetikkefunnet;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeUgyldigInput;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/enhet")
@Produces(APPLICATION_JSON)
public class EnhetController {

    @Inject
    VirksomhetEnhetServiceImpl virksomhetEnhetService;

    @GET
    @Path("/{enhetId}/veiledere")
    public Response hentRessurser(@PathParam("enhetId") String enhetId) {

        VeiledereResponse response = null;

        try {
            response = virksomhetEnhetService.hentRessursListe(enhetId);
        } catch (HentRessursListeUgyldigInput e) {
            return Response.status(BAD_REQUEST).build();
        } catch (HentRessursListeEnhetikkefunnet e) {
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok().entity(response).build();
    }
}