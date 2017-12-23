package ldapaccount.celsius.a360.com.ldapaccount.iterface;

/**
 * Created by dennisshar on 23/12/2017.
 */

public interface CreateLdapServerConnectionInterface {
    public void createLdapServerConnection(String[] baseDn,String errorMEssage, boolean isConnected);
}
