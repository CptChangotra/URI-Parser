package uri.implementation;

import uri.Uri;
import uri.UriParser;

public class UriParserImplementation implements UriParser {
	String uri;

	public UriParserImplementation(String uri){
		this.uri = uri;
	}

	@Override
	public Uri parse() {
		String scheme = "";
		String authority = "";
		String query = "";
		String userInfo = "";
		String host = "";
		String path = "";
		String rest = "";
		String delimiterScheme = "://";
		if (uri == null || !uri.contains("://")){
			return null;
		}
		if (uri.contains(delimiterScheme)){
			String[] gotScheme = uri.split(delimiterScheme, 2);
			if (gotScheme.length < 2 || gotScheme[0].isEmpty()){
				return null;
			}
			scheme = gotScheme[0];
			uri = gotScheme[1];
		} else {
			scheme = "";
		}
		if (uri.contains("/")){
			String[] splitted = uri.split("/", 2);
			authority = splitted[0];
			rest = splitted[1];
		} else {
			authority = uri;
			rest = "";
		}
		if (authority.contains("@")){
			String[] splitted = authority.split("@", 2);
			userInfo = splitted[0];
			host = splitted[1];
		} else {
			userInfo = null;
			host = authority;
		}
		if (rest.contains("?")){
			String[] splitted = rest.split("\\?", 2);
			path = splitted[0];
			query = splitted[1];
		} else {
			path = rest.isEmpty() ? "" : rest;
			query = "";
		}
		UriImplementation parsedUri = new UriImplementation(scheme, query, userInfo, host, path);
		return parsedUri;
	}
}
