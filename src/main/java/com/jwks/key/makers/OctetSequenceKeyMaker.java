/**
 *
 */
package com.jwks.key.makers;

import java.security.SecureRandom;

import com.jwks.utils.KeyIdGenerator;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.util.Base64URL;

/**
 * @author Ratnesh
 *
 */
public class OctetSequenceKeyMaker {

    /**
     * @param keySize in bits
     * @return
     */
    public static OctetSequenceKey make(Integer keySize, KeyUse use, Algorithm alg, KeyIdGenerator kid) {

        // holder for the random bytes
        byte[] bytes = new byte[keySize / 8];

        // make a random number generator and fill our holder
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(bytes);

        Base64URL encoded = Base64URL.encode(bytes);

        // make a key
        OctetSequenceKey octetSequenceKey = new OctetSequenceKey.Builder(encoded)
                .keyID(kid.generate(use, bytes))
                .algorithm(alg)
                .keyUse(use)
                .build();

        return octetSequenceKey;
    }

}
