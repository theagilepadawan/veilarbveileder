package no.nav.veilarbveileder.provider.rest;

import io.swagger.annotations.Api;
import no.nav.common.auth.SubjectHandler;
import no.nav.veilarbveileder.IdentOgEnhetliste;
import no.nav.veilarbveileder.PortefoljeEnhet;
import no.nav.veilarbveileder.Veileder;
import no.nav.veilarbveileder.VeilederInfo;
import no.nav.veilarbveileder.service.VirksomhetEnhetService;
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
        String ident = SubjectHandler.getIdent().orElseThrow(IllegalStateException::new);
        List<PortefoljeEnhet> response = virksomhetEnhetService.hentEnhetListe(ident);
        return new IdentOgEnhetliste(ident, response);
    }

    @GET
    @Path("/me")
    public Veileder hentVeilederData() throws Exception {
        String ident = SubjectHandler.getIdent().orElseThrow(IllegalStateException::new);
        return hentVeilederForIdent(ident);
    }

    @GET
    @Path("/v2/me")
    public VeilederInfo hentVeilederInfo() throws Exception {
        TilgangsRegler.tilgangTilOppfolging(pepClient);
        String ident = SubjectHandler.getIdent().orElseThrow(IllegalStateException::new);
        return virksomhetEnhetService.hentVeilederInfo(ident);
    }


    @GET
    @Path("/{ident}")
    public Veileder hentVeilederForIdent(@PathParam("ident") String ident) throws Exception {
        TilgangsRegler.tilgangTilOppfolging(pepClient);
        return virksomhetEnhetService.hentVeilederData(ident);
    }
}
