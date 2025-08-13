package uri.implementation;

import uri.Host;
import uri.Uri;


// TODO implement this class or another implementation of Uri
public class UriImplementation implements Uri {
	String scheme;
	String query;
	String userInfo;
	String host;
	String path;

	public UriImplementation(String scheme, String query, String userInfo, String host, String path){
		this.scheme = scheme;
		this.query = query;
		this.userInfo = userInfo;
		this.host = host;
		this.path = path;
	}

	@Override
	public String getScheme() {
		// scheme = ALPHA *( ALPHA / DIGIT )
		if (scheme == null || !scheme.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
			return null;
		}
		return scheme;
	}

	@Override
	public String getUserInfo() {
		if (userInfo == null || userInfo.isEmpty()) return null;

		// userinfo = *( pchar / ":" )
		// pchar = unreserved / pct-encoded
		if (!userInfo.matches("^(?:[a-zA-Z0-9.:]|%[0-9a-fA-F]{2})*$")) {
			return null;
		}
		return userInfo;
	}


	@Override
	public Host getHost() {
		if (host == null || host.isEmpty()) return new HostImplementation(null);

		// Check for valid IPv4: dec-octet "." dec-octet "." dec-octet "." dec-octet
		String ipv4Pattern = "^((0{0,2}\\d|0?\\d\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(0{0,2}\\d|0?\\d\\d|1\\d\\d|2[0-4]\\d|25[0-5])$";
		
		// reg-name = *pchar
		String regNamePattern = "^([a-zA-Z0-9.%]|%[0-9a-fA-F]{2})*$";

		if (host.matches(ipv4Pattern)) {
			return new IPv4AddressImplementation(host);
		} else if (host.matches(regNamePattern)){
			return new HostImplementation(host);
		} else {
			return new HostImplementation(null);
		}
	}

	@Override
	public String getPath() {
		// path = *( "/" *pchar )
		// pchar = unreserved / pct-encoded
		// unreserved = ALPHA / DIGIT / "."
		// pct-encoded = "%" HEXDIGIT HEXDIGIT
		if (path == null || path.isEmpty()) return "";

		// Regex: path consists of zero or more segments starting with '/' followed by valid pchars
		if (!path.matches("^(|(/([a-zA-Z0-9.%]|%[0-9a-fA-F]{2})*)*)$")) {
			return null;
		}
		return path;
	}

	@Override
	public String getQuery() {
		// query = *( pchar / "&" / "=" )
		// pchar = unreserved / pct-encoded
		// unreserved = ALPHA / DIGIT / "."
		if (query == null || query.isEmpty()) return null;

		// This pattern matches valid query characters: ALPHA, DIGIT, ".", "&", "=", or valid % encoding
		if (!query.matches("^([a-zA-Z0-9.]|%[0-9a-fA-F]{2}|[&=])*$")) {
			return null;
		}
		return query;
	}



}
