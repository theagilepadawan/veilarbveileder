package no.nav.veilarbveileder.utils;

import no.nav.common.client.nom.VeilederNavn;
import no.nav.common.types.identer.NavIdent;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DevNomClientTest {

    @Test
    public void skal_lage_navn_for_ident() {
        DevNomClient devNomClient = new DevNomClient();

        NavIdent ident = NavIdent.of("Z123456");

        VeilederNavn veilederNavn = devNomClient.finnNavn(ident);

        assertEquals(ident, veilederNavn.getNavIdent());
        assertEquals("F_123456", veilederNavn.getFornavn());
        assertEquals("E_123456", veilederNavn.getEtternavn());
        assertEquals("E_123456, F_123456", veilederNavn.getVisningsNavn());
    }

    @Test
    public void skal_lage_navn_for_identer() {
        DevNomClient devNomClient = new DevNomClient();

        VeilederNavn veileder1 = new VeilederNavn()
                .setNavIdent(NavIdent.of("Z444444"))
                .setFornavn("F_444444")
                .setEtternavn("E_444444")
                .setVisningsNavn("E_444444, F_444444");

        VeilederNavn veileder2 = new VeilederNavn()
                .setNavIdent(NavIdent.of("Z777777"))
                .setFornavn("F_777777")
                .setEtternavn("E_777777")
                .setVisningsNavn("E_777777, F_777777");

        List<VeilederNavn> veilederNavn = devNomClient.finnNavn(List.of(veileder1.getNavIdent(), veileder2.getNavIdent()));

        assertEquals(2, veilederNavn.size());
        assertEquals(veileder1, veilederNavn.get(0));
        assertEquals(veileder2, veilederNavn.get(1));
    }


}
