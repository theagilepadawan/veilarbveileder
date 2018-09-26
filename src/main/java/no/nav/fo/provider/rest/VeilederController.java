package no.nav.fo.provider.rest;

import io.swagger.annotations.Api;
import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.fo.IdentOgEnhetliste;
import no.nav.fo.PortefoljeEnhet;
import no.nav.fo.Veileder;
import no.nav.fo.service.VirksomhetEnhetService;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.virksomhet.tjenester.enhet.v1.HentEnhetListeRessursIkkeFunnet;
import no.nav.virksomhet.tjenester.enhet.v1.HentEnhetListeUgyldigInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

@Api(value = "Veileder")
@Path("/veileder")
@Produces(APPLICATION_JSON)
public class VeilederController {

    @Inject
    private VirksomhetEnhetService virksomhetEnhetService;

    @Inject
    private Pep pepClient;

    @GET
    @Path("/enheter")
    public Response hentEnheter() {
        TilgangsRegler.tilgangTilOppfolging(pepClient);
        String ident = SubjectHandler.getSubjectHandler().getUid();

        try {
            List<PortefoljeEnhet> response = virksomhetEnhetService.hentEnhetListe(ident);
            IdentOgEnhetliste entity = new IdentOgEnhetliste(ident, response);

            return Response.ok().entity(entity).build();
        } catch (HentEnhetListeUgyldigInput e) {
            throw new WebApplicationException(e, BAD_REQUEST);
        } catch (HentEnhetListeRessursIkkeFunnet e) {
            return Response.noContent().build();
        } catch (Exception e) {
            throw new WebApplicationException(e, INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/me")
    public Response hentVeilederInfo() {
        String ident = SubjectHandler.getSubjectHandler().getUid();
        return hentVeilederForIdent(ident);
    }

    @GET
    @Path("/{ident}")
    public Response hentVeilederForIdent(@PathParam("ident") String ident) {
        TilgangsRegler.tilgangTilOppfolging(pepClient);

        try {
            Veileder veileder = virksomhetEnhetService.hentVeilederData(ident);
            return Response.ok(veileder).build();
        } catch (HentEnhetListeUgyldigInput e) {
            throw new WebApplicationException(e, BAD_REQUEST);
        } catch (HentEnhetListeRessursIkkeFunnet e) {
            return Response.noContent().build();
        } catch (Exception e) {
            throw new WebApplicationException(e, INTERNAL_SERVER_ERROR);
        }
    }
}
