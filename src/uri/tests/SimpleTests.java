package uri.tests;

import static org.junit.Assert.assertEquals;					
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import uri.Uri;

import uri.Host;
import uri.IPv4Address;
import uri.UriParser;
import uri.UriParserFactory;

/**
 * This class provides a very simple example of how to write tests for this project.
 * You can implement your own tests within this class or any other class within this package.
 * Tests in other packages will not be run and considered for completion of the project.
 */
public class SimpleTests {

	/**
	 * Helper function to determine if the given host is an instance of {@link IPv4Address}.
	 *
	 * @param host the host
	 * @return {@code true} if the host is an instance of {@link IPv4Address}
	 */
	public boolean isIPv4Address(Host host) {
		return host instanceof IPv4Address;
	}

	/**
	 * Helper function to retrieve the byte array representation of a given host which must be an instance of
	 * {@link IPv4Address}.
	 *
	 * @param host the host
	 * @return the byte array representation of the IPv4 address
	 */
	public byte[] getIPv4Octets(Host host) {
		if (!isIPv4Address(host))
			throw new IllegalArgumentException("host must be an IPv4 address");
		return ((IPv4Address) host).getOctets();
	}

	@Test
	public void testNonNull() {
		assertNotNull(UriParserFactory.create("scheme://").parse());
	}

	@Test
	public void testNegativeSimple() {
		assertNull(UriParserFactory.create("").parse());
	}


	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Scheme Tests
	// ------------------------------------------------------------------------------------------------------------------------------------------

	@Test
	public void hasNoScheme() {
		assertNull(UriParserFactory.create("://youtube.com/watch?v=dQw4w9WgXcQ").parse());
	}

	@Test
	public void hasAScheme() {
		assertNotNull("The given URI has a scheme.",
				UriParserFactory.create("https://youtube.com/watch?v=dQw4w9WgXcQ").parse().getScheme());
	}

	@Test
	public void schemeStartsWithAlphabet() {
		Uri uri = UriParserFactory.create("https://youtube.com").parse();
		String scheme = uri.getScheme();
		assertEquals("https", scheme);
	}

	@Test
	public void schemeStartsWithNumber() {
		assertNull(UriParserFactory.create("0https://youtube.com").parse().getScheme());
	}

	@Test
	public void schemeHasOnlyAlphabets() {
		Uri uri = UriParserFactory.create("https://youtube.com").parse();
		String scheme = uri.getScheme();
		assertEquals("https", scheme);
	}

	@Test
	public void schemeHasBothLettersAndNumbers() {
		String scheme = UriParserFactory.create("https123://youtube.com").parse().getScheme();
		assertEquals("https123", scheme);
	}

	@Test
	public void schemeHasOnlyNumbers() {
		assertNull(UriParserFactory.create("01234://youtube.com").parse().getScheme());
	}

	@Test
	public void schemeHasInvalidCharacters() {
		assertNull(UriParserFactory.create("http$%!://unisaarland.de").parse().getScheme());
	}

	@Test
	public void schemeHasAllCaps() {
		assertEquals("HTTPS",
				UriParserFactory.create("HTTPS://www.youtube.com/watch?v=dQw4w9WgXcQ").parse().getScheme());
	}

	// Fix this test - should throw exception
	@Test
	public void schemeWithSomeInvalidChars() {
    	assertNull(UriParserFactory.create("a+b-c.d123://example.com").parse().getScheme());
	}
	@Test
	public void schemeCaseSensitivity() {
		Uri uri = UriParserFactory.create("HtTpS://example.com").parse();
		assertEquals("HtTpS", uri.getScheme());
	}

