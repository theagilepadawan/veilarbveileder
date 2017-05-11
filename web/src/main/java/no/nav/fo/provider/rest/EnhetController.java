package no.nav.fo.provider.rest;


import io.swagger.annotations.Api;
import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.fo.Utils;
import no.nav.fo.domene.VeiledereResponse;
import no.nav.fo.service.PepClientInterface;
import no.nav.fo.service.VirksomhetEnhetService;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeEnhetikkefunnet;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeUgyldigInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

@Api(value="Enhet")
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
        String ident = SubjectHandler.getSubjectHandler().getUid();

        if(!Utils.enhetErIPilot(enhetId)) {
            Enhet returnEnhet = new Enhet();
            returnEnhet.setEnhetId(enhetId);
            return Response.ok().entity(new VeiledereResponse().setVeilederListe(new ArrayList<>()).setEnhet(returnEnhet)).build();
        }

        try {
            boolean isUserInModigOppfolging = pepClient.isSubjectMemberOfModiaOppfolging(ident);
            if(!isUserInModigOppfolging) {
                return Response.status(UNAUTHORIZED).build();
            }
        } catch(Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }

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