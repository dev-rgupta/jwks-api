/**
 * 
 */
package com.jwks.apis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jwks.key.makers.ECKeyMaker;
import com.jwks.key.makers.OKPKeyMaker;
import com.jwks.key.makers.OctetSequenceKeyMaker;
import com.jwks.key.makers.RSAKeyMaker;
import com.jwks.model.KeysList;
import com.jwks.utils.KeyIdGenerator;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.KeyUse;

/**
 * @author Ratnesh
 *
 */
@Service
public class JWKSService {

	private static List<Curve> ecCurves = Arrays.asList(
			Curve.P_256, Curve.SECP256K1, Curve.P_384, Curve.P_521);

		private static List<Curve> okpCurves = Arrays.asList(
			Curve.Ed25519, Curve.Ed448, Curve.X25519, Curve.X448);

		private static List<KeyType> keyTypes = Arrays.asList(
			KeyType.RSA, KeyType.OCT, KeyType.EC, KeyType.OKP);
		
	public Object getJWKSByKeyType(String key, String size, String alg, String crv, String use) throws Exception {
		try {

			KeyIdGenerator generator = KeyIdGenerator.get(null);
			// check for required fields
			if (key == null) {
				throw new Exception("Key type must be supplied.");
			}

			KeyType keyType = KeyType.parse(key);
			
			KeyUse keyUse = validateKeyUse(use);
			Algorithm keyAlg = null;
			if (!Strings.isNullOrEmpty(alg)) {
				keyAlg = JWSAlgorithm.parse(alg);
			}

			JWK jwk = makeKey(size, generator, crv, keyType, keyUse, keyAlg);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			/* ------------------------------------- */
			KeysList keys = new KeysList();
			List<JWK> keysList= new ArrayList<>();
			keysList.add(jwk);		
			keys.setKeys(keysList);
			/* ------------------------------------- */
			Map<String, Object> mapParam = new HashMap<>();
			mapParam.put("keys", keys.getKeys());
			
			System.out.println("keyList::::"+ keys.getKeys().toString());
			
			JsonElement json = JsonParser.parseString(mapParam.toString());
			
			String jsonString = gson.toJson(json);
			System.out.println("mapParam::::"+ mapParam.toString());
			
			return  jsonString;
		} catch (Exception e) {
			throw new Exception("Invalid key size: " + e.getMessage());
		}

	}
	
	private static KeyUse validateKeyUse(String use) throws Exception {
		try {
			return KeyUse.parse(use);
		} catch (java.text.ParseException e) {
			throw new Exception("Invalid key usage, must be 'sig' or 'enc', got " + use);
		}
	}

	private static JWK makeKey(String size, KeyIdGenerator kid, String crv, KeyType keyType, KeyUse keyUse,
			Algorithm keyAlg) throws Exception {
		JWK jwk;
		if (keyType.equals(KeyType.RSA)) {
			jwk = makeRsaKey(kid, size, keyType, keyUse, keyAlg);
		} else if (keyType.equals(KeyType.OCT)) {
			jwk = makeOctKey(kid, size, keyType, keyUse, keyAlg);
		} else if (keyType.equals(KeyType.EC)) {
			jwk = makeEcKey(kid, crv, keyType, keyUse, keyAlg);
		} else if (keyType.equals(KeyType.OKP)) {
			jwk = makeOkpKey(kid, crv, keyType, keyUse, keyAlg);
		} else {
			throw new Exception("Unknown key type: " + keyType);
		}
		return jwk;
	}

	private static JWK makeRsaKey(KeyIdGenerator kid, String size, KeyType keyType, KeyUse keyUse, Algorithm keyAlg)
			throws Exception {
		if (Strings.isNullOrEmpty(size)) {
			throw new Exception("Key size (in bits) is required for key type " + keyType);
		}

		// surrounding try/catch catches numberformatexception from this
		Integer keySize = Integer.decode(size);
		if (keySize % 8 != 0) {
			throw new Exception("Key size (in bits) must be divisible by 8, got " + keySize);
		}

		return RSAKeyMaker.make(keySize, keyUse, keyAlg, kid);
	}
	private static JWK makeOkpKey(KeyIdGenerator kid, String crv, KeyType keyType, KeyUse keyUse, Algorithm keyAlg) throws Exception {
		if (Strings.isNullOrEmpty(crv)) {
			throw new Exception("Curve is required for key type " + keyType);
		}
		Curve keyCurve = Curve.parse(crv);

		if (!okpCurves.contains(keyCurve)) {
			throw new Exception("Curve " + crv + " is not valid for key type " + keyType);
		}

		return OKPKeyMaker.make(keyCurve, keyUse, keyAlg, kid);
	}

	private static JWK makeEcKey(KeyIdGenerator kid, String crv, KeyType keyType, KeyUse keyUse, Algorithm keyAlg) throws Exception {
		if (Strings.isNullOrEmpty(crv)) {
			throw new Exception("Curve is required for key type " + keyType);
		}
		Curve keyCurve = Curve.parse(crv);

		if (!ecCurves.contains(keyCurve)) {
			throw new Exception("Curve " + crv + " is not valid for key type " + keyType);
		}

		return ECKeyMaker.make(keyCurve, keyUse, keyAlg, kid);
	}

	private static JWK makeOctKey(KeyIdGenerator kid, String size, KeyType keyType, KeyUse keyUse, Algorithm keyAlg) throws Exception {
		if (Strings.isNullOrEmpty(size)) {
			throw new Exception("Key size (in bits) is required for key type " + keyType);
		}

		// surrounding try/catch catches numberformatexception from this
		Integer keySize = Integer.decode(size);
		if (keySize % 8 != 0) {
			throw new Exception("Key size (in bits) must be divisible by 8, got " + keySize);
		}

		return OctetSequenceKeyMaker.make(keySize, keyUse, keyAlg, kid);
	}
}
