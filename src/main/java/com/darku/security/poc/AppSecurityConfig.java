package com.darku.security.poc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

/**
 * @author Catalin on 11.03.2022
 */
@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppSecurityConfig.class);

    /**
     * A practical example on how to return the roles in an attribute then remap them.
     */
    @Bean
    public GrantedAuthoritiesMapper customMapper() {
        return authorities -> {
            LOGGER.info("Remapping authorities: {}", authorities);
            List<GrantedAuthority> res = new ArrayList<>();
            //remap roles here taken from the scopes attribute and add them to the authorities
            authorities.forEach(grantedAuthority -> {
                if (grantedAuthority instanceof OAuth2UserAuthority o) {
                    final Object scopes = o.getAttributes().get("cognito:groups");
                    if (scopes instanceof List) {
                        res.addAll(((List<?>) scopes).stream()
                                .map(scope -> new OAuth2UserAuthority("ROLE_" + scope.toString(), o.getAttributes()))
                                .toList());
                    }
                }
            });
            return res;
        };
    }

    /**
     * Implemented just for the purpose of logging, and to demonstrate the purpose of customizing
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//
                .antMatchers("/protected/**").authenticated() //
                .antMatchers("/protected-by-super-role/**").hasRole("DEVM8_ADMIN") //
                .anyRequest().permitAll() //
                .and() //
                .oauth2Login(Customizer.withDefaults()) //
                .rememberMe();
    }

    private static final class CustomOAuth2UserService extends DefaultOAuth2UserService {
        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            final OAuth2User oAuth2User = super.loadUser(userRequest);
            LOGGER.info("Loaded user: {}", oAuth2User.getName());
            return oAuth2User;
        }
    }

}
