package no.nav.fo.provider.rest;


import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.fo.domene.VeiledereResponse;
import no.nav.fo.service.PepClient;
import no.nav.fo.service.VirksomhetEnhetService;
import no.nav.virksomhet.organisering.enhetogressurs.v1.Enhet;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeEnhetikkefunnet;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeUgyldigInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

@Path("/enhet")
@Produces(APPLICATION_JSON)
public class EnhetController {

    @Inject
    VirksomhetEnhetService virksomhetEnhetService;

    @Inject
    PepClient pepClient;

    @GET
    @Path("/{enhetId}/veiledere")
    public Response hentRessurser(@PathParam("enhetId") String enhetId) {
        String ident = SubjectHandler.getSubjectHandler().getUid();

        try {
            boolean isUserInModigOppfolging = pepClient.isSubjectMemberOfModiaOppfolging();
            if(!isUserInModigOppfolging) {
                return Response.status(UNAUTHORIZED).build();
            }
        } catch(Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }

        List<String> enheterIPilot = Arrays.asList(System.getProperty("portefolje.pilot.enhetliste").split(","));


        VeiledereResponse response = null;

        try {
            if(!enheterIPilot.contains(enhetId)) {
                Enhet returnEnhet = new Enhet();
                returnEnhet.setEnhetId(enhetId);
                return Response.ok().entity(new VeiledereResponse().setVeilederListe(new ArrayList<>()).setEnhet(returnEnhet)).build();
            }
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