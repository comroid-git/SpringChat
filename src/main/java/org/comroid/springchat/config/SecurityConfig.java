package org.comroid.springchat.config;

import org.comroid.springchat.SpringChatApplication;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.oauth2ResourceServer()
                .jwt();
        return security.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        // todo this seems hella wrong
        return new InMemoryClientRegistrationRepository(
                ClientRegistration.withRegistrationId("chat-client")
                        .clientId("chat-client")
                        .clientSecret(SpringChatApplication.OAUTH_SECRET_FILE.getContent())
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                        .scope("username")
                        .authorizationUri("https://auth.comroid.org/authorized")
                        .tokenUri("https://auth.comroid.org/oauth2/token")
                        .userInfoUri("https://auth.comroid.org/oauth2/userinfo")
                        .clientName("SpringChat")
                        .build()
        );
    }
}
