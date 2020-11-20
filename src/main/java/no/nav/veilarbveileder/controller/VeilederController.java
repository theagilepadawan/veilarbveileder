package no.nav.veilarbveileder.controller;

import no.nav.common.types.identer.NavIdent;
import no.nav.veilarbveileder.domain.IdentOgEnhetliste;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.Veileder;
import no.nav.veilarbveileder.domain.VeilederInfo;
import no.nav.veilarbveileder.service.AuthService;
import no.nav.veilarbveileder.service.VirksomhetEnhetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        authService.sjekkTilgangTilModia();
        NavIdent navIdent = authService.getInnloggetVeilederIdent();
        List<PortefoljeEnhet> response = virksomhetEnhetService.hentEnhetListe(navIdent);
        return new IdentOgEnhetliste(navIdent, response);
    }

    @GetMapping("/enheter/{veilederIdent}")
    public IdentOgEnhetliste hentEnheter(@PathVariable("veilederIdent") NavIdent veilederIdent) {
        if (!authService.erSystemBruker()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ikke tilgang til oppfølging");
        }

        authService.sjekkTilgangTilOppfolging();
        List<PortefoljeEnhet> response = virksomhetEnhetService.hentEnhetListe(veilederIdent);
        return new IdentOgEnhetliste(veilederIdent, response);
    }

    @GetMapping("/me")
    public Veileder hentVeilederData() {
        authService.sjekkTilgangTilModia();
        return virksomhetEnhetService.hentVeilederData(authService.getInnloggetVeilederIdent());
    }

    @GetMapping("/v2/me")
    public VeilederInfo hentVeilederInfo() {
        authService.sjekkTilgangTilModia();
        return virksomhetEnhetService.hentVeilederInfo(authService.getInnloggetVeilederIdent());
    }

    @GetMapping("/{ident}")
    public Veileder hentVeilederForIdent(@PathVariable("ident") NavIdent ident) {
        authService.sjekkTilgangTilModia();
        return virksomhetEnhetService.hentVeilederData(ident);
    }

}
