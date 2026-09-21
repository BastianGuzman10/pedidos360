package com.pedidos360;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "app.security.enabled=true",
    "app.security.issuer-uri=https://issuer.example.com",
    "app.security.audience=pedidos360-api"
})
@AutoConfigureMockMvc
class SecurityIntegrationTest {
    @Autowired MockMvc mockMvc;
    @MockBean JwtDecoder jwtDecoder;

    @Test void healthEsPublico() throws Exception {
        mockMvc.perform(get("/api/health")).andExpect(status().isOk());
    }

    @Test void endpointProtegidoSinTokenRetorna401() throws Exception {
        mockMvc.perform(get("/api/productos")).andExpect(status().isUnauthorized());
    }

    @Test void tokenSinScopeRetorna403() throws Exception {
        mockMvc.perform(get("/api/productos").with(jwt())).andExpect(status().isForbidden());
    }

    @Test void tokenConScopePermiteAcceso() throws Exception {
        mockMvc.perform(get("/api/productos").with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_access_as_user"))))
            .andExpect(status().isOk());
    }
}
