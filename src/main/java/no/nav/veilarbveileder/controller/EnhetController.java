package no.nav.veilarbveileder.controller;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.types.identer.EnhetId;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.VeiledereResponse;
import no.nav.veilarbveileder.service.AuthService;
import no.nav.veilarbveileder.service.EnhetService;
import no.nav.veilarbveileder.service.ToggledVeilederOgEnhetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/enhet")
@Slf4j
public class EnhetController {

    private final ToggledVeilederOgEnhetService veilederOgEnhetService;

    private final EnhetService enhetService;

    private final AuthService authService;

    @Autowired
    public EnhetController(
            ToggledVeilederOgEnhetService veilederOgEnhetService,
            EnhetService enhetService,
            AuthService authService
    ) {
        this.veilederOgEnhetService = veilederOgEnhetService;
        this.enhetService = enhetService;
        this.authService = authService;
    }

    @GetMapping("/{enhetId}/navn")
    public PortefoljeEnhet hentNavn(@PathVariable("enhetId") EnhetId enhetId) {
        return enhetService.hentEnhet(enhetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{enhetId}/veiledere")
    public VeiledereResponse hentRessurser(@PathVariable("enhetId") EnhetId enhetId) {
        authService.sjekkTilgangTilModia();
        authService.sjekkVeilederTilgangTilEnhet(enhetId);

        return veilederOgEnhetService.hentRessursListe(enhetId);
    }

    // TODO: Bruk List<NavIdent> når tjenestebuss er fjernet
    @GetMapping("/{enhetId}/identer")
    public List<String> hentIdenter(@PathVariable("enhetId") EnhetId enhetId) {
        if (authService.erSystemBruker()) {
            authService.sjekkTilgangTilOppfolging();
        } else {
            authService.sjekkTilgangTilModia();
        }

        return veilederOgEnhetService.hentIdentListe(enhetId);
    }

}
