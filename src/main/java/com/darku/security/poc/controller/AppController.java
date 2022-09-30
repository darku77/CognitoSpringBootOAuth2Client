package com.darku.security.poc.controller;

import java.util.Objects;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Catalin on 21.03.2022
 */

@RestController
public class AppController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public AppController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/protected/main")
    public String main() {
        return "PROTECTED CONTROLLER";
    }

    @GetMapping("/protected/access-token")
    public String accessToken() {
        final var authorizedClient = authorizedClientService.loadAuthorizedClient("cognito", "catalin");
        if (Objects.nonNull(authorizedClient)) {
            final var accessToken = authorizedClient.getAccessToken();
            return accessToken.getTokenValue();
        }
        return "no access token for user";
    }

    @GetMapping("/protected-by-super-role/hello")
    public String superRoleProtected() {
        return "Hey";
    }
}
