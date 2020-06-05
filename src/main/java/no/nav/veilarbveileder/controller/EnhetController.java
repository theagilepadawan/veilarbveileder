package no.nav.veilarbveileder.controller;

import no.nav.common.client.norg2.Norg2Client;
import no.nav.veilarbveileder.domain.PortefoljeEnhet;
import no.nav.veilarbveileder.domain.VeiledereResponse;
import no.nav.veilarbveileder.service.AuthService;
import no.nav.veilarbveileder.service.VirksomhetEnhetService;
import no.nav.veilarbveileder.utils.Mappers;
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
public class EnhetController {

    private final VirksomhetEnhetService virksomhetEnhetService;

    private final Norg2Client norg2Client;

    private final AuthService authService;

    @Autowired
    public EnhetController(
            VirksomhetEnhetService virksomhetEnhetService,
            Norg2Client norg2Client,
            AuthService authService
    ) {
        this.virksomhetEnhetService = virksomhetEnhetService;
        this.norg2Client = norg2Client;
        this.authService = authService;
    }

    @GetMapping("/{enhetId}/navn")
    public PortefoljeEnhet hentNavn(@PathVariable("enhetId") String enhetId) {
        return norg2Client
                .alleAktiveEnheter()
                .stream()
                .filter(enhet -> enhet.getEnhetNr().equals(enhetId))
                .findFirst()
                .map(Mappers::tilPortefoljeEnhet)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{enhetId}/veiledere")
    public VeiledereResponse hentRessurser(@PathVariable("enhetId") String enhetId) {
        authService.tilgangTilOppfolging();
        authService.tilgangTilEnhet(enhetId);
        return virksomhetEnhetService.hentRessursListe(enhetId);
    }

    @GetMapping("/{enhetId}/identer")
    public List<String> hentIdenter(@PathVariable("enhetId") String enhetId) {
        return virksomhetEnhetService.hentIdentListe(enhetId);
    }

}
