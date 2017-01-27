package no.nav.fo.provider.rest;


import no.nav.fo.service.VirksomhetEnhetServiceImpl;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/enhet")
@Produces(APPLICATION_JSON)
public class EnhetController {

    @Inject
    VirksomhetEnhetServiceImpl virksomhetEnhetService;

    @GET
    @Path("/{enhetId}/veiledere")
    public Response hentRessurser(@PathParam("enhetId") String enhetId) {

        return Response.ok().entity(enhetId).build();
    }
}
