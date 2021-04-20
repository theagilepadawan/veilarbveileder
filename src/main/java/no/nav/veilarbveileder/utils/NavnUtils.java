package no.nav.veilarbveileder.utils;

import no.nav.common.utils.StringUtils;

public class NavnUtils {

    public static String lagNavn(String fornavn, String mellomnavn, String etternavn) {
        StringBuilder builder = new StringBuilder();
        builder.append(etternavn);
        builder.append(", ");
        builder.append(fornavn);

        if (StringUtils.notNullOrEmpty(mellomnavn)) {
            builder.append(" ");
            builder.append(mellomnavn);
        }

        return builder.toString();
    }

    public static String storForbokstav(String navn) {
        if (StringUtils.nullOrEmpty(navn)) {
            return navn;
        }

        char[] nameLowerCase = navn.toLowerCase().toCharArray();
        boolean makeUpper = true; // the first character will be upper case

        for (int i = 0; i < nameLowerCase.length; i++) {
            char ch = nameLowerCase[i];

            if (makeUpper) {
                nameLowerCase[i] = Character.toUpperCase(ch);
                makeUpper = false;
            } else if (ch == ' ' || ch == '-') {
                makeUpper = true; // the next character will be upper case
            }
        }

        return new String(nameLowerCase);
    }

}
