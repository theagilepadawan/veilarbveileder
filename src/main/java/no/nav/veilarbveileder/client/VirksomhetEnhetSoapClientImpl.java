package no.nav.veilarbveileder.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.nav.common.health.HealthCheckResult;
import no.nav.veilarbveileder.config.CacheConfig;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentEnhetListeResponse;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeRequest;
import no.nav.virksomhet.tjenester.enhet.meldinger.v1.WSHentRessursListeResponse;
import no.nav.virksomhet.tjenester.enhet.v1.Enhet;
import org.springframework.cache.annotation.Cacheable;

@Slf4j
public class VirksomhetEnhetSoapClientImpl implements VirksomhetEnhetSoapClient {

    private final Enhet virksomhetEnhet;

    public VirksomhetEnhetSoapClientImpl(Enhet virksomhetEnhet) {
        this.virksomhetEnhet = virksomhetEnhet;
    }

    @Override
    @SneakyThrows
    @Cacheable(CacheConfig.HENT_ENHET_INFO_CACHE_NAME)
    public WSHentRessursListeResponse hentEnhetInfo(String enhetId) {
        WSHentRessursListeRequest request = new WSHentRessursListeRequest();
        request.setEnhetId(enhetId);

        try {
            return virksomhetEnhet.hentRessursListe(request);
        } catch (Exception e) {
            log.error("Kunne ikke hente ressursene til enhet {} fra VirksomhetEnhet/NORG2", enhetId, e);
            throw e;
        }
    }

    @Override
    @SneakyThrows
    @Cacheable(CacheConfig.VEILEDER_INFO_CACHE_NAME)
    public WSHentEnhetListeResponse hentVeilederInfo(String ident) {
        WSHentEnhetListeRequest request = new WSHentEnhetListeRequest();
        request.setRessursId(ident);
        try {
            return virksomhetEnhet.hentEnhetListe(request);
        } catch (Exception e){
            log.error("Kunne ikke hente enhetene til veileder {} fra VirksomhetEnhet/NORG2", ident, e);
            throw e;
        }
    }

    @Override
    public HealthCheckResult checkHealth() {
        try {
            virksomhetEnhet.ping();
            return HealthCheckResult.healthy();
        } catch (Exception e) {
            return HealthCheckResult.unhealthy(e);
        }
    }
}
