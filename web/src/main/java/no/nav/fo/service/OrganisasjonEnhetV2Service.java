package no.nav.fo.service;

import no.nav.fo.domene.PortefoljeEnhet;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.binding.OrganisasjonEnhetV2;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.informasjon.Oppgavebehandlerfilter;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.informasjon.Organisasjonsenhet;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.meldinger.HentFullstendigEnhetListeRequest;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.meldinger.HentFullstendigEnhetListeResponse;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class OrganisasjonEnhetV2Service {

    private static final Logger LOGGER = getLogger(OrganisasjonEnhetV2Service.class);

    @Inject
    private OrganisasjonEnhetV2 organisasjonEnhetService;

    @Cacheable("veilarbveilederCache")
    public List<PortefoljeEnhet> hentAlleEnheter() {
        try {
            HentFullstendigEnhetListeResponse hentFullstendigEnhetListeResponse = organisasjonEnhetService.hentFullstendigEnhetListe(lagHentFullstendigEnhetListeRequest());

            return hentFullstendigEnhetListeResponse.getEnhetListe().stream()
                    .map(TIL_PORTEFOLJEENHET)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LOGGER.error("Kunne ikke hente alle enheter fra OrganisasjonEnhetV2/NORG2", e);
            throw e;
        }
    }

    private HentFullstendigEnhetListeRequest lagHentFullstendigEnhetListeRequest() {
        final HentFullstendigEnhetListeRequest request = new HentFullstendigEnhetListeRequest();
        request.setOppgavebehandlerfilter(Oppgavebehandlerfilter.UFILTRERT);

        return request;
    }

    private static final Function<Organisasjonsenhet, PortefoljeEnhet> TIL_PORTEFOLJEENHET =
            Organisasjonsenhet -> new PortefoljeEnhet()
                    .setEnhetId(Organisasjonsenhet.getEnhetId())
                    .setNavn(Organisasjonsenhet.getEnhetNavn());
}
