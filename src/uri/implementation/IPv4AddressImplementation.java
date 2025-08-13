package uri.implementation;

import uri.IPv4Address;

// TODO implement this class or another Implementation of IPv4Address
public class IPv4AddressImplementation extends HostImplementation implements IPv4Address {

	public IPv4AddressImplementation(String host) {
		super(host);
		// TODO implement this
	}

	@Override
	public byte[] getOctets() {
		byte[] octets;
		if (host.contains(".")) {
			String[] splitted = host.split("\\."); // Fixed: changed to String array
			if (splitted.length != 4) {
				throw new IllegalArgumentException("Invalid IPv4 address: not 4 parts");
			}
			octets = new byte[4];
			for (int i = 0; i < 4; i++) {
				int value;
				try {
					value = Integer.parseInt(splitted[i]);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Invalid number."); // Fixed: added semicolon
				}
				if (value < 0 || value > 255) {
					throw new IllegalArgumentException("Octet out of range. Should be between 0 and 255."); // Fixed: added semicolon
				}
				octets[i] = (byte) value;
			}
		} else {
			throw new IllegalArgumentException("Invalid IPv4 address format: no dots found");
		}
		return octets;
	}

	@Override
	public String toString() {
		byte[] octets = getOctets(); // Fixed: get octets from method call
		return (octets[0] & 0xFF) + "." + (octets[1] & 0xFF) + "." + (octets[2] & 0xFF) + "." + (octets[3] & 0xFF);
	}

}