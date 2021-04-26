package no.nav.veilarbveileder.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.featuretoggle.UnleashClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnleashService {

    public final static String TJENESTEBUSS_TJENESTEBUSS_TOGGLE_NAME = "veilarbveileder.enabled-tjenestebuss";

    private final UnleashClient unleashClient;

    public boolean skalBrukeTjenestebuss() {
        return unleashClient.isEnabled(TJENESTEBUSS_TJENESTEBUSS_TOGGLE_NAME);
    }

}
