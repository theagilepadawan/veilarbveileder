package no.nav.veilarbveileder.config;

import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Configuration
public class LdapContextProvider {

    private static Hashtable<String, String> env = new Hashtable<>();

    public static final String LDAP_URL = "LDAP_URL";
    public static final String LDAP_USERNAME = "LDAP_USERNAME";
    public static final String LDAP_PASSWORD = "LDAP_PASSWORD";

    static {
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, getRequiredProperty(LDAP_URL));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, getRequiredProperty(LDAP_USERNAME));
        env.put(Context.SECURITY_CREDENTIALS, getRequiredProperty(LDAP_PASSWORD));
    }

    public static LdapContext getInitialLdapContext() {
        try {
            return new InitialLdapContext(env, null);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
