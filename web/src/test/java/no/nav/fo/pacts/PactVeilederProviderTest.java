package no.nav.fo.pacts;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import no.nav.pact.runner.NavHttpsPactTest;
import no.nav.pact.runner.NavPactRunner;
import org.junit.runner.RunWith;

import java.util.Optional;

@RunWith(NavPactRunner.class)
@Provider("veilarbveileder")
public class PactVeilederProviderTest extends NavHttpsPactTest {

    @State("a request for info about a veileder")
    public void verifyProviderStateSinglePersonNoChildren() {
        System.out.println("Known veileder.");
    }

    @State("a request for info about an unknown veileder")
    public void verifyProviderStateNoData() {
        System.out.println("Unknown veileder.");
    }

    @State("a request for info about a veileder in enhet")
    public void verifyProviderStateVeilederEnhet() {
        System.out.println("Known veileder in enhet.");
    }

    @Override
    public String getHttpTarget() {
        return Optional.ofNullable(System.getenv("PACT_TARGET_URL")).orElse("https://app-t6.adeo.no");
    }

}