	@Test
	public void singleCharScheme() {
		Uri uri = UriParserFactory.create("a://example.com").parse();
		assertEquals("a", uri.getScheme());
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// UserInfo Tests
	// ------------------------------------------------------------------------------------------------------------------------------------------

	@Test
	public void hasUserInfo() {
		assertNotNull("The given URI has user info",
				UriParserFactory.create("https://user@www.site.com/docs?raw=1").parse().getUserInfo());
	}

	@Test
	public void hasUserInfoWithPassword() {
		Uri uri = UriParserFactory.create("https://user:pass@youtube.com/watch?v=dQw4w9WgXcQ").parse();
		assertEquals("user:pass", uri.getUserInfo());
	}

	@Test
	public void hasNoUserInfo() {
		assertNull(
				UriParserFactory.create("https://youtube.com/watch?v=dQw4w9WgXcQ").parse().getUserInfo());
	}

	@Test
	public void userInfowithNumbers() {
		assertEquals("person123", UriParserFactory.create("https://person123@example.com").parse().getUserInfo());
	}

	@Test
	public void userInfowithOnlyNumbers() {
		assertEquals("123456", UriParserFactory.create("https://123456@example.com").parse().getUserInfo());
	}

	@Test
	public void userInfowithDots() {
		String userInfo = UriParserFactory.create("https://great.person@example.com").parse().getUserInfo();
		assertEquals("great.person", userInfo);
	}

	@Test
	public void userInfowithOnlyDots() {
		String userInfo = UriParserFactory.create("https://....@example.com").parse().getUserInfo();
		assertEquals("....", userInfo);
	}

	@Test
	public void userInfowithDotsAndNumbers() {
		String userInfo = UriParserFactory.create("https://great.person69@example.com").parse().getUserInfo();
		assertEquals("great.person69", userInfo);
	}

	@Test
	public void userInfowithHexa() {
		String userInfo = UriParserFactory.create("https://user%3Aname@example.com").parse().getUserInfo();
		assertEquals("user%3Aname", userInfo);
	}

	@Test
	public void userInfowithNumberPassword() {
		String userInfo = UriParserFactory.create("https://user:123@example.com").parse().getUserInfo();
		assertEquals("user:123", userInfo);
	}

	@Test
	public void userInfowithMixedPassword() {
		String userInfo = UriParserFactory.create("https://user:name123@example.com").parse().getUserInfo();
		assertEquals("user:name123", userInfo);
	}

	@Test
	public void userInfowithOnlyLettersPassword() {
		String userInfo = UriParserFactory.create("https://user:name@example.com").parse().getUserInfo();
		assertEquals("user:name", userInfo);
	}

	@Test
	public void userInfowithStrongPassword() {
		String userInfo = UriParserFactory.create("https://user:paSSwordIS12345@example.com").parse().getUserInfo();
		assertEquals("user:paSSwordIS12345", userInfo);
	}

	@Test
	public void userInfoHasInvalidCharacters() {
		assertNull(UriParserFactory.create("https://user:pa$$word!S12345@example.com").parse().getUserInfo());
	}

	@Test
	public void userInfoHasInvalidPercentEncoding() {
		assertNull(UriParserFactory.create("https://user%ZZname@example.com").parse().getUserInfo());
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Host Tests: regname
	// ------------------------------------------------------------------------------------------------------------------------------------------

	@Test
	public void hasAHost() {
		assertNotNull("The given URI has a host", UriParserFactory.create("https://www.prog2.de").parse().getHost());
	}

	@Test
	public void hasNoHost() {
		assertEquals("", UriParserFactory.create("https://watch?v=dQw4w9WgXcQ").parse().getHost().toString());
	}

	@Test
	public void hostHasOnlyLetters() {
		String host = UriParserFactory.create("https://www.youtube.com/watch?v=dQw4w9WgXcQ").parse().getHost()
				.toString();
		assertEquals("www.youtube.com", host);
	}

	@Test
	public void hostHasOnlyNumbers() {
		String host = UriParserFactory.create("https://www.124.com").parse().getHost().toString();
		assertEquals("www.124.com", host);
	}

	@Test
	public void hostHasNumbers() {
		String host = UriParserFactory.create("https://www.youtube123.com/watch?v=dQw4w9WgXcQ").parse().getHost()
				.toString();
		assertEquals("www.youtube123.com", host);
	}

	@Test
	public void hostHasSubdomains() {
		String host = UriParserFactory.create("https://cms.sic.saarland/system/courses").parse().getHost().toString();
		assertEquals("cms.sic.saarland", host);
	}

	@Test
	public void hostHasOnlyDots() {
		String host = UriParserFactory.create("https://.../courses").parse().getHost().toString();
		assertEquals("...", host);
	}


	@Test
	public void hostHasPercentEncoder() {
		String host = UriParserFactory.create("https://percent%20encOde123.com").parse().getHost().toString();
		assertEquals("percent%20encOde123.com", host);
	}


	@Test
	public void regnEdgeWow(){
		assertEquals("wtfahpass.c.c.69429.VzW.%0f%4b.%0a.lf.", UriParserFactory.create("scheme://username:@wtfahpass.c.c.69429.VzW.%0f%4b.%0a.lf./%hello").parse().getHost().toString());
	}
	
	@Test
	public void regnEdge1() {
    	Host host = UriParserFactory.create("schEMe://%41%42%43@TeSt.ExaMPle.%31%32%33/path").parse().getHost();
    	assertEquals("TeSt.ExaMPle.%31%32%33", host.toString());
	}

	@Test
	public void regnEdge2() {
   	 	Host host = UriParserFactory.create("abc://user:%25pass@%25%30%30%2E%41%2Ecom/path").parse().getHost();
   		 assertEquals("%25%30%30%2E%41%2Ecom", host.toString());
}

	@Test
	public void regnEdge1_notNull() {
	    Host host = UriParserFactory.create("schEMe://%41%42%43@TeSt.ExaMPle.%31%32%33/path").parse().getHost();
	    assertNotNull("Host should not be null", host);
	}

	@Test
	public void regnEdge2_notNull() {
	    Host host = UriParserFactory.create("abc://user:%25pass@%25%30%30%2E%41%2Ecom/path").parse().getHost();
 	   assertNotNull("Host should not be null", host);
	}

	@Test
	public void regnEdge3_notNull() {
 	   Host host = UriParserFactory.create("XyZ://name@host%2Ena%4D%45.123/path").parse().getHost();
 	   assertNotNull("Host should not be null", host);
	}

	@Test
	public void regnEdge4_notNull() {
	    Host host = UriParserFactory.create("proto://%31user@%41%42%2E%63%2E%64%2E/path").parse().getHost();
	    assertNotNull("Host should not be null", host);
	}

	@Test
	public void regnEdge5_notNull() {
	   Host host = UriParserFactory.create("mySCHEME://user@%61.%62.%63.%64.%65.%66.%67.%68.%69.%70/path").parse().getHost();
 	   assertNotNull("Host should not be null", host);
	}

	@Test
public void hostWithMaxPercentEncoding() {
    // Every single character percent-encoded
    Host host = UriParserFactory.create("scheme://user@%65%78%61%6D%70%6C%65%2E%63%6F%6D/path").parse().getHost();
    assertNotNull("Fully percent-encoded host should parse", host);
}

@Test
public void hostWithMixedCasePercentEncoding() {
    // Mix of uppercase and lowercase hex digits
    Host host = UriParserFactory.create("scheme://user@%45%78%41%6d%50%6c%45%2e%43%6f%4D/path").parse().getHost();
    assertNotNull("Mixed case percent encoding should parse", host);
}

@Test
public void hostWithReservedCharactersEncoded() {
    // Reserved characters that shouldn't appear in host but are encoded
    Host host = UriParserFactory.create("scheme://user@%3A%2F%2F%3F%23%5B%5D%40/path").parse().getHost();
    assertNotNull("Reserved chars encoded in host should parse", host);
}

@Test
public void hostWithUnreservedCharactersUnnecessarilyEncoded() {
    // Unreserved chars that don't need encoding but are encoded anyway
    Host host = UriParserFactory.create("scheme://user@%41%42%43%2D%2E%5F%7E%30%31%32/path").parse().getHost();
    assertNotNull("Unnecessarily encoded unreserved chars should parse", host);
}

@Test
public void hostWithNullByteEncoded() {
    // Null byte in host (should this even be allowed?)
    Host host = UriParserFactory.create("scheme://user@test%00host.com/path").parse().getHost();
    assertNotNull("Host with encoded null byte", host);
}

@Test
public void hostWithControlCharactersEncoded() {
    // Control characters (0x01-0x1F, 0x7F-0x9F)
    Host host = UriParserFactory.create("scheme://user@test%01%02%1F%7F%80%9F.com/path").parse().getHost();
    assertNotNull("Host with encoded control characters", host);
}

@Test
public void hostWithHighUnicodeEncoded() {
    // High Unicode characters encoded as UTF-8 percent sequences
    Host host = UriParserFactory.create("scheme://user@%E2%9C%93%F0%9F%98%80.com/path").parse().getHost();
    assertNotNull("Host with high Unicode encoded", host);
}

@Test
public void hostWithValidButWeirdPercentSequences() {
    // Valid hex but unusual: %00 (null), %20 (space), %7F (DEL)
    Host host = UriParserFactory.create("scheme://user@test%00%20%7F.com/path").parse().getHost();
    assertNotNull("Host with valid but unusual percent sequences", host);
}

@Test
public void hostWithAllValidHexDigits() {
    // Test all valid hex digits in HEXDIGIT range
    Host host = UriParserFactory.create("scheme://user@%41%42%43%44%45%46%61%62%63%64%65%66%30%31%32%33%34%35%36%37%38%39/path").parse().getHost();
    assertNotNull("Host with all valid hex digits", host);
}

@Test
public void hostWithBoundaryHexValues() {
    // Boundary values: %00, %FF, %7F, %80
    Host host = UriParserFactory.create("scheme://user@test%00%FF%7F%80.com/path").parse().getHost();
    assertNotNull("Host with boundary hex values", host);
}

@Test
public void hostWithOnlyDots() {
    // Host with only dots - dots are unreserved chars, so valid in reg-name
    Host host = UriParserFactory.create("scheme://user@%2E%2E%2E%2E/path").parse().getHost();
    assertNotNull("Host with only encoded dots", host);
}

@Test
public void hostWithOnlyUnreservedChars() {
    // Only unreserved = ALPHA / DIGIT / "."
    Host host = UriParserFactory.create("scheme://user@abc123.XYZ.789/path").parse().getHost();
    assertNotNull("Host with only unreserved characters", host);
}

@Test
public void hostWithLeadingDot() {
    // Host starting with dot
    Host host = UriParserFactory.create("scheme://user@%2Eexample.com/path").parse().getHost();
    assertNotNull("Host with leading encoded dot", host);
}

@Test
public void hostWithConsecutiveDots12() {
    // Multiple consecutive dots
    Host host = UriParserFactory.create("scheme://user@test%2E%2E%2Eexample.com/path").parse().getHost();
    assertNotNull("Host with consecutive encoded dots", host);
}

@Test
public void hostWithEncodedUnreservedChars() {
    // Hyphen (-) is NOT in unreserved (ALPHA/DIGIT/"."), so must be encoded
    // %2D = hyphen, but this is actually invalid per your grammar!
    // Let's use valid encoded unreserved chars instead
    Host host = UriParserFactory.create("scheme://user@%41%42%43%2E%44%45%46/path").parse().getHost();
    assertNotNull("Host with encoded unreserved chars", host);
}

@Test
public void hostWith255CharacterLabel() {
    // Maximum length domain label (63 chars) but all percent-encoded = 189 chars
    StringBuilder longLabel = new StringBuilder();
    for (int i = 0; i < 63; i++) {
        longLabel.append("%41"); // 'A' encoded
    }
    Host host = UriParserFactory.create("scheme://user@" + longLabel + ".com/path").parse().getHost();
    assertNotNull("Host with maximum length encoded label", host);
}

@Test
public void hostWithExtremelyLongHost() {
    // Very long hostname with many subdomains
    StringBuilder longHost = new StringBuilder();
    for (int i = 0; i < 50; i++) {
        longHost.append("sub").append(i).append("%2E");
    }
    longHost.append("example%2Ecom");
    Host host = UriParserFactory.create("scheme://user@" + longHost + "/path").parse().getHost();
    assertNotNull("Extremely long encoded host", host);
}

@Test
public void hostWithMixedEncodingStyles() {
    // Mix encoded and non-encoded in same host
    Host host = UriParserFactory.create("scheme://user@test%2Dexample.com%2Dtest/path").parse().getHost();
    assertNotNull("Host with mixed encoding styles", host);
}

@Test
public void hostWithDoubleEncoding() {
    // Double percent encoding (%25 = %, so %2541 = %41 = A)
    Host host = UriParserFactory.create("scheme://user@%2541%2542%2543.com/path").parse().getHost();
    assertNotNull("Host with double encoding", host);
}

@Test
public void hostWithTripleEncoding() {
    // Triple encoding for maximum confusion
    Host host = UriParserFactory.create("scheme://user@%25%32%35%34%31.com/path").parse().getHost();
    assertNotNull("Host with triple encoding", host);
}

	@Test
	public void hostWithAllPrintableAsciiEncoded() {
		// Every printable ASCII character encoded
		StringBuilder allChars = new StringBuilder();
		for (int i = 33; i <= 126; i++) { // Printable ASCII range
			if (i != 47 && i != 63 && i != 35) { // Skip /, ?, # as they have special meaning
				allChars.append("%").append(String.format("%02X", i));
			}
		}
		Host host = UriParserFactory.create("scheme://user@" + allChars + "/path").parse().getHost();
		assertNotNull("Host with all printable ASCII encoded", host);
	}

	@Test
	public void hostWithZeroWidthCharacters() {
		// Zero-width Unicode characters
		Host host = UriParserFactory.create("scheme://user@test%E2%80%8B%E2%80%8C%E2%80%8D.com/path").parse().getHost();
		assertNotNull("Host with zero-width characters", host);
	}

	@Test
	public void hostWithBidirectionalOverrides() {
		// Unicode bidirectional override characters
		Host host = UriParserFactory.create("scheme://user@%E2%80%AE%E2%80%AD%E2%80%ACtest.com/path").parse().getHost();
		assertNotNull("Host with bidirectional overrides", host);
	}

	@Test
	public void hostWithHomoglyphAttack() {
		// Cyrillic characters that look like Latin
		Host host = UriParserFactory.create("scheme://user@%D0%B0%D0%BC%D0%B0%D0%B7%D0%BE%D0%BD.com/path").parse().getHost();
		assertNotNull("Host with homoglyph characters", host);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Host Tests: IPv4
	// ------------------------------------------------------------------------------------------------------------------------------------------

	@Test
	public void ipAddValid() {
		String host = UriParserFactory.create("https://192.168.010.001").parse().getHost().toString();
		assertEquals("192.168.10.1", host);
	}

	@Test
	public void ipAddTwoZeroes() {
		String host = UriParserFactory.create("https://001.002.005.009").parse().getHost().toString();
		assertEquals("1.2.5.9", host);
	}

	@Test
	public void ipAddOneZero() {
		String host = UriParserFactory.create("https://010.027.054.099").parse().getHost().toString();
		assertEquals("10.27.54.99", host);
	}

	@Test
	public void ipAddStartsWithOne() {
		String host = UriParserFactory.create("https://192.127.154.100").parse().getHost().toString();
		assertEquals("192.127.154.100", host);
	}

	@Test
	public void ipAddStartsWithRangeTwoFour() {
		String host = UriParserFactory.create("https://246.227.214.249").parse().getHost().toString();
		assertEquals("246.227.214.249", host);
	}

	@Test
	public void ipAddStartsWithTwentyFive() {
		String host = UriParserFactory.create("https://250.251.254.255").parse().getHost().toString();
		assertEquals("250.251.254.255", host);
	}


	@Test
	public void ipMin() {
		String host = UriParserFactory.create("https://0.0.0.0").parse().getHost().toString();
		assertEquals("0.0.0.0", host);
	}


	@Test
	public void ipMax() {
		String host = UriParserFactory.create("https://255.255.255.255").parse().getHost().toString();
		assertEquals("255.255.255.255", host);

	}

	@Test
	public void testIPv4AddressSimple() {
		Host host = UriParserFactory.create("scheme://1.2.3.4").parse().getHost();
		assertTrue("host must be an IPv4 address", isIPv4Address(host));
	}


	@Test
	public void ipv4WithLeadingZeroesValid() {
		Uri uri = UriParserFactory.create("https://001.002.003.004").parse();
		assertTrue("Should be recognized as IPv4", isIPv4Address(uri.getHost()));
		assertEquals("1.2.3.4", uri.getHost().toString());
	}

	@Test
	public void ipv4WithMixedLeadingZeroes() {
		Uri uri = UriParserFactory.create("https://192.001.1.004").parse();
		assertTrue("Should be recognized as IPv4", isIPv4Address(uri.getHost()));
		assertEquals("192.1.1.4", uri.getHost().toString());
	}

	@Test
	public void ipv4LeadingZerosValid() {
    	// Grammar allows leading zeros: ["0" ["0"]] DIGIT
    	Uri uri = UriParserFactory.create("https://001.002.003.004").parse();
    	assertTrue("Should be IPv4", isIPv4Address(uri.getHost()));
    	assertEquals("1.2.3.4", uri.getHost().toString());
	}

	@Test
	public void ipv4RangeValidation() {
    	// Make sure your parser correctly validates dec-octet ranges
    	// 250-255: "25" "0"-"5"
    	Uri uri = UriParserFactory.create("https://255.255.255.255").parse();
    	assertTrue("Should be IPv4", isIPv4Address(uri.getHost()));
	}

	@Test
	public void regNameVsIPv4Precedence() {
    	// IPv4address has precedence over reg-name
    	// "192.168.1.1" should be parsed as IPv4, not reg-name
    	Uri uri = UriParserFactory.create("https://192.168.1.1").parse();
    	assertTrue("Should be recognized as IPv4", isIPv4Address(uri.getHost()));
	}

	@Test
	public void legalIpv4Edge() {
		assert(isIPv4Address(UriParserFactory.create("http://user@255.249.199.9/path").parse().getHost()));
  	  	String ip = UriParserFactory.create("http://user@255.249.199.9/path").parse().getHost().toString();
   	 	assertNotNull("Legal max-boundary IPv4 should not be null", ip);
	}



	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Path Tests
	// ------------------------------------------------------------------------------------------------------------------------------------------

	@Test
	public void hasNoPath() {
		assertEquals("", UriParserFactory.create("https://prog2.de").parse().getPath());
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Query Tests
	// ------------------------------------------------------------------------------------------------------------------------------------------

	@Test
	public void hasAQuery() {
		assertNotNull("The given URI has a query.",
				UriParserFactory.create("https://youtube.com/watch?v=dQw4w9WgXcQ").parse().getQuery());
	}

	@Test
	public void hasNoQuery() {
		assertNull(UriParserFactory.create("https://facebook.com").parse().getQuery());
	}

	@Test
	public void queryWithNumbers() {
		String query = UriParserFactory.create("https://www.youtube.com/watch?vdQw4w9WgXcQ").parse().getQuery();
		assertEquals("vdQw4w9WgXcQ", query);
	}

	@Test
	public void queryWithAllLetters() {
		String query = UriParserFactory.create("https://www.youtube.com/watch?wowthisisnice").parse().getQuery();
		assertEquals("wowthisisnice", query);
	}

	@Test
	public void queryWithAlphabets() {
		String query = UriParserFactory.create("https://www.youtube.com/watch?vdQw4w9WgXcQ").parse().getQuery();
		assertEquals("vdQw4w9WgXcQ", query);
	}

	@Test
	public void queryWithAllCaps() {
		String query = UriParserFactory.create("https://www.youtube.com/watch?WOWTHISISNICE").parse().getQuery();
		assertEquals("WOWTHISISNICE", query);
	}

	@Test
	public void queryWithDots() {
		String query = UriParserFactory.create("https://www.youtube.com/watch?...").parse().getQuery();
		assertEquals("...", query);
	}

	@Test
	public void queryWithAmpersand() {
		String query = UriParserFactory.create("https://www.youtube.com/watch?vd&Qw4w9WgXcQ").parse().getQuery();
		assertEquals("vd&Qw4w9WgXcQ", query);
	}

	@Test
	public void queryWithEqualTo() {
		String query = UriParserFactory.create("https://www.youtube.com/watch?vd=Qw4w9WgXcQ").parse().getQuery();
		assertEquals("vd=Qw4w9WgXcQ", query);
	}

	@Test
	public void emptyQuery() {
		String query = UriParserFactory.create("https://www.youtube.com/watch?").parse().getQuery();
		assertNull(query);
	}

	@Test
	public void queryWithPercentEncoding() {
		String query = UriParserFactory.create("https://example.com/search?query=hello%20world%26more").parse()
				.getQuery();
		assertEquals("query=hello%20world%26more", query);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Sample Tests & Edge Cases
	// ------------------------------------------------------------------------------------------------------------------------------------------



	@Test
	public void noAuthoritySlashes() {
		assertNull(UriParserFactory.create("scheme:path").parse());
			
		
	}

	@Test
	public void schemeFollowedByPathOnly() {
		// This should actually be valid for some URI schemes (like mailto:, file:)
		// But for http/https schemes that require authority, it should fail
		assertNull(UriParserFactory.create("https:path").parse());
			
		
	}


	@Test
	public void emptyStringInput() {
		// Empty string should throw exception, not return null
		assertNull(UriParserFactory.create("").parse());
			
		
	}

	@Test
	public void malformedUriNoColon() {
		assertNull(UriParserFactory.create("httpexample.com").parse());
			
		
	}

	@Test
	public void malformedUriOnlyScheme() {
		assertNull(UriParserFactory.create("https:").parse());
			
		
	}


	@Test
	public void noAuthorityNotAllowed() {
		// For schemes like https that require authority, this should fail
		assertNull(UriParserFactory.create("https:path").parse());
			
		
	}


	// Additional recommended tests for better coverage:

	// Edge cases and boundary conditions for 100% coverage:

	@Test
	public void schemeCaseSensitivity2() {
		Uri uri = UriParserFactory.create("HTTPS://example.com").parse();
		assertEquals("HTTPS", uri.getScheme()); // or "https" if normalized
	}

	@Test
	public void schemeStartingWithNumber() {
		assertNull(UriParserFactory.create("2http://example.com").parse().getScheme());
			
		
	}

	@Test
	public void schemeWithHyphen() {
		assertNull(UriParserFactory.create("ht-tp://example.com").parse().getScheme());
			
		
	}

	@Test
	public void emptyScheme() {
		assertNull(UriParserFactory.create("://example.com").parse());
			
		
	}

	@Test
	public void hostWithDot() {
		Uri uri = UriParserFactory.create("https://example.com.").parse();
		assertEquals("example.com.", uri.getHost().toString());
	}

	@Test
	public void singleCharacterHost() {
		Uri uri = UriParserFactory.create("https://a").parse();
		assertEquals("a", uri.getHost().toString());
	}

	@Test
	public void pathWithDoubleSlash() {
		Uri uri = UriParserFactory.create("https://example.com//path").parse();
		assertEquals("/path", uri.getPath());
	}

	@Test
	public void pathWithTripleSlash() {
		Uri uri = UriParserFactory.create("https://example.com///path").parse();
		assertEquals("//path", uri.getPath());
	}

	@Test
	public void pathWithOnlySlash() {
		Uri uri = UriParserFactory.create("https://example.com/").parse();
		assertEquals("", uri.getPath());
	}

	@Test
	public void userInfoWithPercent() {
		Uri uri = UriParserFactory.create("https://user%20name:pass@example.com").parse();
		assertEquals("user%20name:pass", uri.getUserInfo());
	}

	@Test
	public void userInfoWithInvalidChar() {
		assertNull(UriParserFactory.create("https://user name:pass@example.com").parse().getUserInfo());
			
		
	}

	@Test
	public void userInfoMultipleColons() {
		Uri uri = UriParserFactory.create("https://user:pass:word@example.com").parse();
		assertEquals("user:pass:word", uri.getUserInfo());
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// End of File :)
	// ------------------------------------------------------------------------------------------------------------------------------------------
}