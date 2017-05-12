package no.nav.fo.service;

public class PepClientMock implements PepClientInterface {

    @Override
    public boolean isSubjectMemberOfModiaOppfolging(String ident, String token) {
        return true;
    }
}
