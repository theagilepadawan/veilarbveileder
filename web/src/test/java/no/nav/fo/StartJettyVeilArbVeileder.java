package no.nav.fo;

import no.nav.dialogarena.config.DevelopmentSecurity;
import no.nav.sbl.dialogarena.common.jetty.Jetty;

import static no.nav.dialogarena.config.DevelopmentSecurity.setupISSO;
import static no.nav.sbl.dialogarena.common.jetty.JettyStarterUtils.first;
import static no.nav.sbl.dialogarena.common.jetty.JettyStarterUtils.gotKeypress;
import static no.nav.sbl.dialogarena.common.jetty.JettyStarterUtils.waitFor;

public class StartJettyVeilArbVeileder {

    public static final String VEILARBVEILEDER = "veilarbveileder";

    public static void main(String[] args) {
        final Jetty jetty = setupISSO(Jetty.usingWar()
                        .at(VEILARBVEILEDER)
                        .sslPort(9590)
                        .port(9591),
                new DevelopmentSecurity.ISSOSecurityConfig(VEILARBVEILEDER)
        ).buildJetty();
        jetty.startAnd(first(waitFor(gotKeypress())).then(jetty.stop));
    }

}
