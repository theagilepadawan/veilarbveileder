package no.nav.veilarbveileder.controller;

import no.nav.common.types.identer.NavIdent;
import no.nav.veilarbveileder.domain.IdentOgEnhetliste;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.Veileder;
import no.nav.veilarbveileder.domain.VeilederInfo;
import no.nav.veilarbveileder.service.AuthService;
import no.nav.veilarbveileder.service.VirksomhetEnhetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/veileder")
public class VeilederController {

    private final VirksomhetEnhetService virksomhetEnhetService;

    private final AuthService authService;

    @Autowired
    public VeilederController(VirksomhetEnhetService virksomhetEnhetService, AuthService authService) {
        this.virksomhetEnhetService = virksomhetEnhetService;
        this.authService = authService;
    }

    @GetMapping("/enheter")
    public IdentOgEnhetliste hentEnheter() {
        authService.tilgangTilModia();
        NavIdent navIdent = authService.getInnloggetVeilederIdent();
        List<PortefoljeEnhet> response = virksomhetEnhetService.hentEnhetListe(navIdent);
        return new IdentOgEnhetliste(navIdent, response);
    }

    @GetMapping("/me")
    public Veileder hentVeilederData() {
        authService.tilgangTilModia();
        return virksomhetEnhetService.hentVeilederData(authService.getInnloggetVeilederIdent());
    }

    @GetMapping("/v2/me")
    public VeilederInfo hentVeilederInfo() {
        authService.tilgangTilModia();
        return virksomhetEnhetService.hentVeilederInfo(authService.getInnloggetVeilederIdent());
    }

    @GetMapping("/{ident}")
    public Veileder hentVeilederForIdent(@PathVariable("ident") String ident) {
        authService.tilgangTilModia();
        return virksomhetEnhetService.hentVeilederData(NavIdent.of(ident));
    }

}
