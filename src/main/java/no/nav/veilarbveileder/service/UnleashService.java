package no.nav.veilarbveileder.service;

import lombok.RequiredArgsConstructor;
import no.nav.common.featuretoggle.UnleashClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnleashService {

    public final static String BRUK_NOM_TOGGLE = "veilarbveileder.bruk-nom";

    private final UnleashClient unleashClient;

    public boolean brukNom() {
        return unleashClient.isEnabled(BRUK_NOM_TOGGLE);
    }

}
