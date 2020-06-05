package no.nav.veilarbveileder.controller;

import no.nav.common.client.norg2.Norg2Client;
import no.nav.common.health.selftest.SelfTestCheck;
import no.nav.common.health.selftest.SelfTestUtils;
import no.nav.common.health.selftest.SelftTestCheckResult;
import no.nav.common.health.selftest.SelftestHtmlGenerator;
import no.nav.veilarbveileder.client.LdapClient;
import no.nav.veilarbveileder.client.VirksomhetEnhetSoapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static no.nav.common.health.selftest.SelfTestUtils.checkAllParallel;

@RestController
@RequestMapping("/internal")
public class InternalController {

    private final List<SelfTestCheck> selftestChecks;

    @Autowired
    public InternalController(
            LdapClient ldapClient,
            Norg2Client norg2Client,
            VirksomhetEnhetSoapClient virksomhetEnhetSoapClient
    ) {
        this.selftestChecks = Arrays.asList(
                new SelfTestCheck("Ldap sjekk", true, ldapClient),
                new SelfTestCheck("Ping mot norg2 REST API", true, norg2Client),
                new SelfTestCheck("Ping mot VirksomhetEnhet (NORG)", true, virksomhetEnhetSoapClient)
        );
    }

    @GetMapping("/isReady")
    public void isReady() {}

    @GetMapping("/isAlive")
    public void isAlive() {}

    @GetMapping("/selftest")
    public ResponseEntity selftest() {
        List<SelftTestCheckResult> checkResults = checkAllParallel(selftestChecks);
        String html = SelftestHtmlGenerator.generate(checkResults);
        int status = SelfTestUtils.findHttpStatusCode(checkResults, true);

        return ResponseEntity
                .status(status)
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }


}
