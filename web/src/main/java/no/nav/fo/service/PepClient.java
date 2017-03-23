package no.nav.fo.service;

import no.nav.brukerdialog.security.context.SubjectHandler;
import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import no.nav.sbl.dialogarena.common.abac.pep.domain.response.BiasedDecisionResponse;
import no.nav.sbl.dialogarena.common.abac.pep.domain.response.Decision;
import no.nav.sbl.dialogarena.common.abac.pep.exception.PepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;


public class PepClient {

    Logger logger = LoggerFactory.getLogger(PepClient.class);

    @Inject
    private Pep pep;

    @Cacheable("veilarbveilederCache")
    public boolean isSubjectMemberOfModiaOppfolging(String ident) {
        BiasedDecisionResponse callAllowed;
        try {
            callAllowed  = pep.isSubjectMemberOfModiaOppfolging(ident);
        } catch (PepException e) {
            throw new InternalServerErrorException("Something went wrong when wrong in PEP", e);
        }
        if (callAllowed.getBiasedDecision().equals(Decision.Deny)) {
            logger.info("User "+ ident +" is not in group MODIA-OPPFOLGING");
        }
        return callAllowed.getBiasedDecision().equals(Decision.Permit);
    }
}
