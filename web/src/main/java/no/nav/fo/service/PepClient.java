package no.nav.fo.service;

import no.nav.metrics.MetricsFactory;
import no.nav.metrics.Timer;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.domain.response.BiasedDecisionResponse;
import no.nav.sbl.dialogarena.common.abac.pep.domain.response.Decision;
import no.nav.sbl.dialogarena.common.abac.pep.exception.PepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;


public class PepClient implements PepClientInterface {

    private Logger LOGGER = LoggerFactory.getLogger(PepClient.class);

    @Inject
    private Pep pep;

    public boolean isSubjectMemberOfModiaOppfolging(String ident, String token) {
        BiasedDecisionResponse callAllowed;
        try {
            Timer timer = MetricsFactory.createTimer("isSubjectMemberOfModiaOppfolging");
            timer.start();
            callAllowed  = pep.isSubjectMemberOfModiaOppfolging(token, "veilarb");
            timer.stop();
            timer.report();
        } catch (PepException e) {
            throw new InternalServerErrorException("Something went wrong when wrong in PEP", e);
        }
        if (callAllowed.getBiasedDecision().equals(Decision.Deny)) {
            LOGGER.info("User "+ ident +" is not in group MODIA-OPPFOLGING");
        }
        return callAllowed.getBiasedDecision().equals(Decision.Permit);
    }
}
