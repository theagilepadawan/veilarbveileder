package no.nav.fo;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Utils {

    private static Pattern pattern = Pattern.compile("\\d{4}");

    public static boolean enhetErIPilot(String enhet) {

        String enhetsliste = System.getProperty("portefolje.pilot.enhetliste", "");
        enhetsliste = pattern.matcher(enhetsliste).find() ? enhetsliste : "";

        if (isBlank(enhetsliste)) {
            return true;
        }

        List<String> pilotenheter = Arrays.asList(enhetsliste.split(","));

        return pilotenheter.isEmpty() || pilotenheter.contains(enhet);
    }
}
