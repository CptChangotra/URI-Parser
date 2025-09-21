# 🌐 URI Parser

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Academic-blue.svg)](#license)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](#building-and-testing)

A robust and efficient Java implementation of a URI parser that follows a subset and variant of RFC 3986. This project provides a comprehensive URI parsing library designed for accuracy, performance, and ease of use.

## ✨ Features

- 🔍 **Complete URI Parsing**: Parses URIs into their components (scheme, authority, path, query)
- 🌍 **IPv4 Address Support**: Recognizes and validates IPv4 addresses in the host component
- 🔤 **Percent Encoding**: Handles percent-encoded characters in URIs
- ✅ **Comprehensive Validation**: Validates URI components according to the specified grammar
- 🛡️ **Robust Error Handling**: Returns null for invalid URIs instead of throwing exceptions
- ⚡ **High Performance**: Efficient parsing without unnecessary object creation
- 🔒 **Thread Safe**: Immutable objects ensure thread safety

## 📋 Grammar Specification

The parser supports URIs that conform to the following grammar:

```abnf
URI           = scheme ":" hierarchical [ "?" query ]
hierarchical  = "//" authority path
scheme        = ALPHA *( ALPHA / DIGIT )
authority     = [ userinfo "@" ] host
userinfo      = *( pchar / ":" )
host          = IPv4address / reg-name

IPv4address   = dec-octet "." dec-octet "." dec-octet "." dec-octet
dec-octet     = ["0" ["0"]] DIGIT      ; 000-009 with optional leading zeros
              / ["0"] "1"-"9" DIGIT    ; 010-099
              / "1" DIGIT DIGIT        ; 100-199
              / "2" "0"-"4" DIGIT      ; 200-249
              / "25" "0"-"5"           ; 250-255

reg-name      = *pchar
path          = *( "/" *pchar )        ; begins with "/" or is empty
query         = *pchar
pchar         = unreserved / pct-encoded
unreserved    = ALPHA / DIGIT / "."
pct-encoded   = "%" HEXDIGIT HEXDIGIT

ALPHA         = "A"-"Z" / "a"-"z"
DIGIT         = "0"-"9"
HEXDIGIT      = DIGIT / "A"-"F" / "a"-"f"
```

## 📁 Project Structure

```
📦 URI-Parser
├── 📂 src/
│   ├── 📂 prog2/
│   │   └── 📂 tests/
│   │       └── 📂 pub/
│   │           └── 📄 UriParserTests.java
│   └── 📂 uri/
│       ├── 📄 Host.java
│       ├── 📄 IPv4Address.java
│       ├── 📄 Uri.java
│       ├── 📄 UriParser.java
│       ├── 📄 UriParserFactory.java
│       ├── 📂 implementation/
│       │   ├── 📄 HostImplementation.java
│       │   ├── 📄 IPv4AddressImplementation.java
│       │   ├── 📄 UriImplementation.java
│       │   └── 📄 UriParserImplementation.java
│       └── 📂 tests/
│           └── 📄 SimpleTests.java
├── 📂 bin/                     # Compiled classes
├── 📂 key-creation/           # Key generation utilities
└── 📄 README.md
```

## 🚀 Quick Start

### Basic Usage

```java
import uri.UriParserFactory;
import uri.Uri;

// Create a parser for a URI
UriParser parser = UriParserFactory.create("https://user:pass@example.com/path?query=value");

// Parse the URI
Uri uri = parser.parse();

if (uri != null) {
    // Access URI components
    String scheme = uri.getScheme();        // "https"
    String userInfo = uri.getUserInfo();    // "user:pass"
    String host = uri.getHost().toString(); // "example.com"
    String path = uri.getPath();            // "/path"
    String query = uri.getQuery();          // "query=value"
    
    System.out.println("✅ Successfully parsed: " + uri.toString());
} else {
    System.out.println("❌ Invalid URI format");
}
```

### IPv4 Address Handling

```java
// Parse URI with IPv4 address
UriParser parser = UriParserFactory.create("http://192.168.1.1/path");
Uri uri = parser.parse();

if (uri != null) {
    Host host = uri.getHost();
    if (host instanceof IPv4Address) {
        IPv4Address ipv4 = (IPv4Address) host;
        byte[] octets = ipv4.getOctets();
        System.out.println("🌐 IPv4 Address: " + Arrays.toString(octets));
        // Output: [192, 168, 1, 1]
    }
}
```

### Error Handling

```java
// Invalid URI returns null
UriParser parser = UriParserFactory.create("invalid://malformed");
Uri uri = parser.parse();  // Returns null for invalid URIs

if (uri == null) {
    System.out.println("⚠️ URI validation failed");
}
```

## 📝 Supported URI Examples

### ✅ Valid URIs
- `https://example.com`
- `http://user:pass@example.com/path?query=value`
- `ftp://192.168.1.1/file.txt`
- `scheme://host.sub.domain.com/path/to/resource`
- `https://example.com/path%20with%20spaces`
- `mysql://localhost:3306/database`

### 🌐 IPv4 Addresses
- `http://192.168.1.1/path`
- `https://255.255.255.255`
- `scheme://001.002.003.004` (leading zeros are allowed)
- `ftp://127.0.0.1/home`

### 🔤 Percent Encoding
- `https://example.com/path%20with%20spaces`
- `http://user%3Aname@example.com`
- `https://example.com?query=hello%20world`
- `https://site.com/search?q=java%2Bprogramming`

## 🛠️ Building and Testing

### Prerequisites
- Java 8 or higher
- JUnit 4.13.2 (for testing)

### Compilation

```bash
# Compile all Java files
find src -name "*.java" | xargs javac -d bin

# Or compile specific packages
javac -d bin src/uri/*.java src/uri/implementation/*.java
```

### Running Tests

```bash
# Compile tests
javac -cp bin -d bin src/uri/tests/*.java src/prog2/tests/pub/*.java

# Run the test suite
java -cp bin uri.tests.SimpleTests
```

## 🏗️ Architecture

### Core Components

| Component | Description |
|-----------|-------------|
| **UriParserFactory** | 🏭 Entry point for creating URI parsers |
| **UriParser** | 🔧 Interface for parsing URI strings |
| **Uri** | 📋 Represents a parsed URI with access to all components |
| **Host** | 🏠 Represents the host component (IPv4 address or reg-name) |
| **IPv4Address** | 🌐 Specialized host type for IPv4 addresses |

### Design Principles

- 🔒 **Immutable Objects**: All parsed URI components are immutable for thread safety
- 🛡️ **Null Safety**: Invalid URIs return null rather than throwing exceptions
- 📏 **Grammar Compliance**: Strict adherence to the defined grammar specification
- ⚡ **Performance**: Efficient parsing algorithms with minimal memory overhead
- 🧪 **Testability**: Comprehensive test coverage with clear separation of concerns

## ⚠️ Error Handling

The parser returns `null` for invalid URIs in the following cases:

- ❌ Missing required components (e.g., scheme)
- ❌ Invalid characters in components
- ❌ Malformed IPv4 addresses
- ❌ Invalid percent encoding sequences
- ❌ Grammar violations

## 📊 Performance

- **Memory Efficient**: Minimal object allocation during parsing
- **Fast Parsing**: Optimized algorithms for common URI patterns
- **Thread Safe**: Immutable design allows safe concurrent usage
