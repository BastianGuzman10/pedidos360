package com.pedidos360;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"app.security.issuer-uri=https://issuer.example.com", "app.security.audience=pedidos360-api"})
@AutoConfigureMockMvc
class HealthControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean JwtDecoder jwtDecoder;
    @Test void healthIsPublic() throws Exception { mockMvc.perform(get("/api/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP")); }
    @Test void productsRequireToken() throws Exception { mockMvc.perform(get("/api/productos")).andExpect(status().isUnauthorized()); }
}
