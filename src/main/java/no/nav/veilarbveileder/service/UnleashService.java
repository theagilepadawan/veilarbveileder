package no.nav.veilarbveileder.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.featuretoggle.UnleashClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnleashService {

    public final static String DISABLE_TJENESTEBUSS_TJENESTEBUSS_TOGGLE_NAME = "veilarbveileder.disable-tjenestebuss";

    private final UnleashClient unleashClient;

    public boolean skalIkkeBrukeTjenestebuss() {
        return unleashClient.isEnabled(DISABLE_TJENESTEBUSS_TJENESTEBUSS_TOGGLE_NAME);
    }
}
