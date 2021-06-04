/**
 * 
 */
package com.jwks.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ratnesh
 *
 */
@RestController
@RequestMapping("/jwsk")
public class JWKSController {
	@Autowired
	private JWKSService postService;

	@RequestMapping(value = "/{keyType}", method = RequestMethod.GET, produces = { "application/json" })
	public Object getJWKS(@PathVariable String keyType, 
			@RequestParam(name = "alg", required = false) String alg,
			@RequestParam(name = "size", required = false) String size,
			@RequestParam(name = "crv", required = false) String crv,
			@RequestParam(name = "use", required = false) String use) throws Exception {
		return postService.getJWKSByKeyType(keyType, size, alg, crv, use);
	}

}
