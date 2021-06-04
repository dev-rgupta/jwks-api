package com.jwks.model;

import java.util.List;

import com.nimbusds.jose.jwk.JWK;

public class KeysList {
private List<JWK> keys;

public List<JWK> getKeys() {
	return keys;
}

public void setKeys(List<JWK> keys) {
	this.keys = keys;
}
}
