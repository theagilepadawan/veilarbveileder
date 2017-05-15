package no.nav.fo;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UtilsTest {

    @Before
    public void setup() {
        System.clearProperty("portefolje.pilot.enhetliste");
    }


    @Test
    public void skalReturnereFalseDersomIkkeIListe() {
        System.setProperty("portefolje.pilot.enhetliste", "0000,0001");

        assertThat(Utils.enhetErIPilot("0002"), is(false));
    }

    @Test
    public void skalReturnereTrueDersomEnhetErIPilot() {
        System.setProperty("portefolje.pilot.enhetliste", "0000,0001");

        assertThat(Utils.enhetErIPilot("0000"), is(true));
    }

    @Test
    public void skalReturnereTrueDersomListeIkkeFinnes() {
        assertThat(Utils.enhetErIPilot("0002"), is(true));
    }

    @Test
    public void skalReturnereTrueDersomIngenEnheterIListe() {
        System.setProperty("portefolje.pilot.enhetliste", "[]");
        assertThat(Utils.enhetErIPilot("0002"), is(true));
    }

}