package no.nav.fo.service;

import no.nav.apiapp.feil.IngenTilgang;
import no.nav.apiapp.selftest.Helsesjekk;
import no.nav.apiapp.selftest.HelsesjekkMetadata;
import no.nav.common.auth.SubjectHandler;
import no.nav.sbl.rest.RestUtils;
import no.nav.sbl.util.EnvironmentUtils;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static no.nav.apiapp.util.UrlUtils.clusterUrlForApplication;
import static no.nav.common.auth.SsoToken.Type.OIDC;

@Component
public class VeilArbAbacService implements Helsesjekk {

    public static final String VEILARBABAC_HOSTNAME_PROPERTY = "VEILARBABAC";

    private final String abacTargetUrl = EnvironmentUtils.getOptionalProperty(VEILARBABAC_HOSTNAME_PROPERTY)
            .orElseGet(() -> clusterUrlForApplication("veilarbabac"));

    public void sjekkLeseTilgangTilAktor(String aktorId) {
        sjekkErTrue(() -> harLeseTilgangTilAktor(aktorId));
    }

    public void sjekkSkriveTilgangTilAktor(String fnr) {
        sjekkErTrue(() -> harSkriveTilgangTilAktor(fnr));
    }

    public void sjekkLeseTilgangTilFnr(String fnr) {
        sjekkErTrue(() -> harLeseTilgangTilFnr(fnr));
    }

    private void sjekkErTrue(Supplier<Boolean> asdf) {
        if (!asdf.get()) {
            throw new IngenTilgang();
        }
    }

    public boolean harTilgangTilEnhet(String enhetId) {
        return harTilgang(Resource.enhet, Action.read, "enhetId", enhetId);
    }

    private boolean harLeseTilgangTilAktor(String aktorId) {
        return harTilgang(Resource.person, Action.read, "aktorId", aktorId);
    }

    private boolean harSkriveTilgangTilAktor(String fnr) {
        return harTilgang(Resource.person, Action.update, "aktorId", fnr);
    }

    private boolean harLeseTilgangTilFnr(String fnr) {
        return harTilgang(Resource.person, Action.read, "fnr", fnr);
    }

    private boolean harTilgang(Resource resource, Action action, String identParameterNavn, String ident) {
        return "permit".equals(RestUtils.withClient(c -> c.target(abacTargetUrl)
                .path("self")
                .path(resource.name())
                .queryParam(identParameterNavn, ident)
                .queryParam("action", action.name())
                .request()
                .header(AUTHORIZATION, "Bearer " + SubjectHandler.getSsoToken(OIDC).orElseThrow(IllegalStateException::new))
                .get(String.class)
        ));
    }

    @Override
    public void helsesjekk() {
        int status = RestUtils.withClient(c -> c.target(abacTargetUrl)
                .path("ping")
                .request()
                .get()
                .getStatus());

        if (status != 200) {
            throw new IllegalStateException("Rest kall mot veilarbabac feilet");
        }
    }

    @Override
    public HelsesjekkMetadata getMetadata() {
        return new HelsesjekkMetadata(
                "veilarbabac helsesjekk",
                 abacTargetUrl,
                "Sjekker om veilarbabac endepunkt svarer",
                true
        );
    }

    private enum Action {
        read,
        update
    }

    private enum Resource {
        person,
        enhet
    }


}
