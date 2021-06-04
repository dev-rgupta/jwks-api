# jwks-api

## What is JWKS endpoint?
The JSON Web Key Set (JWKS) endpoint is a read-only endpoint that contains the public keys’ information in the JWKS format. The public keys are the counterpart of private keys which is used to sign the tokens.

JWT tokens have a signature to prove their legitimacy to the client or resource servers. In the testing environments, it is easy to validate JWT as we have access to the keystore where we keep the public-private key pair. In the production environments, we need our consumers to validate our tokens before proceeding. Thus we need to have a way to convey our public key to the third party who is going to use that token.
As a solution to the above conundrum JWKS endpoint was introduced.

# JSON Web Key:
A JWK is a JSON object that represents a cryptographic key.The members of the object represent properties of the key, including its value
# JWKS:
A JWK Set is a JSON object that represents a set of JWKs. The JSON object MUST have a "keys" member, with its value being an array of JWKs

In simple terms, JWKS has arrays of keysets. Each keyset can be used to create a public key

```
kty → identifies the cryptographic algorithm  family used with the key, such as “RSA” or “EC”
kid → (key ID) parameter is used to match a specific key. This is used, for instance, to choose among a set of keys within a JWK Set during key rollover. The structure         of the “kid” value is unspecified. When “kid” values are used within a JWK Set, different keys within the JWK Set SHOULD use distinct “kid” values
use → parameter identifies the intended use of the public key. It can be either “sig” (signature) or “enc” (encryption).
alg → parameter identifies the algorithm intended for use with the key.eg in RSA, we can have RSA256 or RSA512 
e,n → are related to RSA algorithms. n is the modulus and e is the exponent. EC type will have different parameters

```
Option's

```
usage: java -jar json-web-key-generator.jar -t <keyType> [options]
 -t,--type <arg>           Key Type, one of: RSA, oct, EC, OKP
 -s,--size <arg>           Key Size in bits, required for RSA and oct key
                           types. Must be an integer divisible by 8
 -c,--curve <arg>          Key Curve, required for EC or OKP key type.
                           Must be one of P-256, secp256k1, P-384, P-521
                           for EC keys or one of Ed25519, Ed448, X25519,
                           X448 for OKP keys.
 -u,--usage <arg>          Usage, one of: enc, sig (optional)
 -a,--algorithm <arg>      Algorithm (optional)
 -i,--id <arg>             Key ID (optional), one will be generated if not
                           defined
 -g,--idGenerator <arg>    Key ID generation method (optional). Can be one
                           of: date, timestamp, sha256, sha1, none. If
                           omitted, generator method defaults to
                           'timestamp'.
 -I,--noGenerateId         <deprecated> Don't generate a Key ID.
                           (Deprecated, use '-g none' instead.)
 -p,--showPubKey           Display public key separately (if applicable)
 -S,--keySet               Wrap the generated key in a KeySet
 -o,--output <arg>         Write output to file. Will append to existing
                           KeySet if -S is used. Key material will not be
                           displayed to console.
 -P,--pubKeyOutput <arg>   Write public key to separate file. Will append
                           to existing KeySet if -S is used. Key material
                           will not be displayed to console. '-o/--output'
                           must be declared as well.
 -x,--x509                 Display keys in X509 PEM format
```

## How we set up this
step 1: First you have to clone this repo as maven project. its a maven based Springboot application
step 2: Start the springboot application (default: http://localhost:8080)
step 3: hit the end point with required parameters i.e. http://localhost:8080/jwsk/{keyType}/{size}?alg={algorithem}
Sample Url http://localhost:8080/jwsk/RSA/2048?alg=RS256 for RSA 

Thats It !!!


https://knowledge.broadcom.com/external/article/142040/jwks-endpoint.html  
https://medium.com/@inthiraj1994/signature-verification-using-jwks-endpoint-in-wso2-identity-server-5ba65c5de086
