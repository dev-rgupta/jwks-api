/**
 * 
 */
package com.jwks.apis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jwks.key.makers.RSAKeyMaker;
import com.jwks.model.KeysList;
import com.jwks.utils.KeyIdGenerator;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.KeyUse;

/**
 * @author Ratnesh
 *
 */
@Service
public class JWKSService {

	public Object getJWKSByKeyType(String key, String size, String alg) throws Exception {
		try {

			KeyIdGenerator generator = KeyIdGenerator.get(null);
			// check for required fields
			if (key == null) {
				throw new Exception("Key type must be supplied.");
			}

			KeyType keyType = KeyType.parse(key);

			Algorithm keyAlg = null;
			if (!Strings.isNullOrEmpty(alg)) {
				keyAlg = JWSAlgorithm.parse(alg);
			}

			JWK jwk = makeKey(size, generator, null, keyType, null, keyAlg);
			// round trip it through GSON to get a prettyprinter
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

		//	String res =	printKey(jwk, gson);

			
			/* ------------------------------------- */
			ObjectMapper objectMapper = new ObjectMapper();
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

	private static JWK makeKey(String size, KeyIdGenerator kid, String crv, KeyType keyType, KeyUse keyUse,
			Algorithm keyAlg) throws Exception {
		JWK jwk;
		if (keyType.equals(KeyType.RSA)) {
			jwk = makeRsaKey(kid, size, keyType, keyUse, keyAlg);
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

//	private static String printKey(JWK jwk, Gson gson) {
//			JsonElement json = JsonParser.parseString(jwk.toJSONString());
//			System.out.println(gson.toJson(json));
//			
//			return gson.toJson(json);
//	}
}
