package com.test.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.AuthResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@GetMapping("/login")
	public ResponseEntity<AuthResponse> login(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OidcUser user, Model model) {

		AuthResponse response = new AuthResponse();
		response.setUserId(user.getEmail());
		response.setAccessToken(authorizedClient.getAccessToken().getTokenValue());
		response.setRefreshToken(authorizedClient.getRefreshToken().getTokenValue());
		response.setExpireAt(authorizedClient.getAccessToken().getExpiresAt().getEpochSecond());
		List<String> authorities = user.getAuthorities().stream().map(grantAuthorities -> {
			return grantAuthorities.getAuthority();
		}).collect(Collectors.toList());
		response.setAuthorities(authorities);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
