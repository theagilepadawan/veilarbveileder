package no.nav.fo.provider.rest;


import io.swagger.annotations.Api;
import no.nav.fo.PortefoljeEnhet;
import no.nav.fo.service.OrganisasjonEnhetV2Service;
import no.nav.fo.service.VirksomhetEnhetService;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeEnhetikkefunnet;
import no.nav.virksomhet.tjenester.enhet.v1.HentRessursListeUgyldigInput;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

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
    public Response hentNavn(@PathParam("enhetId") String enhetId) {
        try {
            final PortefoljeEnhet portefoljeEnhet = organisasjonEnhetV2Service.hentAlleEnheter()
                    .stream()
                    .filter((enhet) -> enhet.getEnhetId().equals(enhetId))
                    .findFirst().orElseThrow(HentRessursListeEnhetikkefunnet::new);

            return Response.ok().entity(portefoljeEnhet).build();
        } catch (HentRessursListeEnhetikkefunnet e) {
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{enhetId}/veiledere")
    public Response hentRessurser(@PathParam("enhetId") String enhetId) {
        TilgangsRegler.tilgangTilOppfolging(pepClient);
        TilgangsRegler.tilgangTilEnhet(pepClient, enhetId);
        try {
            return Response.ok().entity(virksomhetEnhetService.hentRessursListe(enhetId)).build();
        } catch (HentRessursListeUgyldigInput e) {
            return Response.status(BAD_REQUEST).build();
        } catch (HentRessursListeEnhetikkefunnet e) {
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{enhetId}/identer")
    public Response hentIdenter(@PathParam("enhetId") String enhetId) {
        try {
            return Response.ok().entity(virksomhetEnhetService.hentIdentListe(enhetId)).build();
        } catch (HentRessursListeUgyldigInput e) {
            return Response.status(BAD_REQUEST).build();
        } catch (HentRessursListeEnhetikkefunnet e) {
            return Response.status(NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
