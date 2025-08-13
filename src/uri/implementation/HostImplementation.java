package uri.implementation;

import uri.Host;

// TODO implement this class or another implementation of Host
public class HostImplementation implements Host {
	String host;

	public HostImplementation(String host) {
		this.host = host;
	}

	@Override
	public String toString() {
		if (host == null){
			return "";
		} else {
			return "" + host;
		}
	}

}
