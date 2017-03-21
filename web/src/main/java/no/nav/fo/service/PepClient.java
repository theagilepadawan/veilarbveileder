package no.nav.fo.service;

import no.nav.sbl.dialogarena.common.abac.pep.Pep;
import org.springframework.cache.annotation.Cacheable;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.ws.rs.InternalServerErrorException;


public class PepClient {

    @Inject
    private Pep pep;

    @Cacheable("veilarbveilederCache")
    public boolean isSubjectMemberOfModiaOppfolging(String ident) {
        boolean userIsMember = false;
        try {
            userIsMember = pep.isSubjectMemberOfModigOppfolging(ident);
        } catch (NamingException e) {
            throw new InternalServerErrorException("something went wrong when calling AD", e);
        }
        return userIsMember;
    }
}
