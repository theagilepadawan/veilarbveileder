package no.nav.veilarbveileder.service;

import no.nav.veilarbveileder.PortefoljeEnhet;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.OrganisasjonEnhetV2;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.informasjon.WSOppgavebehandlerfilter;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.meldinger.WSHentFullstendigEnhetListeRequest;
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.meldinger.WSHentFullstendigEnhetListeResponse;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class OrganisasjonEnhetV2Service {

    private static final Logger LOGGER = getLogger(OrganisasjonEnhetV2Service.class);

    @Inject
    private OrganisasjonEnhetV2 organisasjonEnhetService;

    public Optional<PortefoljeEnhet> hentEnhet(String enhetId) {
         return hentAlleEnheter()
                .stream()
                .filter((enhet) -> enhet.getEnhetId().equals(enhetId))
                .findFirst();
    }

    @Cacheable("veilarbveilederCache")
    public List<PortefoljeEnhet> hentAlleEnheter() {
        try {
            WSHentFullstendigEnhetListeResponse hentFullstendigEnhetListeResponse = organisasjonEnhetService.hentFullstendigEnhetListe(lagHentFullstendigEnhetListeRequest());

            return hentFullstendigEnhetListeResponse.getEnhetListe().stream()
                    .map(MappersKt::orgEnhetTilPortefoljeEnhet)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LOGGER.error("Kunne ikke hente alle enheter fra OrganisasjonEnhetV2/NORG2", e);
            throw e;
        }
    }

    private WSHentFullstendigEnhetListeRequest lagHentFullstendigEnhetListeRequest() {
        final WSHentFullstendigEnhetListeRequest request = new WSHentFullstendigEnhetListeRequest();
        request.setOppgavebehandlerfilter(WSOppgavebehandlerfilter.UFILTRERT);

        return request;
    }
}
