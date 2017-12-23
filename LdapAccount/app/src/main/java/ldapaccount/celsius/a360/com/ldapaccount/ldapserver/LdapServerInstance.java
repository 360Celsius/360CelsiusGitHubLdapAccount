package ldapaccount.celsius.a360.com.ldapaccount.ldapserver;

import android.util.Log;

import com.unboundid.ldap.sdk.ExtendedResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.extensions.StartTLSExtendedRequest;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

import java.io.Serializable;

import javax.net.SocketFactory;

/**
 * Created by dennisshar on 23/12/2017.
 */

public class LdapServerInstance implements Serializable {

     //The encryption method (0 - no encryption, 1 - SSL, 2 - StartTLS)
    private final int encryption;

    //The host address of the LDAP server
    private final String host;

    //The port number of the LDAP server
    private final int port;

    //The DN to use to bind to the server.
    private final String bindDN;


    //The password to use to bind to the server.
    private final String bindPW;

    public LdapServerInstance(final String host, final int port, final int encryption, final String bindDN, final String bindPW,final String baseDN) {
        this.host = host;
        this.port = port;
        this.encryption = encryption;
        this.bindDN = bindDN;
        this.bindPW = bindPW;
    }


    public LDAPConnection getConnection() throws LDAPException {
        SocketFactory socketFactory = null;
        if (usesSSL()) {
            final SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
            try {
                socketFactory = sslUtil.createSSLSocketFactory();
            } catch (Exception e) {
                throw new LDAPException(ResultCode.LOCAL_ERROR, "Cannot initialize SSL", e);
            }
        }

        final LDAPConnectionOptions options = new LDAPConnectionOptions();
        options.setAutoReconnect(true);
        options.setConnectTimeoutMillis(30000);
        options.setFollowReferrals(false);
        options.setMaxMessageSize(0);

        final LDAPConnection conn = new LDAPConnection(socketFactory, options, host, port);

        if (usesStartTLS()) {
            final SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
            try {
                final ExtendedResult r = conn.processExtendedOperation(new StartTLSExtendedRequest(sslUtil.createSSLContext()));
                if (r.getResultCode() != ResultCode.SUCCESS) {
                    throw new LDAPException(r);
                }
            } catch (LDAPException le) {
                conn.close();
                throw le;
            } catch (Exception e) {
                conn.close();
                throw new LDAPException(ResultCode.CONNECT_ERROR, "Cannot initialize StartTLS", e);
            }
        }

        if ((bindDN != null) && (bindPW != null)) {
            try {
                conn.bind(bindDN, bindPW);
            } catch (LDAPException le) {
                conn.close();
                throw le;
            }
        }

        return conn;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("LDAPServer(host=\"");
        buffer.append(host).append(":").append(port).append("\"");
        buffer.append(", bindDN=\"");
        if (bindDN != null) {
            buffer.append(bindDN);
        }
        buffer.append("\" - ");
        if (usesSSL()) {
            buffer.append("SSL");
        } else if (usesStartTLS()) {
            buffer.append("StartTLS");
        } else {
            buffer.append("No encryption");
        }
        buffer.append(")");
        return buffer.toString();
    }

    public boolean usesSSL() {
        return encryption == 1;
    }

    public boolean usesStartTLS() {
        return encryption == 2;
    }

}
