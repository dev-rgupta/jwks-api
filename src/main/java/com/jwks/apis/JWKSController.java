/**
 * 
 */
package com.jwks.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@GetMapping("/{keyType}/{size}") //alg ,
	public Object getJWKS(@PathVariable String keyType,@PathVariable String size,@RequestParam("alg") String alg) throws Exception {
		return postService.getJWKSByKeyType(keyType,size, alg);
	}

}
