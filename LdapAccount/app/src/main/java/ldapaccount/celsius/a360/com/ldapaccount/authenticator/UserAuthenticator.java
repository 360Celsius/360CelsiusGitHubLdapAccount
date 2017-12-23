package ldapaccount.celsius.a360.com.ldapaccount.authenticator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ldapaccount.celsius.a360.com.ldapaccount.iterface.ServerAuthenticatorInterface;

public class UserAuthenticator implements ServerAuthenticatorInterface {

	private DateFormat dateFormat = null;
	private String authToken = null;

	@Override
	public String signIn(String email, String password) {
		dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
		authToken = email + "-" + dateFormat.format(new Date());
		return authToken;
	}

}
