package no.nav.fo.provider.rest;

import io.swagger.annotations.Api;
import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.fo.IdentOgEnhetliste;
import no.nav.fo.PortefoljeEnhet;
import no.nav.fo.Veileder;
import no.nav.fo.service.VirksomhetEnhetService;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
    public IdentOgEnhetliste hentEnheter() throws Exception {
        TilgangsRegler.tilgangTilOppfolging(pepClient);
        String ident = SubjectHandler.getSubjectHandler().getUid();
        List<PortefoljeEnhet> response = virksomhetEnhetService.hentEnhetListe(ident);
        return new IdentOgEnhetliste(ident, response);
    }

    @GET
    @Path("/me")
    public Veileder hentVeilederInfo() throws Exception {
        String ident = SubjectHandler.getSubjectHandler().getUid();
        return hentVeilederForIdent(ident);
    }

    @GET
    @Path("/{ident}")
    public Veileder hentVeilederForIdent(@PathParam("ident") String ident) throws Exception {
        TilgangsRegler.tilgangTilOppfolging(pepClient);
        return virksomhetEnhetService.hentVeilederData(ident);
    }
}
