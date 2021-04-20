package no.nav.veilarbveileder.utils;

import org.junit.Test;

import static no.nav.veilarbveileder.utils.NavnUtils.lagNavn;
import static no.nav.veilarbveileder.utils.NavnUtils.storForbokstav;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NavnUtilsTest {

    @Test
    public void lagNavn_skal_lage_navn() {
        assertEquals("etternavn, fornavn mellomnavn", lagNavn("fornavn", "mellomnavn", "etternavn"));
        assertEquals("etternavn, fornavn", lagNavn("fornavn", null, "etternavn"));
        assertEquals("etternavn, fornavn", lagNavn("fornavn", "", "etternavn"));

    }

    @Test
    public void storForbokstav__skal_lage_navn_med_stor_forbokstav() {
        assertEquals("Navnesen", storForbokstav("Navnesen"));
        assertEquals("Navnesen", storForbokstav("navnesen"));
        assertEquals("Navnesen", storForbokstav("NAVNESEN"));
        assertEquals("Fornavn Etternavn", storForbokstav("fornavn etternavn"));
        assertEquals("Fornavn-Mellomnavn Etternavn", storForbokstav("fornavn-mellomnavn etternavn"));
    }

    @Test
    public void storForbokstav__skal_handtere_null_og_tom_string() {
        assertNull(storForbokstav(null));
        assertEquals("", storForbokstav(""));
    }

}
