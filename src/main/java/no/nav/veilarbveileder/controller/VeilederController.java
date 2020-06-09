package no.nav.veilarbveileder.controller;

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
        String ident = authService.getInnloggetVeilederIdent();
        List<PortefoljeEnhet> response = virksomhetEnhetService.hentEnhetListe(ident);
        return new IdentOgEnhetliste(ident, response);
    }

    @GetMapping("/me")
    public Veileder hentVeilederData() {
        String ident = authService.getInnloggetVeilederIdent();
        return hentVeilederForIdent(ident);
    }

    @GetMapping("/v2/me")
    public VeilederInfo hentVeilederInfo() {
        authService.tilgangTilModia();
        String ident = authService.getInnloggetVeilederIdent();
        return virksomhetEnhetService.hentVeilederInfo(ident);
    }


    @GetMapping("/{ident}")
    public Veileder hentVeilederForIdent(@PathVariable("ident") String ident) {
        authService.tilgangTilModia();
        return virksomhetEnhetService.hentVeilederData(ident);
    }
}
