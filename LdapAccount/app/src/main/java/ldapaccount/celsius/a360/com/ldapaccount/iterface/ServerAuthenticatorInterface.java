package ldapaccount.celsius.a360.com.ldapaccount.iterface;

/**
 * Created by dennisshar on 22/12/2017.
 */

public interface ServerAuthenticatorInterface {
    public String signIn(final String email, final String password);
}
